package com.github.alphabet26;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter for recycler view. Creates view for objects as necessary.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<Shelter> mShelterList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mShelterName, mShelterCapacity, mShelterPhone;

        public ViewHolder(View v) {
            super(v);
            mShelterName = (TextView) itemView.findViewById(R.id.shelter_name);
            mShelterCapacity = (TextView) itemView.findViewById(R.id.shelter_capacity);
            mShelterPhone = (TextView) itemView.findViewById(R.id.shelter_phone);
        }
    }

    // adapter constructor
    public RecyclerAdapter(List<Shelter> shelterList) {
        mShelterList = shelterList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shelter_list_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Shelter shelter = mShelterList.get(position);
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
