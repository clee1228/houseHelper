package com.example.househelper;

import android.content.Intent;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


    ImageView goBackButton;
    Button submitButton;
    private DatabaseReference dbRef;
    String username, household;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supply);

        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        household = extras.getString("houseName");

        FirebaseDatabase db = FirebaseDatabase.getInstance();
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

//        final Bundle newExtras = new Bundle();
//        newExtras.putString("houseName",household);

        BottomNavigationItemView messageBoardLink = findViewById(R.id.chat);
        BottomNavigationItemView taskListLink = findViewById(R.id.tasks);
        BottomNavigationItemView supplyListLink = findViewById(R.id.shopList);


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


        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        goBackButton = findViewById(R.id.back_button);
        submitButton = findViewById(R.id.submit_supply_button);

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle extras = new Bundle();
                extras.putString("houseName",household);
                SupplyListIntent.putExtras(extras);
                startActivity(SupplyListIntent);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSupply();
            }
        });

        final EditText supplyNameEditText = findViewById(R.id.supply_name);
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

        String supplyName = supplyNameField.getText().toString();
        String urgency = urgencySpinner.getSelectedItem().toString();

        Supply toAdd = new Supply(supplyName, urgency);

        DatabaseReference dbSupply = dbRef.child(supplyName);
        dbSupply.child("name").setValue(supplyName);
        dbSupply.child("urgency").setValue(urgency);

        final Intent goBackToSupplies = new Intent(this, SupplyListActivity.class);
        final Bundle extras = new Bundle();
        extras.putString("houseName",household);
        goBackToSupplies.putExtras(extras);
        startActivity(goBackToSupplies);
    }


}
