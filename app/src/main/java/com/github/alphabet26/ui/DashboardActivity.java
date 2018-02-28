package com.github.alphabet26.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.alphabet26.App;
import com.github.alphabet26.R;
import com.github.alphabet26.model.Shelter;

public final class DashboardActivity extends AppCompatActivity {

    private RecyclerView sheltersRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Snackbar toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sheltersRecyclerView = (RecyclerView) findViewById(R.id.shelters_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        sheltersRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerAdapter(App.get().getShelterDao().find(), new RecyclerAdapter.OnItemClickListener() {
            @Override public void onItemClick(Shelter shelter) {
                goToDetailed(sheltersRecyclerView, shelter);
            }
        });

        sheltersRecyclerView.setAdapter(mAdapter);
    }

    public void goToDetailed(View view, Shelter shelter) {
        Intent intent = new Intent(getBaseContext(), DetailedActivity.class);
        intent.putExtra("SHELTER", shelter);
        startActivity(intent);
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
