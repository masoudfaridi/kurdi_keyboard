package com.kurdikeyboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Pick a phone contact (with search) and send its details straight into the field you were
 * typing in. Opened from the keyboard; the chosen text is handed back to the keyboard, which
 * types it when the original app gets the focus again.
 *
 * Replaces the 2017 flow (an external picker that copied the number to the clipboard, and a
 * second, separate contacts popup).
 */
public class ContactPickerActivity extends Activity {

    private static final int REQUEST_CONTACTS = 11;

    @Override protected void onResume() {
        super.onResume();
        PendingText.screenShown();
        // permission given in the app settings meanwhile
        if (all.isEmpty() && hasPermission()) load();
    }

    @Override protected void onPause() { super.onPause(); PendingText.screenHidden(); }

    private boolean hasPermission() {
        return Build.VERSION.SDK_INT < 23
                || checkSelfPermission(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    static final class Person {
        final long id;
        final String name;
        final List<String[]> phones = new ArrayList<>(); // {label, number}
        final List<String> emails = new ArrayList<>();
        String searchKey;
        Person(long id, String name) { this.id = id; this.name = name; }
    }

    private final List<Person> all = new ArrayList<>();
    private final List<Person> shown = new ArrayList<>();
    private BaseAdapter adapter;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_contact_picker);
        setTitle(R.string.contacts_title);

        empty = (TextView) findViewById(R.id.contactsEmpty);
        ListView list = (ListView) findViewById(R.id.contactsList);
        adapter = new BaseAdapter() {
            @Override public int getCount() { return shown.size(); }
            @Override public Object getItem(int i) { return shown.get(i); }
            @Override public long getItemId(int i) { return shown.get(i).id; }

            @Override
            public View getView(int i, View convert, ViewGroup parent) {
                View v = convert != null ? convert
                        : getLayoutInflater().inflate(R.layout.item_contact, parent, false);
                Person p = shown.get(i);
                TextView avatar = (TextView) v.findViewById(R.id.contactAvatar);
                String initial = p.name.isEmpty() ? "?" : new String(Character.toChars(p.name.codePointAt(0)));
                avatar.setText(initial.toUpperCase(Locale.ROOT));
                GradientDrawable circle = new GradientDrawable();
                circle.setShape(GradientDrawable.OVAL);
                circle.setColor(AVATAR_COLORS[(int) (Math.abs(p.id) % AVATAR_COLORS.length)]);
                avatar.setBackground(circle);
                ((TextView) v.findViewById(R.id.contactName)).setText(p.name);
                String sub = p.phones.isEmpty() ? (p.emails.isEmpty() ? "" : p.emails.get(0)) : p.phones.get(0)[1];
                if (p.phones.size() > 1) sub += "  +" + (p.phones.size() - 1);
                ((TextView) v.findViewById(R.id.contactPhone)).setText(sub);
                return v;
            }
        };
        list.setAdapter(adapter);
        list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                choose(shown.get(position));
            }
        });

        EditText search = (EditText) findViewById(R.id.contactsSearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) { filter(s.toString()); }
        });
        findViewById(R.id.contactsClose).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { finish(); }
        });

        if (!hasPermission()) {
            empty.setText(R.string.contacts_need_permission);
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, REQUEST_CONTACTS);
        } else {
            load();
        }
    }

    private static final int[] AVATAR_COLORS = {0xFF2F6FED, 0xFF0D9488, 0xFFDB2777, 0xFF8B5CF6,
            0xFFEA580C, 0xFF16A34A, 0xFF0284C7, 0xFFC08457};

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if (requestCode != REQUEST_CONTACTS) return;
        if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
            load();
        } else {
            // refused (maybe for good): offer the app settings, where the permission can be given
            empty.setText(getString(R.string.contacts_permission_denied) + "\n\n"
                    + getString(R.string.contacts_open_settings));
            empty.setTextColor(getColor(R.color.kk_primary));
            empty.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    android.content.Intent i = new android.content.Intent(
                            android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            android.net.Uri.fromParts("package", getPackageName(), null));
                    try { startActivity(i); } catch (RuntimeException ignored) {}
                }
            });
        }
    }

    /** reads contacts with their numbers and e-mails, off the main thread */
    private void load() {
        empty.setText(R.string.contacts_loading);
        final ContentResolver resolver = getContentResolver();
        new Thread(new Runnable() {
            @Override public void run() {
                final Map<Long, Person> byId = new LinkedHashMap<>();
                Cursor c = null;
                try {
                    c = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    ContactsContract.CommonDataKinds.Phone.TYPE,
                                    ContactsContract.CommonDataKinds.Phone.LABEL},
                            null, null, null);
                    while (c != null && c.moveToNext()) {
                        long id = c.getLong(0);
                        String name = c.getString(1);
                        String number = c.getString(2);
                        if (number == null) continue;
                        Person p = byId.get(id);
                        if (p == null) {
                            p = new Person(id, name != null ? name : number);
                            byId.put(id, p);
                        }
                        CharSequence label = ContactsContract.CommonDataKinds.Phone.getTypeLabel(
                                getResources(), c.getInt(3), c.getString(4));
                        String clean = number.trim();
                        boolean dup = false;
                        for (String[] ph : p.phones) if (digits(ph[1]).equals(digits(clean))) dup = true;
                        if (!dup) p.phones.add(new String[]{label != null ? label.toString() : "", clean});
                    }
                } catch (RuntimeException ignored) {
                } finally {
                    if (c != null) c.close();
                }
                try {
                    c = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Email.CONTACT_ID,
                                    ContactsContract.CommonDataKinds.Email.ADDRESS},
                            null, null, null);
                    while (c != null && c.moveToNext()) {
                        Person p = byId.get(c.getLong(0));
                        String mail = c.getString(1);
                        if (p != null && mail != null && !p.emails.contains(mail)) p.emails.add(mail);
                    }
                } catch (RuntimeException ignored) {
                } finally {
                    if (c != null) c.close();
                }
                final List<Person> people = new ArrayList<>(byId.values());
                for (Person p : people) {
                    StringBuilder k = new StringBuilder(normalize(p.name));
                    for (String[] ph : p.phones) k.append(' ').append(digits(ph[1]));
                    p.searchKey = k.toString();
                }
                Collections.sort(people, new Comparator<Person>() {
                    @Override public int compare(Person a, Person b) {
                        return a.name.compareToIgnoreCase(b.name);
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        all.clear();
                        all.addAll(people);
                        EditText search = (EditText) findViewById(R.id.contactsSearch);
                        filter(search.getText().toString());
                    }
                });
            }
        }, "contacts").start();
    }

    private void filter(String q) {
        String key = normalize(q.trim());
        String num = digits(q);
        shown.clear();
        for (Person p : all) {
            if (key.isEmpty() || p.searchKey.contains(key) || (num.length() >= 2 && p.searchKey.contains(num))) {
                shown.add(p);
            }
        }
        adapter.notifyDataSetChanged();
        if (shown.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            if (!all.isEmpty()) empty.setText(R.string.contacts_no_match);
        } else {
            empty.setVisibility(View.GONE);
        }
    }

    /** lower case, Arabic/Persian letter variants folded, so "علي" finds "علی" */
    private static String normalize(String s) {
        return com.kurdikeyboard.predict.TextNormalizer.key(s == null ? "" : s);
    }

    /** digits only, Persian and Arabic digits turned into 0-9 */
    private static String digits(String s) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; s != null && i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch >= '0' && ch <= '9') b.append(ch);
            else if (ch >= '۰' && ch <= '۹') b.append((char) ('0' + ch - '۰'));
            else if (ch >= '٠' && ch <= '٩') b.append((char) ('0' + ch - '٠'));
        }
        return b.toString();
    }

    /** what to send: one of the numbers, name + number, or the full card */
    private void choose(final Person p) {
        final List<String> labels = new ArrayList<>();
        final List<String> texts = new ArrayList<>();
        for (String[] ph : p.phones) {
            labels.add((ph[0].isEmpty() ? "" : ph[0] + ": ") + ph[1]);
            texts.add(ph[1]);
        }
        if (!p.phones.isEmpty()) {
            labels.add(getString(R.string.contacts_send_name_number));
            texts.add(p.name + ": " + p.phones.get(0)[1]);
        }
        for (String m : p.emails) {
            labels.add(m);
            texts.add(m);
        }
        StringBuilder card = new StringBuilder(p.name);
        for (String[] ph : p.phones) card.append('\n').append(ph[0].isEmpty() ? "" : ph[0] + ": ").append(ph[1]);
        for (String m : p.emails) card.append('\n').append(m);
        labels.add(getString(R.string.contacts_send_card));
        texts.add(card.toString());

        if (texts.size() == 2 && p.emails.isEmpty() && p.phones.isEmpty()) {
            send(texts.get(0));
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(p.name)
                .setItems(labels.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface d, int which) {
                        send(texts.get(which));
                    }
                })
                .setNegativeButton(R.string.canceltext, null)
                .show();
    }

    private void send(String text) {
        PendingText.set(text);
        finish(); // the original app gets the focus back; the keyboard types the text there
    }
}
