package com.github.alphabet26;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public final class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void onLogout(View view) {
        finish();
    }
}
