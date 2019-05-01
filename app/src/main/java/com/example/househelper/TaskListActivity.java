package com.example.househelper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class TaskListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RelativeLayout layout;
    private ArrayList<User> mUsers;
    String username, household, displayname;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        setTitle("Tasks");









        Intent intent = getIntent();
        Bundle getExtras = intent.getExtras();
        username = getExtras.getString("username");
        household = getExtras.getString("houseName");


        mUsers = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.task_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        BottomNavigationItemView messageBoardLink = findViewById(R.id.chat);
        BottomNavigationItemView supplyListLink = findViewById(R.id.shopList);
        Button addTaskButton = findViewById(R.id.add_task_button);

        final Intent goToMessageBoard = new Intent(this, MessageBoardActivity.class);
        final Intent goToSupplyList = new Intent(this, SupplyListActivity.class);
        final Intent goToAddTask = new Intent(this, AddTaskActivity.class);
        final Bundle extras = new Bundle();
        extras.putString("houseName",household);
        extras.putString("username", username);


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
                startActivity(goToAddTask);
            }
        });

        setAdapterAndUpdateData();
    }

    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        createUsers();
        mAdapter = new TaskAdapter(this, this.mUsers);
        mRecyclerView.setAdapter(mAdapter);
    }

    // DUMMY DATA, DELETE LATER
    private void createUsers() {
        ArrayList<String> tasks = new ArrayList<>();
        tasks.add("Take out trash");
        tasks.add("Clean kitchen");
        tasks.add("Wash dishes");
        this.mUsers.add(new User("Davis", tasks));
        this.mUsers.add(new User("Keren", tasks));
        this.mUsers.add(new User("Justin", tasks));
        this.mUsers.add(new User("Caitlin", tasks));
    }
}
