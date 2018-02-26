package com.github.alphabet26;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public final class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ((TextView) findViewById(R.id.username)).setText(
                getString(R.string.welcome_message, App.get().getActiveUser().getName()));
    }

    public void onLogout(@Nullable View view) {
        App.get().onLogout();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onLogout(null);
    }
}
