package com.github.alphabet26;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public final class DashboardActivity extends AppCompatActivity {

    private RecyclerView sheltersRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sheltersRecyclerView = (RecyclerView) findViewById(R.id.shelters_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        sheltersRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerAdapter(App.get().getShelterDao().find());
        sheltersRecyclerView.setAdapter(mAdapter);
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
