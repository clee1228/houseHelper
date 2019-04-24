package com.example.househelper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;


import java.util.ArrayList;
import java.util.Date;

public class MessageBoardActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Message> msgList = new ArrayList<Message>();
    RelativeLayout layout;
    EditText message;
    Button sendButton;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);

        setTitle("Group Chat");

        layout = (RelativeLayout) findViewById(R.id.chat_layout);
        message = (EditText) layout.findViewById(R.id.input_msg);
        sendButton = (Button) layout.findViewById(R.id.send_button);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setOnClickForSendButton();
        makeTestMsgs();
        setAdapterAndUpdateData();


    }


    private void makeTestMsgs() {
        String randomString = "hello everyone ";
        Message newMsg = new Message(randomString, "test_user1", new Date());
        Message hourAgoMsg = new Message("hey", "test_user2", new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        Message overHourMsg = new Message("whats up", "test_user3", new Date(System.currentTimeMillis() - (2 * 60 * 60 * 1000)));
        msgList.add(newMsg);msgList.add(hourAgoMsg); msgList.add(overHourMsg);
    }

    private void setOnClickForSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = message.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    // don't do anything if nothing was added
                    message.requestFocus();
                } else {
                    // clear edit text, post comment
                    message.setText("");
                    postNewComment(comment);
                }
            }
        });
    }

    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        mAdapter = new ChatAdapter(this, msgList);
        mRecyclerView.setAdapter(mAdapter);

        // scroll to the last comment
        mRecyclerView.smoothScrollToPosition(msgList.size() - 1);
    }

    private void postNewComment(String commentText) {
        Message newMsg = new Message(commentText, "one-sixty student", new Date());
        msgList.add(newMsg);
        setAdapterAndUpdateData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}
