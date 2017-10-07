package com.example.mine.mychatapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;



class UploadListCustomAdapter extends BaseAdapter{
    private ArrayList<String> items;
    private Context context;
    public UploadListCustomAdapter(Context context) {
        this.context=context;
        this.items=new ArrayList<>((Arrays.asList(context.getResources().getStringArray(R.array.UpLoadItems))));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public String getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root=inflater.inflate(R.layout.upload_item,viewGroup,false);
        TextView itemName=root.findViewById(R.id.UploadItem_TextView);
        //TextView failed=root.findViewById(R.id.failed_TextView);
        //TextView succeeded=root.findViewById(R.id.succeeded_TextView);
        //ProgressBar pb=root.findViewById(R.id.upload_progressBar);
        itemName.setText(items.get(i));
        return root;
    }
}