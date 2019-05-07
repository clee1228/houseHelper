package com.example.househelper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RelativeLayout layout;
    private ArrayList<User> mUsers;
    private ArrayList<Task> mTasks;

    String username, household, displayname;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        setTitle("Tasks");
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        household = intent.getStringExtra("houseName");

        if (intent.hasExtra("users")) {
            Log.i("Task List Activity", "got here");
            mUsers = (ArrayList<User>) intent.getSerializableExtra("users");
            Log.i("Task List Activity", mUsers.toString());
        } else {
            mUsers = new ArrayList<>();
        }
        mTasks = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.task_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        BottomNavigationItemView messageBoardLink = findViewById(R.id.chat);
        BottomNavigationItemView supplyListLink = findViewById(R.id.shopList);
        BottomNavigationItemView profileLink = findViewById(R.id.profile);
        Button addTaskButton = findViewById(R.id.add_task_button);

        final Intent goToMessageBoard = new Intent(this, MessageBoardActivity.class);
        final Intent goToSupplyList = new Intent(this, SupplyListActivity.class);
        final Intent goToAddTask = new Intent(this, AddTaskActivity.class);
        final Intent goToProfile = new Intent(this, ProfileActivity.class);
        final Bundle extras = new Bundle();
        extras.putString("houseName",household);

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
                goToAddTask.putExtras(extras);
                goToAddTask.putExtra("users", mUsers);
                startActivity(goToAddTask);
            }
        });

        profileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile.putExtras(extras);
                startActivity(goToProfile);
            }
        });

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        mDatabaseUsers = db.getReference("Households/" + household + "/Users");
        ValueEventListener userDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Set a breakpoint in this method and run in debug mode!!
                // this will be called each time `bearRef` or one of its children is modified
                mUsers = new ArrayList<>();
                Iterable<DataSnapshot> userData = dataSnapshot.getChildren();
                for (DataSnapshot user : userData) {
                    HashMap<String, String> taskMap = (HashMap<String, String>) user.getValue();
                    User loadedUser = new User(taskMap.get("display"), taskMap.get("email"));
                    mUsers.add(loadedUser);
                }
                setAdapterAndUpdateData();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("0", "cancelled");
            }
        };

        mDatabaseUsers.addValueEventListener(userDataListener);

        mDatabaseTasks = db.getReference("Households/" + household + "/Tasks");
        ValueEventListener taskDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Set a breakpoint in this method and run in debug mode!!
                // this will be called each time `bearRef` or one of its children is modified
                mTasks = new ArrayList<>();
                Iterable<DataSnapshot> tasksData = dataSnapshot.getChildren();
                for (DataSnapshot task : tasksData) {
                    HashMap<String, Object> taskMap = (HashMap<String, Object>) task.getValue();
                    Task loadedTask = new Task((String)taskMap.get("name"), (String)taskMap.get("difficulty"),
                            (String)taskMap.get("frequency"), (boolean)taskMap.get("completed"), (String)taskMap.get("userEmail"));
                    mTasks.add(loadedTask);
                }
                setAdapterAndUpdateData();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("0", "cancelled");
            }
        };

        mDatabaseTasks.addValueEventListener(taskDataListener);

        setAdapterAndUpdateData();

        Boolean isNew = intent.getBooleanExtra("isNew", false);
        if (isNew) {
            AlertDialog alert = new AlertDialog.Builder(mRecyclerView.getContext()).setMessage("Visit Profile page to check off completed tasks.").show();
            alert.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.setAdapterAndUpdateData();
    }

    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        mergeUserTasks();
        mAdapter = new TaskAdapter(this, this.mUsers);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void mergeUserTasks() {
        for (Task task : mTasks) {
            if (! task.userEmail.equals("")) {
                for (User user : mUsers) {
                    if (user.getEmail().equals(task.userEmail)) {
                        user.tasks.add(task);
                        user.setScore(user.score + getDifficultyScore(task.difficulty));
                        Log.i("Merge User Tasks", Integer.toString(user.score));
                    }
                }
            }
        }
    }

    public static int getDifficultyScore(String difficulty) {
        switch(difficulty) {
            case "Easy" :
                return 1;
            case "Moderate" :
                return 2;
            case "Hard" :
                return 3;
        }

        return 0;
    }
}
