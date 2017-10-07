package com.example.mine.mychatapplication;

/**
 * this is class adapter to our messages it contains members of friendlyMessage class variable as text for message and text for name and image url
 * this adapter handle with recyclerView
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;



public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView ;
        public TextView messageTextView ;
        public TextView sendTimeTextView ;
        public ImageView photoImageView ;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView =  itemView.findViewById(R.id.nameTextView);
            messageTextView =  itemView.findViewById(R.id.messageTextView);
            sendTimeTextView=  itemView.findViewById(R.id.sendTimeTextView);
            photoImageView =  itemView.findViewById(R.id.photoImageView) ;
        }
    }

    // Store a member variable for the Messages
    private List<ChatMessage> mMessages;
    // Store the messageText for easy access
    private Context mContext;

    // Pass in the contact array into the MessageAdapter
    public MessageAdapter(Context context, List<ChatMessage> Messages) {
        mMessages = Messages;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context  getMessagetext() {
        return mContext;
    }


    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_message, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        ChatMessage friendlyMessage = mMessages.get(position);

        // Set item views based on your views and data model
        viewHolder.nameTextView.setText(friendlyMessage.writer);
        viewHolder.messageTextView.setText(friendlyMessage.body);
        viewHolder.sendTimeTextView.setText(friendlyMessage.sendTime);

        ImageView photoImageView = viewHolder.photoImageView;

        // this line make app crash
        //photoImageView.setImageURI(Uri.parse(friendlyMessage.getPhotoUrl()));
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mMessages.size();
    }
}