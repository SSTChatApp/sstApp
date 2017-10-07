package com.example.mine.mychatapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;



public class AllSectionsAdapter extends RecyclerView.Adapter<AllSectionsAdapter.MyViewHolder> {
    private ArrayList<Section> sections;
    private Context context;
    public AllSectionsAdapter( Context context,ArrayList<Section>sections ) {
        this.sections = sections;
        this.context=context;
    }

    @Override
    public AllSectionsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.one_section,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Section CurrentSection=sections.get(position);
        holder.SectionButton.setText(CurrentSection.name);
        holder.SectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,AllUsersActivity.class);
                i.putExtra("ID",CurrentSection.ID);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {

        return sections.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        Button SectionButton;
        public MyViewHolder(View itemView) {
            super(itemView);
            SectionButton=itemView.findViewById(R.id.SectionButton);


        }
    }
}