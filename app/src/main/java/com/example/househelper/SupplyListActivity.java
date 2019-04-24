package com.example.househelper;

import android.content.Intent;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SupplyListActivity extends AppCompatActivity {

    ImageView addSupplyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_list);


        BottomNavigationItemView messageBoardLink = findViewById(R.id.chat);
        final Intent MessageBoardIntent = new Intent(this, MessageBoardActivity.class);
        messageBoardLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(MessageBoardIntent);
            }
        });

        BottomNavigationItemView taskListLink = findViewById(R.id.tasks);
        final Intent TaskListLinkIntent = new Intent(this, TaskListActivity.class);
        taskListLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(TaskListLinkIntent);
            }
        });

        addSupplyButton = findViewById(R.id.add_supply_button);
        addSupplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent AddSupplyIntent = new Intent(v.getContext(), AddSupplyActivity.class);
                startActivity(AddSupplyIntent);
            }
        });
    }
}
