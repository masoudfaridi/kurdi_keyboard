package com.kurdikeyboard;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Start screen: the two setup steps (enable the keyboard, make it the current one) with
 * their live status, a field to try the keyboard, and the old menu buttons.
 */
public class MainActivity extends Activity {

    private TextView stepEnable, stepSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(android.view.View.LAYOUT_DIRECTION_RTL);
        setContentView(R.layout.activity_main);
        stepEnable = (TextView) findViewById(R.id.stepEnable);
        stepSelect = (TextView) findViewById(R.id.stepSelect);
        refreshSteps();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshSteps();
    }

    /** the input-method picker is a dialog: no onResume when it closes */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) refreshSteps();
    }

    private void refreshSteps() {
        boolean enabled = enabledIME();
        boolean selected = enabled && defaultedIME();
        stepEnable.setText((enabled ? "✔  " : "①  ") + getString(R.string.setup_step_enable));
        stepSelect.setText((selected ? "✔  " : "②  ") + getString(R.string.setup_step_select));
        stepEnable.setSelected(enabled);
        stepSelect.setSelected(selected);
        stepSelect.setEnabled(enabled);
        stepSelect.setAlpha(enabled ? 1f : 0.5f);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.stepEnable) {
            startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS));
            Toast.makeText(this, R.string.setup_enable_toast, Toast.LENGTH_LONG).show();
        } else if (id == R.id.stepSelect) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) imm.showInputMethodPicker();
        } else if (id == R.id.buttonactivationIME) {
            startActivity(new Intent(this, ImePreferences.class));
        } else if (id == R.id.buttonGuid) {
            startActivity(new Intent(this, MainActivityViewPager.class));
        }
    }

    private ComponentName myIme() {
        return new ComponentName(this, kurdikeyboard.class);
    }

    boolean enabledIME() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm == null) return false;
        for (InputMethodInfo info : imm.getEnabledInputMethodList()) {
            if (info.getComponent().equals(myIme())) return true;
        }
        return false;
    }

    /**
     * The setting can be null, and the system may store the short ("pkg/.Cls") or the long
     * ("pkg/pkg.Cls") form: the 2017 code crashed on null and missed the long form.
     */
    boolean defaultedIME() {
        String current = Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        if (current == null) return false;
        ComponentName cn = ComponentName.unflattenFromString(current);
        return myIme().equals(cn);
    }
}
