package com.example.househelper;

import android.content.Intent;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        BottomNavigationItemView messageBoardLink = findViewById(R.id.chat);
        BottomNavigationItemView supplyListLink = findViewById(R.id.shopList);
        Button addTaskButton = findViewById(R.id.submit_task_button);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        final Intent goToMessageBoard = new Intent(this, MessageBoardActivity.class);
        final Intent goToSupplyList = new Intent(this, SupplyListActivity.class);
        final Intent goToAddTask = new Intent(this, AddTaskActivity.class);
        final Intent goBackToTasks = new Intent(this, TaskListActivity.class);

        messageBoardLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToMessageBoard);
            }
        });
        supplyListLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToSupplyList);
            }
        });
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToAddTask);
            }
        });
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goBackToTasks);
            }
        });
    }


}
