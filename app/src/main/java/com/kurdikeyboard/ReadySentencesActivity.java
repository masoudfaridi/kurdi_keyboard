package com.kurdikeyboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Ready sentences (opened from the keyboard's tools row): search by title, create, edit,
 * delete, and send the value (or "title" + newline + value) straight into the field the
 * user was typing in.
 */
public class ReadySentencesActivity extends Activity {

    private final List<ReadySentenceStore.Item> all = new ArrayList<>();
    private final List<ReadySentenceStore.Item> shown = new ArrayList<>();
    private BaseAdapter adapter;
    private TextView empty;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_ready_sentences);
        setTitle(R.string.ready_title);

        empty = (TextView) findViewById(R.id.readyEmpty);
        search = (EditText) findViewById(R.id.readySearch);
        ListView list = (ListView) findViewById(R.id.readyList);
        adapter = new BaseAdapter() {
            @Override public int getCount() { return shown.size(); }
            @Override public Object getItem(int i) { return shown.get(i); }
            @Override public long getItemId(int i) { return i; }

            @Override
            public View getView(int i, View convert, ViewGroup parent) {
                View v = convert != null ? convert
                        : getLayoutInflater().inflate(R.layout.item_ready_sentence, parent, false);
                ReadySentenceStore.Item it = shown.get(i);
                ((TextView) v.findViewById(R.id.readyItemTitle)).setText(it.title);
                ((TextView) v.findViewById(R.id.readyItemValue)).setText(it.value);
                return v;
            }
        };
        list.setAdapter(adapter);
        list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> p, View v, int pos, long id) {
                choose(shown.get(pos));
            }
        });
        list.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(android.widget.AdapterView<?> p, View v, int pos, long id) {
                manage(shown.get(pos));
                return true;
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) { filter(); }
        });
        findViewById(R.id.readyAdd).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { edit(null); }
        });
        findViewById(R.id.readyClose).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { finish(); }
        });
        reload();
    }

    @Override protected void onResume() { super.onResume(); PendingText.screenShown(); }
    @Override protected void onPause() { super.onPause(); PendingText.screenHidden(); }

    private void reload() {
        all.clear();
        all.addAll(ReadySentenceStore.list(this));
        filter();
    }

    private void filter() {
        String q = com.kurdikeyboard.predict.TextNormalizer.key(search.getText().toString().trim());
        shown.clear();
        for (ReadySentenceStore.Item it : all) {
            if (q.isEmpty() || com.kurdikeyboard.predict.TextNormalizer.key(it.title).contains(q)) shown.add(it);
        }
        adapter.notifyDataSetChanged();
        empty.setVisibility(shown.isEmpty() ? View.VISIBLE : View.GONE);
        empty.setText(all.isEmpty() ? R.string.ready_empty : R.string.ready_no_match);
    }

    /** send the value alone, or the title and the value on the next line */
    private void choose(final ReadySentenceStore.Item it) {
        String[] options = {getString(R.string.ready_send_value), getString(R.string.ready_send_both)};
        new AlertDialog.Builder(this)
                .setTitle(it.title)
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface d, int which) {
                        send(which == 0 ? it.value : it.title + "\n" + it.value);
                    }
                })
                .setNegativeButton(R.string.canceltext, null)
                .show();
    }

    private void manage(final ReadySentenceStore.Item it) {
        String[] options = {getString(R.string.ready_edit), getString(R.string.ready_delete)};
        new AlertDialog.Builder(this)
                .setTitle(it.title)
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface d, int which) {
                        if (which == 0) {
                            edit(it);
                        } else {
                            new AlertDialog.Builder(ReadySentencesActivity.this)
                                    .setMessage(getString(R.string.ready_delete_confirm, it.title))
                                    .setPositiveButton(R.string.ready_delete, new DialogInterface.OnClickListener() {
                                        @Override public void onClick(DialogInterface d2, int w) {
                                            it.file.delete();
                                            reload();
                                        }
                                    })
                                    .setNegativeButton(R.string.canceltext, null)
                                    .show();
                        }
                    }
                })
                .show();
    }

    /** create (item == null) or edit a sentence */
    private void edit(final ReadySentenceStore.Item item) {
        float d = getResources().getDisplayMetrics().density;
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        int pad = (int) (20 * d);
        box.setPadding(pad, (int) (8 * d), pad, 0);
        final EditText title = field(getString(R.string.ready_key_hint), false);
        final EditText value = field(getString(R.string.ready_value_hint), true);
        if (item != null) {
            title.setText(item.title);
            value.setText(item.value);
        }
        box.addView(title);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = (int) (10 * d);
        box.addView(value, lp);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(item == null ? R.string.ready_new : R.string.ready_edit)
                .setView(box)
                .setPositiveButton(R.string.savetext, null)
                .setNegativeButton(R.string.canceltext, null)
                .create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface di) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        String t = title.getText().toString().trim();
                        String val = value.getText().toString();
                        if (t.isEmpty() || val.trim().isEmpty()) {
                            Toast.makeText(ReadySentencesActivity.this, R.string.ready_fill_both, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        boolean renamed = item == null || !item.title.equals(t);
                        if (renamed && ReadySentenceStore.exists(ReadySentencesActivity.this, t)) {
                            Toast.makeText(ReadySentencesActivity.this, R.string.ready_exists, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (ReadySentenceStore.save(ReadySentencesActivity.this, t, val, item != null ? item.file : null)) {
                            dialog.dismiss();
                            reload();
                        }
                    }
                });
                title.requestFocus();
            }
        });
        dialog.show();
    }

    private EditText field(String hint, boolean multiLine) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setBackgroundResource(R.drawable.kk_field);
        int p = (int) (12 * getResources().getDisplayMetrics().density);
        e.setPadding(p, p, p, p);
        e.setTextColor(getColor(R.color.kk_text));
        e.setHintTextColor(getColor(R.color.kk_text_secondary));
        e.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        if (multiLine) {
            e.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            e.setMinLines(2);
            e.setMaxLines(6);
        } else {
            e.setInputType(InputType.TYPE_CLASS_TEXT);
            e.setSingleLine(true);
        }
        return e;
    }

    private void send(String text) {
        PendingText.set(text);
        finish(); // the original app gets the focus back; the keyboard types the text there
    }
}
