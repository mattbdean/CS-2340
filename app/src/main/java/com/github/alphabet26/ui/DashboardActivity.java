package com.github.alphabet26.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

/**
 * Dashboard activity
 */
public final class DashboardActivity extends AppCompatActivity {
    static final String PARAM_SEARCH_REQ = "searchRequest";

    private ArrayList<Shelter> displayedShelters;
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
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    // close drawer when item is tapped
                    drawerLayout.closeDrawers();

                    // TODO (mattbdean): Right now if there are multiple menu items it will always
                    // open the MapsActivity

                    // TODO (mattbdean): If a user clicks the "Maps" option, but the SearchTask
                    // hasn't finished we're not going to have any shelters to give the MapsActivity

                    Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                    intent.putParcelableArrayListExtra(
                        MapsActivity.KEY_SHELTERS, displayedShelters);
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

    /**
     * What happens when the user logs out.
     * @param view is the view
     */
    public void onLogout(@Nullable View view) {
        App.get().onLogout();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onLogout(null);
    }

    private static class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
        interface OnItemClickListener {
            void onItemClick(Shelter shelter);
        }

        private List<Shelter> mShelterList;
        private final OnItemClickListener listener;

        // adapter constructor
        RecyclerAdapter(OnItemClickListener listener) {
            this.listener = listener;
            mShelterList = new ArrayList<>();
        }

        // Create new views (invoked by the layout manager)
        @Override @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shelter_list_row, parent, false);
            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Shelter shelter = mShelterList.get(position);
            holder.bind(shelter, listener);
        }

        // Return the size of list (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mShelterList.size();
        }

        void setShelters(List<Shelter> shelters) {
            this.mShelterList = new ArrayList<>(shelters);
            notifyDataSetChanged();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mShelterName;
        private final TextView mShelterCapacity;
        private final TextView mShelterPhone;

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
            mShelterName.setText(shelter.getName());
            mShelterCapacity.setText(itemView.getContext().getString(
                R.string.shelter_capacity, shelter.getCapacity()));
            mShelterPhone.setText(shelter.getPhoneNumber());
        }
    }

    private static final class SearchTask extends AsyncTask<SearchRequest, Void, List<Shelter>> {
        private final WeakReference<DashboardActivity> activity;

        SearchTask(DashboardActivity activity) {
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

            if ((searchRequests.length < 1) || (searchRequests[0] == null)) {
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
                activity.displayedShelters = new ArrayList<>(shelters);
            }
        }
    }
}
