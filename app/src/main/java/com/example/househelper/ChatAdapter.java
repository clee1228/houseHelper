package com.example.househelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Message> mChats;

    public ChatAdapter(Context context, ArrayList<Message> chats) {
        mContext = context;
        mChats = chats;
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(mChats.get(position).getUsername() == user.getDisplayName()) {
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
            /* received chats */
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // here, we the comment that should be displayed at index `position` in our recylcer view
        // everytime the recycler view is refreshed, this method is called getItemCount() times (because
        // it needs to recreate every cell).

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        Message msg = mChats.get(position);

        if(msg.getUsername() == currUser.getDisplayName()) {
            ((SentChatViewHolder) holder).bindSentMsg(msg);

        } else {
            ((ChatViewHolder) holder).bind(msg);
        }

//        Message msg = mChats.get(position);
//        ((ChatViewHolder) holder).bind(msg);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mChats.size();
    }
}

class ChatViewHolder extends RecyclerView.ViewHolder {

    // each data item is just a string in this case
    public RelativeLayout receivedBubbleLayout;
    public TextView mUsernameTextView;
    public TextView mDateTextView;
    public TextView mMsgTextView;

    public ChatViewHolder(View itemView) {
        super(itemView);
        receivedBubbleLayout = itemView.findViewById(R.id.rcv_msgs);
        mUsernameTextView = receivedBubbleLayout.findViewById(R.id.firstName);
//        mDateTextView = mCommentBubbleLayout.findViewById(R.id.date_text_view);
        mMsgTextView = receivedBubbleLayout.findViewById(R.id.msg_body);
    }

    void bind(Message msg) {
        mUsernameTextView.setText(msg.user);
//        mDateTextView.setText("posted " + msg.elapsedTimeString() + " ago");
        mMsgTextView.setText(msg.text);
    }
}

    class SentChatViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public RelativeLayout sentBubbleLayout;
//        public TextView mDateTextView;
        public TextView myMsgTextView;

        public SentChatViewHolder(View itemView) {
            super(itemView);
            sentBubbleLayout = itemView.findViewById(R.id.my_msgs);
//        mDateTextView = mCommentBubbleLayout.findViewById(R.id.date_text_view);
            myMsgTextView = sentBubbleLayout.findViewById(R.id.msg_body);
        }

        void bindSentMsg(Message msg) {
//        mDateTextView.setText("posted " + msg.elapsedTimeString() + " ago");
            myMsgTextView.setText(msg.text);
        }
    }



