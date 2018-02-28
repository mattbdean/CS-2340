package com.github.alphabet26.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.alphabet26.App;
import com.github.alphabet26.R;
import com.github.alphabet26.model.Shelter;

import java.util.List;

public final class DashboardActivity extends AppCompatActivity {

    private RecyclerView sheltersRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Snackbar toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sheltersRecyclerView = findViewById(R.id.shelters_recycler_view);

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

    private static class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        public interface OnItemClickListener {
            void onItemClick(Shelter shelter);
        }

        private List<Shelter> mShelterList;
        private final OnItemClickListener listener;

        // adapter constructor
        public RecyclerAdapter(List<Shelter> shelterList, OnItemClickListener listener) {
            this.listener = listener;
            mShelterList = shelterList;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shelter_list_row, parent, false);
            final ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public TextView mShelterName, mShelterCapacity, mShelterPhone;

            public ViewHolder(View v) {
                super(v);
                mShelterName = itemView.findViewById(R.id.shelter_name);
                mShelterCapacity = itemView.findViewById(R.id.shelter_capacity);
                mShelterPhone = itemView.findViewById(R.id.shelter_phone);
            }
            public void bind(final Shelter shelter, final OnItemClickListener listener) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        listener.onItemClick(shelter);
                    }
                });
            }
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Shelter shelter = mShelterList.get(position);
            holder.bind(mShelterList.get(position), listener);
            holder.mShelterName.setText(shelter.getName());
            holder.mShelterCapacity.setText(shelter.getCapacity());
            holder.mShelterPhone.setText(shelter.getPhoneNumber());
        }

        // Return the size of list (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mShelterList.size();
        }


    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mShelterName, mShelterCapacity, mShelterPhone;

        public ViewHolder(View v) {
            super(v);
            mShelterName = itemView.findViewById(R.id.shelter_name);
            mShelterCapacity = itemView.findViewById(R.id.shelter_capacity);
            mShelterPhone = itemView.findViewById(R.id.shelter_phone);
        }
        public void bind(final Shelter shelter, final RecyclerAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(shelter);
                }
            });
        }
    }

}
