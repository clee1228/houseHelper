package com.example.househelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Message> mChats;

    public ChatAdapter(Context context, ArrayList<Message> chats) {
        mContext = context;
        mChats = chats;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rcv_msgs, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // here, we the comment that should be displayed at index `position` in our recylcer view
        // everytime the recycler view is refreshed, this method is called getItemCount() times (because
        // it needs to recreate every cell).
        Message msg = mChats.get(position);
        ((ChatViewHolder) holder).bind(msg);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mChats.size();
    }
}

class ChatViewHolder extends RecyclerView.ViewHolder {

    // each data item is just a string in this case
    public RelativeLayout mCommentBubbleLayout;
    public TextView mUsernameTextView;
    public TextView mDateTextView;
    public TextView mMsgTextView;

    public ChatViewHolder(View itemView) {
        super(itemView);
        mCommentBubbleLayout = itemView.findViewById(R.id.rcv_msgs);
        mUsernameTextView = mCommentBubbleLayout.findViewById(R.id.username);
//        mDateTextView = mCommentBubbleLayout.findViewById(R.id.date_text_view);
        mMsgTextView = mCommentBubbleLayout.findViewById(R.id.message_body);
    }

    void bind(Message msg) {
        mUsernameTextView.setText(msg.user);
//        mDateTextView.setText("posted " + msg.elapsedTimeString() + " ago");
        mMsgTextView.setText(msg.text);
    }
}


