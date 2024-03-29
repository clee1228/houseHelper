package com.example.househelper;

import android.content.Intent;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SupplyListActivity extends AppCompatActivity {

    Button addSupplyButton;
    private ArrayList<Supply> mSupplies;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    String household;
    DatabaseReference dbRef;
    ArrayList<User> mUsers;
    ArrayList<Task> mTasks;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_list);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        mRecyclerView = findViewById(R.id.supply_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSupplies = new ArrayList<>();


        Intent intent = getIntent();
        Bundle getExtras = intent.getExtras();
        household = getExtras.getString("houseName");
        mUsers = (ArrayList<User>) intent.getSerializableExtra("users");
        mTasks = (ArrayList<Task>) intent.getSerializableExtra("tasks");

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        //change to household
        dbRef = db.getReference("Households/" + household + "/Supplies");

        ValueEventListener myDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSupplies = new ArrayList<>();
                Iterable<DataSnapshot> suppliesData = dataSnapshot.getChildren();
                for (DataSnapshot supply : suppliesData) {
                    HashMap<String, String> supplyMap = (HashMap<String, String>) supply.getValue();
                    Supply loadedSupply = new Supply(supplyMap.get("name"),
                            supplyMap.get("urgency"), supplyMap.get("price"));
                    mSupplies.add(loadedSupply);
                }
                setAdapterAndUpdateData();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("0", "cancelled");
            }
        };

        dbRef.addValueEventListener(myDataListener);
        setAdapterAndUpdateData();

        setButtonOnClicks();

    }

    private void setButtonOnClicks() {
        BottomNavigationItemView messageBoardLink = findViewById(R.id.chat);
        BottomNavigationItemView taskListLink = findViewById(R.id.tasks);
        BottomNavigationItemView profileLink = findViewById(R.id.profile);

        addSupplyButton = findViewById(R.id.add_supply_button);

        final Intent MessageBoardIntent = new Intent(this, MessageBoardActivity.class);
        final Intent TaskListLinkIntent = new Intent(this, TaskListActivity.class);
        final Intent AddSupplyIntent = new Intent(this, AddSupplyActivity.class);
        final Intent ProfileLinkIntent = new Intent(this, ProfileActivity.class);

        final Bundle extras = new Bundle();
        extras.putString("houseName",household);

        messageBoardLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBoardIntent.putExtras(extras);
                startActivity(MessageBoardIntent);
            }
        });

        profileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileLinkIntent.putExtras(extras);
                startActivity(ProfileLinkIntent);
            }
        });

        taskListLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskListLinkIntent.putExtras(extras);
                TaskListLinkIntent.putExtra("users", mUsers);
                TaskListLinkIntent.putExtra("tasks", mTasks);
                startActivity(TaskListLinkIntent);
            }
        });

        addSupplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSupplyIntent.putExtras(extras);
                AddSupplyIntent.putExtra("users", mUsers);
                AddSupplyIntent.putExtra("tasks", mTasks);
                startActivity(AddSupplyIntent);
            }
        });
    }

    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        mAdapter = new SupplyAdapter(this, mSupplies, household);
        mRecyclerView.setAdapter(mAdapter);
    }

}
