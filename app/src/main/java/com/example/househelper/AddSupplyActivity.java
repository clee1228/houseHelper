package com.example.househelper;

import android.content.Intent;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.UUID;

public class AddSupplyActivity extends AppCompatActivity {

    Button submitButton;
    private DatabaseReference dbRef;
    String username, household;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supply);

        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        household = extras.getString("houseName");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        //change to household
        dbRef = db.getReference("Households/" + household + "/Supplies");

        // Urgency spinner stuff.
        Spinner spinner = findViewById(R.id.supply_urgency);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.urgency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Intent MessageBoardIntent = new Intent(this, MessageBoardActivity.class);
        final Intent TaskListLinkIntent = new Intent(this, TaskListActivity.class);
        final Intent SupplyListIntent = new Intent(this, SupplyListActivity.class);
        final Intent ProfileLinkIntent = new Intent(this, ProfileActivity.class);

        BottomNavigationItemView messageBoardLink = findViewById(R.id.chat);
        BottomNavigationItemView taskListLink = findViewById(R.id.tasks);
        BottomNavigationItemView supplyListLink = findViewById(R.id.shopList);
        BottomNavigationItemView profileLink = findViewById(R.id.profile);


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
                SupplyListIntent.putExtras(extras);
                startActivity(SupplyListIntent);
            }
        });
        profileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileLinkIntent.putExtras(extras);
                startActivity(ProfileLinkIntent);
            }
        });


        submitButton = findViewById(R.id.submit_supply_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSupply();
            }
        });

        final EditText supplyNameEditText = findViewById(R.id.supply_name);
        final EditText supplyPriceEditText = findViewById(R.id.supply_price);
        supplyPriceEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supplyPriceEditText.setText("");
            }
        });
        supplyNameEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supplyNameEditText.setText("");
            }
        });


    }

    public void submitSupply() {
        EditText supplyNameField = findViewById(R.id.supply_name);
        Spinner urgencySpinner = findViewById(R.id.supply_urgency);
        EditText supplyPriceField = findViewById(R.id.supply_price);

        String supplyName = supplyNameField.getText().toString();
        String urgency = urgencySpinner.getSelectedItem().toString();
        double supplyPrice = Double.parseDouble(supplyPriceField.getText().toString());

        DatabaseReference dbSupply = dbRef.child(supplyName);
        dbSupply.child("name").setValue(supplyName);
        dbSupply.child("urgency").setValue(urgency);
        dbSupply.child("price").setValue(supplyPrice);

        final Intent goBackToSupplies = new Intent(this, SupplyListActivity.class);
        final Bundle extras = new Bundle();
        extras.putString("houseName",household);
        goBackToSupplies.putExtras(extras);
        startActivity(goBackToSupplies);
    }


}
