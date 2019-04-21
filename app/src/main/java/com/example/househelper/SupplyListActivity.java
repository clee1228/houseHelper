package com.example.househelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SupplyListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_list);
        ImageView messageBoardLink = findViewById(R.id.message_board_link);

        final Intent MessageBoardIntent = new Intent(this, MessageBoardActivity.class);

        messageBoardLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(MessageBoardIntent);
            }
        });
    }
}
