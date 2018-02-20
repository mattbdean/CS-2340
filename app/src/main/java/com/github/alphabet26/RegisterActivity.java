package com.github.alphabet26;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public final class RegisterActivity extends AppCompatActivity {

    private EditText nameField;
    private EditText usernameField;
    private EditText passwordField;
    private Spinner userTypeSpinner;

    private User _user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameField = ((TextInputLayout) findViewById(R.id.reg_name)).getEditText();
        usernameField = ((TextInputLayout) findViewById(R.id.reg_username)).getEditText();
        passwordField = ((TextInputLayout) findViewById(R.id.reg_password)).getEditText();
        userTypeSpinner = (Spinner) findViewById(R.id.user_type_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, UserType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);

    }
}
