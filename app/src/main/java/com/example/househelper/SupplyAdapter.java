package com.example.househelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SupplyAdapter extends RecyclerView.Adapter<SupplyViewHolder> {

    private Context mContext;
    private ArrayList<Supply> mSupplies;
    private String household;

    public SupplyAdapter(Context context, ArrayList<Supply> supplies, String household) {
        mContext = context;
        mSupplies = supplies;
        this.household = household;
    }

    @Override
    public SupplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.supply_cell_layout, parent, false);
        return new SupplyViewHolder(view, household);
    }

    @Override
    public void onBindViewHolder(SupplyViewHolder holder, int position) {
        // here, we the location that should be displayed at index `position` in our recylcer view
        // everytime the recycler view is refreshed, this method is called getItemCount() times (because
        // it needs to recreate every cell).
        Supply supply = mSupplies.get(position);
        ((SupplyViewHolder) holder).bind(supply);
    }

    @Override
    public int getItemCount() {
        return this.mSupplies.size();
    }

}

class SupplyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Supply mSupply;
    private LinearLayout mSupplyBubbleLayout;
    private Context context;
    private TextView mSupplyTextView;
    private TextView mSupplyUrgencyTextView;
    private CheckBox supplyCheckBox;
    private DatabaseReference dbRef;

    SupplyViewHolder(View itemView, String household) {
        super(itemView);
        this.mSupplyBubbleLayout = itemView.findViewById(R.id.supply_cell_layout);
        this.mSupplyTextView = mSupplyBubbleLayout.findViewById(R.id.supply_name_text_view);
        this.mSupplyUrgencyTextView = mSupplyBubbleLayout.findViewById(R.id.urgency_text_view);
        final String mHousehold = household;

        CheckBox supplyCheckBox = itemView.findViewById(R.id.supply_check_box);
        supplyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    String supplyName = mSupply.getName();
                    dbRef = db.getReference("Households/" + mHousehold + "/Supplies/" + supplyName);
                    dbRef.removeValue();
                }

            }
        });


        itemView.setClickable(true);
        itemView.setOnClickListener(this);
        context = itemView.getContext();
    }

    void bind(Supply supply) {
        this.mSupply = supply;
        mSupplyTextView.setText(supply.getName());
        mSupplyUrgencyTextView.setText("Urgency: " + supply.getUrgency());
    }

    @Override
    public void onClick(View v) {
        Intent goToSupplyInfo = new Intent();
    }

}
