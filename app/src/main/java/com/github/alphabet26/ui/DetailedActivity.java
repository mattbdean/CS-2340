package com.github.alphabet26.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.alphabet26.R;
import com.github.alphabet26.model.Shelter;

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

        Shelter shelter = getIntent().getParcelableExtra("SHELTER");

        setContentView(R.layout.activity_detailed);
        shelterTitle = findViewById(R.id.shelterName);
        longAndLat = findViewById(R.id.latAndLong);
        cap = findViewById(R.id.capacity);
        phoneNum = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        gender = findViewById(R.id.gender);

//        List<Shelter> list = App.get().getShelterDao().find();
//        Shelter shelter = list.get(0);
//        for (Shelter shelter2 : list) {
//            if (shelter2.getId() == id) {
//                shelter = shelter2;
//            }
//        }
        shelterTitle.setText(shelter.getName());
        String s = shelter.getLatitude() + ", " + shelter.getLongitude();
        longAndLat.setText(s);
        cap.setText("Capacity: " + shelter.getCapacity());
        phoneNum.setText(shelter.getPhoneNumber());
        address.setText(shelter.getAddress());
        gender.setText("Allowed: " + shelter.getRestrictions());
    }

    public void onDone(View view) {
        finish();
    }

}
