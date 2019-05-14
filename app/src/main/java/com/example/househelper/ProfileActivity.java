package com.example.househelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    String household;
    String username;
    String email;
    DatabaseReference dbRef;
    private ArrayList<Task> mTasks;
    ArrayList<Task> allTasks;
    private RecyclerView taskRecyclerView;
    private RecyclerView.Adapter mAdapter;
    ArrayList<User> mUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        Bundle getExtras = intent.getExtras();
        household = getExtras.getString("houseName");
        allTasks = (ArrayList<Task>) intent.getSerializableExtra("tasks");
        mUsers = (ArrayList<User>) intent.getSerializableExtra("users");

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        //change to household
        dbRef = db.getReference("Households/" + household + "/Tasks");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        username = user.getDisplayName();
        email = user.getEmail();
        mTasks = new ArrayList<>();

        final TextView profileText = findViewById(R.id.profile_text);
        profileText.setText(username + "'s Tasks");

        ValueEventListener myDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Set a breakpoint in this method and run in debug mode!!
                // this will be called each time `bearRef` or one of its children is modified
                mTasks = new ArrayList<>();
                Iterable<DataSnapshot> taskData = dataSnapshot.getChildren();
                for (DataSnapshot task : taskData) {
                    try {
                        HashMap<String, String> taskMap = (HashMap<String, String>) task.getValue();
                        String retrievedEmail = taskMap.get("userEmail");
                        Object completed = taskMap.get("completed");
                        Boolean completedBool = (Boolean) completed;
                        if (retrievedEmail.equals(email) && completedBool.equals(false)) {
                            Task loadedTask = new Task(taskMap.get("name"),
                                    taskMap.get("difficulty"), taskMap.get("frequency"));
                            mTasks.add(loadedTask);
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
                setAdapterAndUpdateData();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("0", "cancelled");
            }
        };

        taskRecyclerView = (RecyclerView) findViewById(R.id.profile_task_recycler);
        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbRef.addValueEventListener(myDataListener);
        setAdapterAndUpdateData();

        setButtonOnClicks();

    }

    private void setButtonOnClicks() {
        BottomNavigationItemView messageBoardLink = findViewById(R.id.chat);
        BottomNavigationItemView taskListLink = findViewById(R.id.tasks);
        BottomNavigationItemView supplyListLink = findViewById(R.id.shopList);
        Button viewProfileButton = findViewById(R.id.view_profile_details_button);

        final Intent MessageBoardIntent = new Intent(this, MessageBoardActivity.class);
        final Intent TaskListLinkIntent = new Intent(this, TaskListActivity.class);
        final Intent SupplyListLinkIntent = new Intent(this, SupplyListActivity.class);
        final Intent UserProfileLinkIntent = new Intent(this, UserProfile.class);

        final Bundle extras = new Bundle();
        extras.putString("houseName",household);


        messageBoardLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBoardIntent.putExtras(extras);
                startActivity(MessageBoardIntent);
            }
        });

        taskListLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskListLinkIntent.putExtras(extras);
                TaskListLinkIntent.putExtra("users", mUsers);
                TaskListLinkIntent.putExtra("tasks", allTasks);
                startActivity(TaskListLinkIntent);
            }
        });

        supplyListLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupplyListLinkIntent.putExtras(extras);
                startActivity(SupplyListLinkIntent);
            }
        });

        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileLinkIntent.putExtras(extras);
                UserProfileLinkIntent.putExtra("users", mUsers);
                UserProfileLinkIntent.putExtra("tasks", allTasks);
                startActivity(UserProfileLinkIntent);
            }
        });
    }

    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        mAdapter = new ProfileTaskAdapter(this, mTasks, household);
        taskRecyclerView.setAdapter(mAdapter);
    }

}
