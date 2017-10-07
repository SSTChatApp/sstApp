package com.example.mine.mychatapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;



class ListOfCommentsAdapter extends BaseAdapter {
    private Context context;
    private List<Comment> comments;

    public ListOfCommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {

        return comments.size();
    }

    @Override
    public Object getItem(int i)
    {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.commnt_list_item,null);
        TextView Name= view.findViewById(R.id.CommentUserName);
        TextView Value= view.findViewById(R.id.CommentValue);
        TextView Date= view.findViewById(R.id.CommentDate);
        Name.setText(comments.get(i).user);
        Value.setText(comments.get(i).value);
        Date.setText(comments.get(i).date);
        return view;
    }
}
