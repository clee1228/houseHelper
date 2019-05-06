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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    String household;
    String username;
    DatabaseReference dbRef;
    private RecyclerView taskRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        Bundle getExtras = intent.getExtras();
        household = getExtras.getString("houseName");

        dbRef = FirebaseDatabase.getInstance().getReference("Households");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        username = user.getDisplayName();

        final TextView profileText = findViewById(R.id.profile_text);
        profileText.setText(username + "'s Tasks");

        taskRecyclerView = (RecyclerView) findViewById(R.id.profile_task_recycler);
        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setButtonOnClicks();

    }

    private void setButtonOnClicks() {
        BottomNavigationItemView messageBoardLink = findViewById(R.id.chat);
        BottomNavigationItemView taskListLink = findViewById(R.id.tasks);
        BottomNavigationItemView supplyListLink = findViewById(R.id.shopList);

        final Intent MessageBoardIntent = new Intent(this, MessageBoardActivity.class);
        final Intent TaskListLinkIntent = new Intent(this, TaskListActivity.class);
        final Intent SupplyListLinkIntent = new Intent(this, SupplyListActivity.class);

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
    }

}
