package com.github.alphabet26.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.alphabet26.App;
import com.github.alphabet26.R;
import com.github.alphabet26.dao.ShelterDao;
import com.github.alphabet26.model.SearchRequest;
import com.github.alphabet26.model.Shelter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public final class DashboardActivity extends AppCompatActivity {
    static final String PARAM_SEARCH_REQ = "searchRequest";

    private ProgressBar progressBar;
    private RecyclerAdapter adapter;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        progressBar = findViewById(R.id.progressBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        RecyclerView recyclerView = findViewById(R.id.shelters_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(new RecyclerAdapter.OnItemClickListener() {
            @Override public void onItemClick(Shelter shelter) {
                Intent intent = new Intent(getBaseContext(), DetailedActivity.class);
                intent.putExtra("SHELTER", shelter);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    // close drawer when item is tapped
                    drawerLayout.closeDrawers();

                    Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                    startActivity(intent);

                    return true;
                }
            });

        SearchRequest req = getIntent().getParcelableExtra(PARAM_SEARCH_REQ);
        new SearchTask(this).execute(req);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search:
                startActivity(new Intent(this, SearchQueryActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private static class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
        public interface OnItemClickListener {
            void onItemClick(Shelter shelter);
        }

        private List<Shelter> mShelterList;
        private final OnItemClickListener listener;

        // adapter constructor
        public RecyclerAdapter(OnItemClickListener listener) {
            this.listener = listener;
            mShelterList = new ArrayList<>();
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shelter_list_row, parent, false);
            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Shelter shelter = mShelterList.get(position);
            holder.bind(mShelterList.get(position), listener);
            holder.mShelterName.setText(shelter.getName());
            holder.mShelterCapacity.setText("" + shelter.getCapacity());
            holder.mShelterPhone.setText(shelter.getPhoneNumber());
        }

        // Return the size of list (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mShelterList.size();
        }

        public void setShelters(List<Shelter> shelters) {
            this.mShelterList = shelters;
            notifyDataSetChanged();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mShelterName, mShelterCapacity, mShelterPhone;

        ViewHolder(View v) {
            super(v);
            mShelterName = itemView.findViewById(R.id.shelter_name);
            mShelterCapacity = itemView.findViewById(R.id.shelter_capacity);
            mShelterPhone = itemView.findViewById(R.id.shelter_phone);
        }

        void bind(final Shelter shelter, final RecyclerAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(shelter);
                }
            });
        }
    }

    private static final class SearchTask extends AsyncTask<SearchRequest, Void, List<Shelter>> {
        private WeakReference<DashboardActivity> activity;

        public SearchTask(DashboardActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            if (activity.get() != null) {
                activity.get().progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected List<Shelter> doInBackground(SearchRequest... searchRequests) {
            ShelterDao shelterDao = App.get().getShelterDao();

            if (searchRequests.length < 0 || searchRequests[0] == null) {
                return shelterDao.list();
            } else {
                return shelterDao.search(searchRequests[0]);
            }
        }

        @Override
        protected void onPostExecute(List<Shelter> shelters) {
            DashboardActivity activity = this.activity.get();

            if (activity != null) {
                activity.progressBar.setVisibility(View.GONE);
                activity.adapter.setShelters(shelters);
            }
        }
    }
}
