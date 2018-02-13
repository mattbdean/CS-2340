package com.github.alphabet26;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public final class LoginActivity extends AppCompatActivity {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private EditText usernameField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.usernameField = findViewById(R.id.username);
        this.passwordField = findViewById(R.id.password);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.usernameField.setText("");
        this.passwordField.setText("");
    }

    public void onLogin(View view) {
        if (usernameField.getText().toString().equals(USERNAME) &&
                passwordField.getText().toString().equals(PASSWORD)) {

            startActivity(new Intent(this, DashboardActivity.class));
        } else {
            Snackbar.make(view, R.string.invalid_creds, Snackbar.LENGTH_LONG).show();
        }
    }
}
