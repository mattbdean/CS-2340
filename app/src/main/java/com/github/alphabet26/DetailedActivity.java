package com.github.alphabet26;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class DetailedActivity extends AppCompatActivity {

    private TextView shelterTitle;
    private TextView longAndLat;
    private TextView cap;
    private TextView phoneNum;
    private TextView address;
    private TextView gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        shelterTitle = findViewById(R.id.shelterName);
        longAndLat = findViewById(R.id.latAndLong);
        cap = findViewById(R.id.capacity);
        phoneNum = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        gender = findViewById(R.id.gender);

        shelterTitle.setText("Title");



    }

}
