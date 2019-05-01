package com.example.househelper;

import android.content.Intent;
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

public class AddSupplyActivity extends AppCompatActivity {


    ImageView goBackButton;
    Button submitButton;
    EditText inputted_supply;
    Spinner inputted_urgency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supply);

        Spinner spinner = findViewById(R.id.supply_urgency);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.urgency_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        goBackButton = findViewById(R.id.back_button);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent SupplyListIntent = new Intent(v.getContext(), SupplyListActivity.class);
                startActivity(SupplyListIntent);
            }
        });

        submitButton = findViewById(R.id.submit_supply_button);
        inputted_supply = findViewById(R.id.supply_name);
        inputted_urgency = findViewById(R.id.supply_urgency);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent SupplyListIntent = new Intent(v.getContext(), SupplyListActivity.class);
                SupplyListIntent.putExtra("supply_name", inputted_supply.toString());
                SupplyListIntent.putExtra("urgency", inputted_urgency.toString());
                startActivity(SupplyListIntent);
            }
        });

    }
}
