package com.example.gabden.testrandomuser.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.support.design.widget.TextInputLayout;
import com.example.gabden.testrandomuser.R;
import com.example.gabden.testrandomuser.activity.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class SettingsActivity extends AppCompatActivity {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 1000;

    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.til_user_value_text_input_layout) TextInputLayout editTextInputLayout;
    @BindView(R.id.et_user_value)
    EditText userValueEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userValueEditText.setText(SharedPreferencesUtils.getSavedUsersCount(this));
    }

    @OnTextChanged(R.id.et_user_value)
    public void textChanged(CharSequence text) {
        String enteredValue = text.toString();

        if (TextUtils.isEmpty(enteredValue)) {
            SharedPreferencesUtils.setSavedUsersCount(this, SharedPreferencesUtils.DEFAULT_USER_COUNT);
        } else {
            int value = Integer.parseInt(enteredValue);

            if (value <= MIN_VALUE || value > MAX_VALUE) {
                SharedPreferencesUtils.setSavedUsersCount(this, SharedPreferencesUtils.DEFAULT_USER_COUNT);
                editTextInputLayout.setError(getString(R.string.input_error));
                editTextInputLayout.setErrorEnabled(true);
            } else {
                SharedPreferencesUtils.setSavedUsersCount(this, enteredValue);
                editTextInputLayout.setErrorEnabled(false);
            }
        }
    }
}
