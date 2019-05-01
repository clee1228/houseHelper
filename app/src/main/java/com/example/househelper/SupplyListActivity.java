package com.example.househelper;

import android.content.Intent;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SupplyListActivity extends AppCompatActivity {

    Button addSupplyButton;
    private ArrayList<Supply> mSupplies;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    String household;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_list);


        Intent intent = getIntent();
        Bundle getExtras = intent.getExtras();
        household = getExtras.getString("houseName");


        mSupplies = new ArrayList<>();

        setButtonOnClicks();

        mRecyclerView = (RecyclerView) findViewById(R.id.supply_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setAdapterAndUpdateData();

    }

    private void setButtonOnClicks() {
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

    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        createSupplies();
        mAdapter = new SupplyAdapter(this, this.mSupplies);
        mRecyclerView.setAdapter(mAdapter);
    }

    // DUMMY DATA, DELETE LATER
    private void createSupplies() {
        this.mSupplies.add(new Supply("toilet paper", "high"));
        this.mSupplies.add(new Supply("dish liquid", "medium"));
    }
}
