package com.example.mine.mychatapplication;

/**
 *
 * this is class adapter to our messages it contains members of friendlyMessage class variable as text for message and text for name and image url
 * this adapter handle with recyclerView
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;



public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView ;
        public TextView textTextView ;
        public  Button commentButton ;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView =  itemView.findViewById(R.id.nameTextView);
            textTextView =  itemView.findViewById(R.id.textextView);
            commentButton = itemView.findViewById(R.id.commentButton) ;
        }
    }

    // Store a member variable for the posts
    private List<Post> mPosts;
    // Store the postText view for easy access
    private Context mContext;

    // Pass in the contact array into the PostAdapter
    public PostAdapter(Context context, List<Post> posts) {
        mPosts = posts;
        mContext = context;
    }

    // Easy access to the context object in the recyclerView
    private Context  getPostText() {
        return mContext;
    }


    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.post_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Post post = mPosts.get(position);

        // Set item views based on your views and data model
        TextView nameTextView = viewHolder.nameTextView;
        nameTextView.setText(post.name);

        TextView textTextView = viewHolder.textTextView;
        textTextView.setText(post.value);
        Button commentButton = viewHolder.commentButton ;

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(mContext,CommentAct.class);
                i.putExtra("ID",post.id);
                mContext.startActivity(i);

            }
        });
    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mPosts.size();
    }


}