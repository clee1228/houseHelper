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

public class ProfileTaskAdapter extends RecyclerView.Adapter<ProfileTaskViewHolder> {

    private Context mContext;
    private ArrayList<Task> mTasks;
    private String household;

    public ProfileTaskAdapter(Context context, ArrayList<Task> tasks, String household) {
        mContext = context;
        mTasks = tasks;
        this.household = household;
    }

    @Override
    public ProfileTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //TODO: profile_task_cell_layout
        View view = LayoutInflater.from(mContext).inflate(R.layout.supply_cell_layout, parent, false);
        return new ProfileTaskViewHolder(view, household);
    }

    @Override
    public void onBindViewHolder(ProfileTaskViewHolder holder, int position) {
        // here, we the location that should be displayed at index `position` in our recylcer view
        // everytime the recycler view is refreshed, this method is called getItemCount() times (because
        // it needs to recreate every cell).
        Task task = mTasks.get(position);
        ((ProfileTaskViewHolder) holder).bind(task);
    }

    @Override
    public int getItemCount() {
        return this.mTasks.size();
    }

}

class ProfileTaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Task mTask;
    private LinearLayout mSupplyBubbleLayout;
    private Context context;
    private TextView mTaskTextView;
    private TextView mTaskDifficultyTextView;

    private CheckBox taskCheckBox;
    private DatabaseReference dbRef;

    ProfileTaskViewHolder(View itemView, String household) {
        super(itemView);
        this.mSupplyBubbleLayout = itemView.findViewById(R.id.supply_cell_layout);
        this.mTaskTextView = mSupplyBubbleLayout.findViewById(R.id.supply_name_text_view);
        this.mTaskDifficultyTextView = mSupplyBubbleLayout.findViewById(R.id.difficulty_text_view);
        final String mHousehold = household;

        taskCheckBox = itemView.findViewById(R.id.profile_task_check_box);
        taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    String taskName = mTask.getName();
                    //TODO: get the object we want to delete.
//                    dbRef = db.getReference("Households/" + mHousehold + "/Davis/" + taskName);
//                    dbRef.removeValue();
                }

            }
        });


        itemView.setClickable(true);
        itemView.setOnClickListener(this);
        context = itemView.getContext();
    }

    void bind(Task task) {
        this.mTask = task;
        mTaskTextView.setText(mTask.getName());
        mTaskDifficultyTextView.setText("difficulty: " + task.difficulty);
    }

    @Override
    public void onClick(View v) {
        Intent goToSupplyInfo = new Intent();
    }

}
