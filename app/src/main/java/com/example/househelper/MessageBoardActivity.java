package com.example.househelper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class MessageBoardActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Message> msgList = new ArrayList<Message>();
    public HashMap<String, String> messages;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    RelativeLayout layout;
    EditText message;
    Button sendButton;
    Toolbar mToolbar;
    String household;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);

        setTitle("Group Chat");
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        household = extras.getString("houseName");

        setPageUp();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setOnClickForSendButton();

        msgList = new ArrayList<Message>();


        final DatabaseReference chats = database.getReference("Households").child(household).child("Chats");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                msgList = new ArrayList<Message>();
                messages = (HashMap<String, String>) dataSnapshot.getValue();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String s = ds.getKey();
                    Date date = getDate(s);
                    String message = ds.child("message").getValue(String.class);
                    String user = ds.child("user").getValue(String.class);
                    String time = ds.child("time").getValue(String.class);
                    Message newMsg = new Message(message, user, date, time);

                    msgList.add(newMsg);
                }

                setAdapterAndUpdateData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        chats.addValueEventListener(listener);
        setAdapterAndUpdateData();
    }

    private void setPageUp(){
        layout = (RelativeLayout) findViewById(R.id.chat_layout);
        message = (EditText) layout.findViewById(R.id.input_msg);
        sendButton = (Button) layout.findViewById(R.id.send_button);

        mToolbar = (Toolbar) findViewById(R.id.msgToolbar);
        setSupportActionBar(mToolbar);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        mRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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

    public Date getDate(String s){
        Date date = null;
        try {
            date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(s);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;

    }


    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        mAdapter = new ChatAdapter(this, msgList);
        mRecyclerView.setAdapter(mAdapter);

        // scroll to the last comment
        if (msgList.size() == 0) {
            mRecyclerView.smoothScrollToPosition(0);
        } else {
            mRecyclerView.smoothScrollToPosition(msgList.size() - 1);
        }

    }

    private void postNewComment(String msgInput) {

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

        Calendar now = Calendar.getInstance();
        String timeStamp = (now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE));

        Message newMsg = new Message(msgInput, currUser.getDisplayName(), new Date(), timeStamp);
        msgList.add(newMsg);

        DatabaseReference chats  = database.getReference("Households").child(household).child("Chats");
        String time = String.valueOf(new Date());
        DatabaseReference chat = chats.child(time);
        chat.child("user").setValue(currUser.getDisplayName());
        chat.child("message").setValue(msgInput);
        chat.child("time").setValue(timeStamp);
        setAdapterAndUpdateData();
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}
