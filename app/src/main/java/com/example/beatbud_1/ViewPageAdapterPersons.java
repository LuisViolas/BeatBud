package com.example.beatbud_1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewPageAdapterPersons extends RecyclerView.Adapter<ViewPageAdapterPersons.ViewHolder> {

    private static Context context;
    public static ArrayList<Groups> myCardList;
    private static String userEmail;

    public ViewPageAdapterPersons(Context context,ArrayList<Groups> myCardList,String userEmail) {
        this.context = context;
        this.myCardList = myCardList;
        this.userEmail = userEmail;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtPersonName;

        public ViewHolder(View itemView) {
            super(itemView);
            txtPersonName = itemView.findViewById(R.id.card_person_name);
//            b.setOnClickListener(new View.OnClickListener() {
            //              @Override
            //            public void onClick(View v) {
            //              Log.d("mymusic", "Element " + getAdapterPosition() + " clicked.");
            //        }
            //  });
        }

    }
    @NonNull
    @Override
    public ViewPageAdapterPersons.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_persons, viewGroup, false);
        return new ViewPageAdapterPersons.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPageAdapterPersons.ViewHolder viewHolder, int i) {


            Groups item = myCardList.get(i);

            viewHolder.txtPersonName.setText(item.getName());

    }

    @Override
    public int getItemCount() {
        return myCardList.size();
    }


}

