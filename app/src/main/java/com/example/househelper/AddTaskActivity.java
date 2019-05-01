package com.example.househelper;

import android.content.Intent;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTaskActivity extends AppCompatActivity {

    private String household;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference("Households");
        setContentView(R.layout.activity_add_task);
        BottomNavigationItemView messageBoardLink = findViewById(R.id.chat);
        BottomNavigationItemView supplyListLink = findViewById(R.id.shopList);
        BottomNavigationItemView taskListLink = findViewById(R.id.tasks);
        Button addTaskButton = findViewById(R.id.submit_task_button);

        final Intent goToTasks = new Intent(this, TaskListActivity.class);
        final Intent goToMessageBoard = new Intent(this, MessageBoardActivity.class);
        final Intent goToSupplyList = new Intent(this, SupplyListActivity.class);
        final Intent goBackToTasks = new Intent(this, TaskListActivity.class);

        taskListLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToTasks);
            }
        });

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
                startActivity(goBackToTasks);
            }
        });

        this.household = "MyHouse"; //TODO: get this info from login
    }

    public void submitTask() {
        EditText taskNameField = findViewById(R.id.task_name_field);
        Spinner frequencySpinner = findViewById(R.id.frequency_spinner);
        Spinner difficultySpinner = findViewById(R.id.difficulty_spinner);

        String taskName = taskNameField.getText().toString();
        String frequency = frequencySpinner.getSelectedItem().toString();
        int difficulty = Integer.parseInt(difficultySpinner.getSelectedItem().toString());

        Task toAdd = new Task(taskName, difficulty, frequency);
        mDatabase.child(this.household).child("Tasks").child(taskName).setValue(toAdd);
    }


}
