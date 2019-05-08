package com.example.househelper;

import android.content.Intent;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    String username, household;
    ArrayList<User> mUsers;
    Toolbar mToolbar;
    EditText dateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_task);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("Households");
        BottomNavigationItemView messageBoardLink = findViewById(R.id.chat);
        BottomNavigationItemView supplyListLink = findViewById(R.id.shopList);
        BottomNavigationItemView taskListLink = findViewById(R.id.tasks);
        BottomNavigationItemView profileLink = findViewById(R.id.profile);
        Button addTaskButton = findViewById(R.id.submit_task_button);
        ImageView datePicker = findViewById(R.id.datePickerButton);
        final EditText dateEditText = findViewById(R.id.date_editText);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = df.format(c);
        dateEditText.setText(formattedDate);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        final EditText taskNameEditText = findViewById(R.id.task_name_field);
        taskNameEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskNameEditText.setText("");
            }
        });

        final Intent goToTasks = new Intent(this, TaskListActivity.class);
        final Intent goToMessageBoard = new Intent(this, MessageBoardActivity.class);
        final Intent goToSupplyList = new Intent(this, SupplyListActivity.class);
        final Intent ProfileLinkIntent = new Intent(this, ProfileActivity.class);

        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        username = extras.getString("username");
        household = extras.getString("houseName");

        mUsers = (ArrayList<User>) intent.getSerializableExtra("users");
        Log.i("Add Task Activity", mUsers.toString());
        taskListLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToTasks);
            }
        });

        messageBoardLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMessageBoard.putExtras(extras);
                startActivity(goToMessageBoard);
            }
        });
        supplyListLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSupplyList.putExtras(extras);
                startActivity(goToSupplyList);
            }
        });
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTask();
            }
        });
        profileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileLinkIntent.putExtras(extras);
                startActivity(ProfileLinkIntent);
            }
        });
    }

    public User assignTask(int score) {
        User minUser = mUsers.get(0);
        for (User u : mUsers) {
            if (u.score < score) {
                minUser = u;
            }
        }
        return minUser;
    }

    public void submitTask() {
        EditText taskNameField = findViewById(R.id.task_name_field);
        Spinner frequencySpinner = findViewById(R.id.frequency_spinner);
        Spinner difficultySpinner = findViewById(R.id.difficulty_spinner);

        String taskName = taskNameField.getText().toString();
        String frequency = frequencySpinner.getSelectedItem().toString();
        String difficulty = difficultySpinner.getSelectedItem().toString();

        //assign task to user with min difficulty score
        User assignedUser = assignTask(TaskListActivity.getDifficultyScore(difficulty));
        Task toAdd = new Task(taskName, difficulty, frequency, false, assignedUser.email);
        int newScore = assignedUser.score + TaskListActivity.getDifficultyScore(difficulty);
        assignedUser.setScore(newScore);

        //TODO: pass udpated list of users back with intent
        mDatabase.child(this.household).child("Tasks").child(taskName).setValue(toAdd);

        final Intent goBackToTasks = new Intent(this, TaskListActivity.class);
        goBackToTasks.putExtra("users", mUsers);
        goBackToTasks.putExtra("houseName", household);
        goBackToTasks.putExtra("username", username);
        startActivity(goBackToTasks);
    }

    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    public void setDateEditText(String date) {
        EditText dateText = findViewById(R.id.date_editText);
        dateText.setText(date);
    }

}
