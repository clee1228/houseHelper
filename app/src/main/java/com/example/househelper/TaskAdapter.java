package com.example.househelper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private Context mContext;
    private ArrayList<User> mUsers;

    public TaskAdapter(Context context, ArrayList<User> users) {
        mContext = context;
        mUsers = users;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.task_cell_layout, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        // here, we the location that should be displayed at index `position` in our recylcer view
        // everytime the recycler view is refreshed, this method is called getItemCount() times (because
        // it needs to recreate every cell).
        User user = mUsers.get(position);
        ((TaskViewHolder) holder).bind(user);
    }

    @Override
    public int getItemCount() {
        return this.mUsers.size();
    }

}

class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public User mUser;
    public LinearLayout mTaskBubbleLayout;
    public Context context;
    public TextView mUserNameTextView;

    public TaskViewHolder(View itemView) {
        super(itemView);
        this.mTaskBubbleLayout = itemView.findViewById(R.id.task_cell_layout);
        this.mUserNameTextView = mTaskBubbleLayout.findViewById(R.id.user_name_text_view);

        itemView.setClickable(true);
        itemView.setOnClickListener(this);
        context = itemView.getContext();
    }

    void bind(User user) {
        this.mUser = user;
        mUserNameTextView.setText(user.getName());
        for (Task task : user.getTasks()) {
            LinearLayout taskLine = new LinearLayout(context);
            taskLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            taskLine.setOrientation(LinearLayout.HORIZONTAL);

            TextView newTask = new TextView(this.context);
            newTask.setText(task.name);
            newTask.setPadding(50, 5, 0, 0);
            ImageView completedCheck = null;
            if (task.completed) {
                completedCheck = new ImageView(context);
                completedCheck.setImageResource(R.drawable.baseline_done_black_18dp);
                completedCheck.setPadding(10, 5, 0, 0);
            }
            //TODO: If task completed, add checkmark next to it
            taskLine.addView(newTask);
            if (completedCheck != null) {
                taskLine.addView(completedCheck);
            }
            mTaskBubbleLayout.addView(taskLine);
        }
    }

    @Override
    public void onClick(View v) {
        Intent goToUserTasks = new Intent();
    }

}
