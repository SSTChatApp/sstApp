package com.example.mine.mychatapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<Employee> employees;
    private Context context;
    public CustomRecyclerViewAdapter( Context context,ArrayList<Employee> employees) {
        this.employees = employees;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.one_user_in_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final String name=employees.get(position).name;
        final String UID=employees.get(position).nationalID;
        holder.name.setText(name);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Welcome to Mr "+ name,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context,ChatActivity.class);
                intent.putExtra("UID",UID);
                intent.putExtra("UName",name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,department,section;
        View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Name);
            view=itemView;

        }
    }
}
