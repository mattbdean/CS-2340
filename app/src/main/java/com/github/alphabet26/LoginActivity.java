package com.github.alphabet26;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public final class LoginActivity extends AppCompatActivity {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private EditText usernameField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.usernameField = ((TextInputLayout) findViewById(R.id.username)).getEditText();
        this.passwordField = ((TextInputLayout) findViewById(R.id.password)).getEditText();

        // Automatically attempt the login the user presses the "next" button on the soft keyboard
        passwordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    onLogin(null);
                    handled = true;
                }
                return handled;
            }
        });
    }

    public void onLogin(View view) {
        if (usernameField.getText().toString().equals(USERNAME) &&
                passwordField.getText().toString().equals(PASSWORD)) {

            startActivity(new Intent(this, DashboardActivity.class));
        } else {
            Snackbar.make(view, R.string.invalid_creds, Snackbar.LENGTH_LONG).show();
        }

        // Clear the form and focus the username field so that the user can start another login
        // attempt
        usernameField.setText("");
        passwordField.setText("");
        usernameField.requestFocus();
    }

    public void onRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
