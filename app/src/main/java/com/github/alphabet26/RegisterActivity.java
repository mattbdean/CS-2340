package com.github.alphabet26;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.ref.WeakReference;

public final class RegisterActivity extends AppCompatActivity {
    private UserDao userDao;

    private EditText nameField;
    private EditText usernameField;
    private EditText passwordField;
    private Spinner userTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDao = new InMemoryUserDao();

        nameField = ((TextInputLayout) findViewById(R.id.reg_name)).getEditText();
        usernameField = ((TextInputLayout) findViewById(R.id.reg_username)).getEditText();
        passwordField = ((TextInputLayout) findViewById(R.id.reg_password)).getEditText();
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
        new RegisterTask(this, userDao).execute(info);
    }

    /**
     * Attempts to register a user. Starts a DashboardActivity when successful.
     */
    private static final class RegisterTask extends AsyncTask<UserRegistrationInfo, Void, User> {
        // Use a WeakReference so if the activity happens to finish before we've finished our
        // processing, we don't prevent the Activity from being GC'd
        private final WeakReference<RegisterActivity> activity;
        private final UserDao userDao;

        RegisterTask(RegisterActivity activity, UserDao userDao) {
            this.activity = new WeakReference<>(activity);
            this.userDao = userDao;
        }

        @Override
        protected User doInBackground(UserRegistrationInfo... userRegistrationInfos) {
            return userDao.register(userRegistrationInfos[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            if (activity.get() != null) {
                activity.get().startActivity(new Intent(activity.get(), DashboardActivity.class));
            }
        }
    }
}
