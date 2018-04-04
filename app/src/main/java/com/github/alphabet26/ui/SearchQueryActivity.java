package com.github.alphabet26.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.alphabet26.R;
import com.github.alphabet26.model.AgeRange;
import com.github.alphabet26.model.Gender;
import com.github.alphabet26.model.SearchRequest;

public final class SearchQueryActivity extends AppCompatActivity {
    private TextInputLayout shelterInput;
    private EnumSpinner<Gender> genderSpinner;
    private EnumSpinner<AgeRange> ageRangeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_query);
        this.shelterInput = findViewById(R.id.shelterName);
        this.genderSpinner = findViewById(R.id.genderSpinner);
        this.ageRangeSpinner = findViewById(R.id.ageRangeSpinner);
    }

    public void onSearch(@Nullable View view) {
        Intent i = new Intent(this, DashboardActivity.class);
        i.putExtra(DashboardActivity.PARAM_SEARCH_REQ, SearchRequest.create(
            /*name = */ (shelterInput.getEditText() == null) ? null :
                ((shelterInput.getEditText().getText().toString())),
            /*gender = */ (genderSpinner.getSelectedItem()),
            /*ageRange = */ (ageRangeSpinner.getSelectedItem())));
        startActivity(i);
    }
}
