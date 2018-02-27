package com.github.alphabet26;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.TextView;

public final class DashboardActivity extends AppCompatActivity {

    private ListView sheltersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sheltersListView = (ListView) findViewById(R.id.shelters_list_view);
        ArrayList<String> shelters = new ArrayList<>();
        shelters.add("Shelter1");
        shelters.add("Shelter2");
        shelters.add("Shelter3");

        String[] listItems = new String[shelters.size()];
// 3
        for(int i = 0; i < shelters.size(); i++){
            listItems[i] = shelters.get(i);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        sheltersListView.setAdapter(adapter);

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
