package com.github.alphabet26.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.alphabet26.App;
import com.github.alphabet26.R;
import com.github.alphabet26.model.Shelter;

import java.lang.ref.WeakReference;

public class DetailedActivity extends AppCompatActivity {
    private int shelterId;

    private TextView shelterTitle;
    private TextView longAndLat;
    private TextView cap;
    private TextView phoneNum;
    private TextView address;
    private TextView gender;
    private EditText numBeds;
    private TextView availableBeds;

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
        numBeds = findViewById(R.id.numInput);
        availableBeds = findViewById(R.id.availableBeds);

        updateView((Shelter) getIntent().getParcelableExtra("SHELTER"));
    }

    private void updateView(Shelter shelter) {
        shelterId = shelter.getId();

        shelterTitle.setText(shelter.getName());
        String s = shelter.getLatitude() + ", " + shelter.getLongitude();
        longAndLat.setText(s);
        cap.setText("Capacity: " + shelter.getCapacity());
        phoneNum.setText(shelter.getPhoneNumber());
        address.setText(shelter.getAddress());
        availableBeds.setText("Available Beds: " + shelter.getAvailableBeds());

        String genderStr = shelter.getGender() == null ? "Any" : shelter.getGender().name().toLowerCase();
        gender.setText("Allowed: " + genderStr);
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
            case R.id.home:
                finish();
                return true;
            case R.id.search:
                //goto search
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClaim(View view) {
        new ClaimTask(this).execute(ClaimRequest.claim(shelterId, Integer.parseInt(numBeds.getText().toString())));
    }

    public void onCancel(View view) {
        new ClaimTask(this).execute(ClaimRequest.unclaim(shelterId));
    }

    public void onDone(View view) {
        finish();
    }

    private static final class ClaimTask extends AsyncTask<ClaimRequest, Void, ClaimResult> {
        private final WeakReference<DetailedActivity> activity;

        ClaimTask(DetailedActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        protected ClaimResult doInBackground(ClaimRequest... claimRequests) {
            ClaimRequest req = claimRequests[0];
            App app = App.get();
            Exception exception = null;

            try {
                if (req.action == ClaimAction.CLAIM) {
                    app.getUserDao().claimBeds(app.getShelterDao(), app.getActiveUserId(), req.shelterId, req.numBeds);
                } else {
                    app.getUserDao().releaseClaim(app.getShelterDao(), app.getActiveUserId());
                }
            } catch (Exception e) {
                exception = e;
            }

            return new ClaimResult(exception, app.getShelterDao().find(req.shelterId));
        }

        @Override
        protected void onPostExecute(ClaimResult result) {
            if (activity.get() != null) {
                activity.get().updateView(result.newShelter);

                if (result.error != null) {
                    Snackbar.make(activity.get().address, result.error.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

        }
    }

    private static final class ClaimRequest {
        final ClaimAction action;
        final int numBeds;
        final int shelterId;

        private ClaimRequest(ClaimAction action, int shelterId, int numBeds) {
            this.action = action;
            this.shelterId = shelterId;
            this.numBeds = numBeds;
        }

        static ClaimRequest claim(int shelterId, int numBeds) {
            return new ClaimRequest(ClaimAction.CLAIM, shelterId, numBeds);
        }

        static ClaimRequest unclaim(int shelterId) {
            return new ClaimRequest(ClaimAction.UNCLAIM, shelterId, -1);
        }
    }

    private static final class ClaimResult {
        @Nullable final Throwable error;
        final Shelter newShelter;

        public ClaimResult(@Nullable Throwable error, Shelter newShelter) {
            this.error = error;
            this.newShelter = newShelter;
        }
    }

    private enum ClaimAction {
        CLAIM, UNCLAIM
    }
}
