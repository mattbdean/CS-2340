package com.github.alphabet26;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public final class LoginActivity extends AppCompatActivity {
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
        // Try to log in using the given credentials
        new LoginTask(this).execute(usernameField.getText().toString(),
                passwordField.getText().toString());
    }

    public void onRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private static final class LoginTask extends AsyncTask<String, Void, User> {
        private WeakReference<LoginActivity> activity;

        LoginTask(LoginActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        protected User doInBackground(String... authData) {
            if (authData.length < 2)
                throw new IllegalArgumentException("Expecting a username and password, got only " +
                    authData.length + " arguments.");

            return App.get().getUserDao().login(authData[0], authData[1]);
        }

        @Override
        protected void onPostExecute(User user) {
            LoginActivity activity = this.activity.get();

            if (activity == null) {
                // The activity has already finished, this result is irrelevant
                return;
            }

            // Clear the form and focus the username field so that the user can start another login
            // attempt
            activity.usernameField.setText("");
            activity.passwordField.setText("");
            activity.usernameField.requestFocus();

            if (user == null) {
                Snackbar.make(activity.findViewById(R.id.logo),
                        R.string.invalid_creds,
                        Snackbar.LENGTH_LONG).show();
            } else {
                App.get().onLogin(user);
                activity.startActivity(new Intent(activity, DashboardActivity.class));
            }
        }
    }
}
