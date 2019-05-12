package com.example.househelper;

import android.content.Intent;
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
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TaskListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RelativeLayout layout;
    private ArrayList<User> mUsers;
    private ArrayList<Task> mTasks;
    private Date rotationDate;
    private boolean checkRotate;

    String username, household, displayname;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseTasks;
    private DatabaseReference mDatabaseRotation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Log.i("TASK LIST", "onCreate called");
        setContentView(R.layout.activity_task_list);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        checkRotate = false;

        Intent intent = getIntent();

        household = intent.getStringExtra("houseName");
//        if (savedInstanceState != null) {
//            // Restore value of members from saved state
//            mUsers = (ArrayList<User>)savedInstanceState.getSerializable("users");
//            mTasks = (ArrayList<Task>)savedInstanceState.getSerializable("tasks");
//        } else {
//            // Probably initialize members with default values for a new instance
//            mUsers = new ArrayList<>();
//            mTasks = new ArrayList<>();
//        }


        if (intent.hasExtra("users")) {
            mUsers = (ArrayList<User>) intent.getSerializableExtra("users");
            mTasks = (ArrayList<Task>) intent.getSerializableExtra("tasks");
        } else {
            mUsers = new ArrayList<>();
            mTasks = new ArrayList<>();
        }
        rotationDate = new Date();
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
                try {
                    for (DataSnapshot task : tasksData) {
                        HashMap<String, Object> taskMap = (HashMap<String, Object>) task.getValue();
                        Task loadedTask = new Task((String)taskMap.get("name"), (String)taskMap.get("difficulty"),
                                (String)taskMap.get("frequency"), (boolean)taskMap.get("completed"), (String)taskMap.get("userEmail"));
                        mTasks.add(loadedTask);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

                setAdapterAndUpdateData();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("0", "cancelled");
            }
        };

        mDatabaseTasks.addValueEventListener(taskDataListener);

        mDatabaseRotation = db.getReference("Households/" + household + "/RotateDate");
        ValueEventListener rotationDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Set a breakpoint in this method and run in debug mode!!
                // this will be called each time `bearRef` or one of its children is modified
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    rotationDate = df.parse(dataSnapshot.getValue(String.class));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("0", "cancelled");
            }
        };

        mDatabaseRotation.addValueEventListener(rotationDataListener);

        messageBoardLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToMessageBoard.putExtras(extras);
                goToMessageBoard.putExtra("users", mUsers);
                goToMessageBoard.putExtra("tasks", mTasks);
                startActivity(goToMessageBoard);
            }
        });
        supplyListLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSupplyList.putExtras(extras);
                goToSupplyList.putExtra("users", mUsers);
                goToSupplyList.putExtra("tasks", mTasks);
                startActivity(goToSupplyList);
            }
        });
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddTask.putExtras(extras);
                goToAddTask.putExtra("users", mUsers);
                goToAddTask.putExtra("tasks", mTasks);
                startActivity(goToAddTask);
            }
        });

        profileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile.putExtras(extras);
                goToProfile.putExtra("users", mUsers);
                goToProfile.putExtra("tasks", mTasks);
                startActivity(goToProfile);
            }
        });

        Boolean isNew = intent.getBooleanExtra("isNew", false);
        if (isNew) {
            AlertDialog alert = new AlertDialog.Builder(mRecyclerView.getContext()).setMessage("Visit Profile page to check off completed tasks.").show();
            alert.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("users", mUsers);
        savedInstanceState.putSerializable("tasks", mTasks);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void setAdapterAndUpdateData() {
        mergeUserTasks();
        mAdapter = new TaskAdapter(this, this.mUsers);
        mRecyclerView.setAdapter(mAdapter);
    }

    //called during onCreate()
    private void rotateTasks(Date d) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Log.i("ROTATE TASKS", cal1.getTime().toString());
        Log.i("ROTATE TASKS", cal2.getTime().toString());
        cal1.setTime(d);
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        if (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
            Log.i("ROTATE TASKS", "got here");
            ArrayList<Task> currTasks= mUsers.get(0).tasks;
            for (Task t : currTasks) {
                t.markAsIncomplete();
                mDatabaseTasks.child(t.name).child("userEmail").setValue(mUsers.get(1).email);
            }
            for (int i = 1; i < mUsers.size(); i++) {
                ArrayList<Task> newCurr = mUsers.get(i).getTasks();
                for (Task t : newCurr) {
                    t.markAsIncomplete();
                    String newEmail = mUsers.get(0).email;
                    if (i+1 != mUsers.size()) {
                        newEmail = mUsers.get(i+1).email;
                    }
                    mDatabaseTasks.child(t.name).child("userEmail").setValue(newEmail);
                }
//                mUsers.get(i).setTasks(currTasks);
                currTasks = newCurr;
            }
//            mUsers.get(0).setTasks(currTasks);
            cal1.add(Calendar.DATE, 1);
            try {
                mDatabaseRotation.setValue(AddTaskActivity.getNextMonday(df.format(cal1.getTime())));
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    //performs rotate task logic here
    private void mergeUserTasks() {
        if (checkRotate) {
            return;
        }
        Log.i("MERGING", "got here");
        for (Task task : mTasks) {
            if (! task.userEmail.equals("")) {
                for (User user : mUsers) {
                    if (user.getEmail().equals(task.userEmail)) {
                        if (!user.tasks.contains(task)) {
                            user.tasks.add(task);
                            user.setScore(user.score + getDifficultyScore(task.difficulty));
                        }
                    }
                }
            }
        }
        if (mUsers.size() > 0 && !checkRotate) {
            rotateTasks(rotationDate);
            checkRotate = true;
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
