package com.example.beatbud_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.style.LineBackgroundSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;


public class ViewPageAdapter extends RecyclerView.Adapter<ViewPageAdapter.ViewHolder> {

    private static Context context;
    public static ArrayList<Groups> myCardList;
    private static String userEmail;

    public ViewPageAdapter(Context context,ArrayList<Groups> myCardList,String userEmail) {
        this.context = context;
        this.myCardList = myCardList;
        this.userEmail = userEmail;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtNameGroup, txtPersons,txtNameOwner;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNameOwner = itemView.findViewById(R.id.card_name_owner);
            txtNameGroup = itemView.findViewById(R.id.card_name);
            txtPersons= itemView.findViewById(R.id.card_persons);

//            b.setOnClickListener(new View.OnClickListener() {
  //              @Override
    //            public void onClick(View v) {
      //              Log.d("mymusic", "Element " + getAdapterPosition() + " clicked.");
        //        }
          //  });
            Button btn_card = itemView.findViewById(R.id.btn_card);
            btn_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Groups item = myCardList.get(getAdapterPosition());
                    Intent intent = new Intent(context,ShowGroups.class);
                    intent.putExtra("USER_EMAIL",userEmail);
                    intent.putExtra("GROUP_NAME",item.getName());
                    context.startActivity(intent);
                }
            });

        }

    }
    @NonNull
    @Override
    public ViewPageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewPageAdapter.ViewHolder viewHolder, int i) {
        Groups  item = myCardList.get(i);
        viewHolder.txtNameOwner.setText(item.getName_owner());
        viewHolder.txtNameGroup.setText(item.getName());
        Integer pp = item.getPersons().size();
        viewHolder.txtPersons.setText("O Grupo tem "+pp+ " pessoas");


    }

    @Override
    public int getItemCount() {
        return myCardList.size();
    }


}

