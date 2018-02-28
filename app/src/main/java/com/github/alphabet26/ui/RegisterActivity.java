package com.github.alphabet26.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.alphabet26.App;
import com.github.alphabet26.R;
import com.github.alphabet26.model.User;
import com.github.alphabet26.model.UserRegistrationInfo;
import com.github.alphabet26.model.UserType;

import java.lang.ref.WeakReference;

public final class RegisterActivity extends AppCompatActivity {
    private EditText nameField;
    private EditText usernameField;
    private EditText passwordField;
    private EditText vPasswordField;
    private Spinner userTypeSpinner;
    private Snackbar failed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameField = ((TextInputLayout) findViewById(R.id.reg_name)).getEditText();
        usernameField = ((TextInputLayout) findViewById(R.id.reg_username)).getEditText();
        passwordField = ((TextInputLayout) findViewById(R.id.reg_password)).getEditText();
        vPasswordField = ((TextInputLayout) findViewById(R.id.reg_passwordV)).getEditText();
        userTypeSpinner = findViewById(R.id.user_type_spinner);

        ArrayAdapter<UserType> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, UserType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);
    }

    public void onRegister(View view) {
        UserRegistrationInfo info = new UserRegistrationInfo(
                nameField.getText().toString(),
                usernameField.getText().toString(),
                passwordField.getText().toString(),
                (UserType) userTypeSpinner.getSelectedItem()
        );
        if (passwordField.getText().toString().equals(vPasswordField.getText().toString())) {
            new RegisterTask(this).execute(info);
        } else {
            Snackbar.make(findViewById(R.id.reg_name),
                "Passwords don't match",
                Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Attempts to register a user. Starts a DashboardActivity when successful.
     */
    private static final class RegisterTask extends AsyncTask<UserRegistrationInfo, Void, User> {
        // Use a WeakReference so if the activity happens to finish before we've finished our
        // processing, we don't prevent the Activity from being GC'd
        private final WeakReference<RegisterActivity> activity;

        RegisterTask(RegisterActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        protected User doInBackground(UserRegistrationInfo... userRegistrationInfos) {
            User newUser = App.get().getUserDao().register(userRegistrationInfos[0]);
            App.get().onLogin(newUser);

            return newUser;
        }

        @Override
        protected void onPostExecute(User user) {
            if (activity.get() != null) {
                activity.get().startActivity(new Intent(activity.get(), DashboardActivity.class));
            }
        }
    }
}
