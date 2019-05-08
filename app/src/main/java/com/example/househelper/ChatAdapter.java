package com.example.househelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class  ChatAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Message> mChats;

    public ChatAdapter(Context context, ArrayList<Message> chats) {
        mContext = context;
        mChats = chats;
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(mChats.get(position).getUsername().equals(user.getDisplayName())) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.my_msg, parent, false);
            return new SentChatViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.rcv_msgs, parent, false);
            return new ChatViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // every time recycler view is refreshed, this method is called getItemCount() times
        // to recreate every cell

        Message msg = mChats.get(position);
        switch((holder.getItemViewType())){
            case 1:  SentChatViewHolder sent = (SentChatViewHolder) holder;
                     sent.bindSentMsg(msg);
                     break;
            case 2:  ((ChatViewHolder) holder).bind(msg);
                     break;
        }

    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }
}

class ChatViewHolder extends RecyclerView.ViewHolder {

    // each data item is just a string in this case
    public RelativeLayout receivedBubbleLayout;
    public TextView mUsernameTextView;
    public TextView rcvTimeView;
    public TextView mMsgTextView;

    public ChatViewHolder(View itemView) {
        super(itemView);
        receivedBubbleLayout = itemView.findViewById(R.id.rcv_msgs);
        mUsernameTextView = receivedBubbleLayout.findViewById(R.id.firstName);
        rcvTimeView = receivedBubbleLayout.findViewById(R.id.rcvTime);
        mMsgTextView = receivedBubbleLayout.findViewById(R.id.msg_body);
    }

    void bind(Message msg) {
        mUsernameTextView.setText(msg.user);
        rcvTimeView.setText(msg.time);
        mMsgTextView.setText(msg.text);
    }
}

    class SentChatViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public RelativeLayout sentBubbleLayout;
        public TextView sentTimeView;
        public TextView myMsgTextView;

        public SentChatViewHolder(View itemView) {
            super(itemView);
            sentBubbleLayout = itemView.findViewById(R.id.my_msgs);
            sentTimeView = sentBubbleLayout.findViewById(R.id.sentTime);
            myMsgTextView = sentBubbleLayout.findViewById(R.id.msg_body);
        }

        void bindSentMsg(Message msg) {
            myMsgTextView.setText(msg.text);
            sentTimeView.setText(msg.time);
        }
    }



