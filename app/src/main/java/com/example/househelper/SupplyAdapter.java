package com.example.househelper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SupplyAdapter extends RecyclerView.Adapter<SupplyViewHolder> {

    private Context mContext;
    private ArrayList<Supply> mSupplies;

    public SupplyAdapter(Context context, ArrayList<Supply> supplies) {
        mContext = context;
        mSupplies = supplies;
    }

    @Override
    public SupplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.supply_cell_layout, parent, false);
        return new SupplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SupplyViewHolder holder, int position) {
        // here, we the location that should be displayed at index `position` in our recylcer view
        // everytime the recycler view is refreshed, this method is called getItemCount() times (because
        // it needs to recreate every cell).
//        User user = mUsers.get(position);
//        ((SupplyViewHolder) holder).bind(user);
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

    SupplyViewHolder(View itemView) {
        super(itemView);
        this.mSupplyBubbleLayout = itemView.findViewById(R.id.supply_cell_layout);
        this.mSupplyTextView = mSupplyBubbleLayout.findViewById(R.id.supply_name_text_view);

        itemView.setClickable(true);
        itemView.setOnClickListener(this);;
        context = itemView.getContext();
    }

    void bind(Supply supply) {
        this.mSupply = supply;
        mSupplyTextView.setText(supply.getName());
//        }
    }

    @Override
    public void onClick(View v) {
//        Log.i("TASK VIEW HOLDER", "CLICK REGISTERED");
        Intent goToSupplyInfo = new Intent();
    }

}
