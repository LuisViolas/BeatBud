package com.example.beatbud_1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShowGroups extends AppCompatActivity {

    TextView show_festival,show_name,txt_details;
    String group_name,userEmail;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference groupsRef = db.collection("groups");
    private CollectionReference festivalRef = db.collection("festival");
    private boolean groupsEmpty = true;
    private String Doc ="";
    private String userName;
    private TextView groupsShow;
    private CollectionReference profileRef = db.collection("profile");
    RecyclerView rvShowGroups;
    private ArrayList<Groups> card_groups;
    private ViewPageAdapterPersons card_groups_adapter;
    ImageView add_btn,saved_btn,festival_image,img;
    ArrayList<String> savedGroups1;
    ArrayList<String> addedGroups;
    String festival_name;
    Boolean smoke,couple,after,drink;
    String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_show_groups);
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        data="";
        profileRef.whereEqualTo("email",userEmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Profile profile = document.toObject(Profile.class);
                    savedGroups1 = profile.getSavedGroups();
                    userName = profile.getName();
                }
                groupsRef.whereArrayContains("persons",userName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        addedGroups = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            addedGroups.add("tem");
                        }

                    }
                });
            }
        });
        txt_details = findViewById(R.id.txt_details);
        festival_image = findViewById(R.id.img_show_groups);
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        show_name = findViewById(R.id.show_groups_name);
        show_festival= findViewById(R.id.show_groups_festival);
        group_name = getIntent().getStringExtra("GROUP_NAME");
        saved_btn = findViewById(R.id.show_groups_saved);
        add_btn = findViewById(R.id.show_groups_add);
        img = findViewById(R.id.img_goups);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ShowGroups.this, Inicio.class);
                intent1.putExtra("USER_EMAIL",userEmail);
                startActivity(intent1);
            }
        });


        rvShowGroups = findViewById(R.id.rvShowGroups);

        rvShowGroups.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayout.HORIZONTAL,false);
        rvShowGroups.setLayoutManager(llm);

        profileRef
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Toast.makeText(Inicio.this,"DATAAA"+document.getData(),Toast.LENGTH_SHORT);
                                Log.d("ASAS", document.getId() + " => " + document.getData());
                                Doc = document.getId();
                                DocumentReference docRef = profileRef.document(Doc);
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Profile profile = documentSnapshot.toObject(Profile.class);
                                        userName = profile.getName();
                                    }
                                });
                            }
                        }
                    }
                                       });

        groupsRef
                .whereEqualTo("name",group_name)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        card_groups = new ArrayList<Groups>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Groups group = document.toObject(Groups.class);
                            drink = group.isDrink();
                            smoke = group.isSmoking();
                            couple = group.isCouple();
                            after = group.isCouple();
                            festival_name = group.getFestival();
                            show_festival.setText(group.getFestival());
                            show_name.setText(group.getName());
                            for(Integer i=0;i<group.getPersons().size();i++)
                            card_groups.add(new Groups(group.getPersons().get(i)));
                        }
                        data = "I'm ok with ";
                        if(drink)
                        {
                                data += "drinking, ";
                        }
                        if(smoke)
                        {
                            data +="smoke, ";
                        }
                        if(after)
                        {
                            data += "having after party,  ";
                        }
                        if(couple)
                        {
                            data += "couples";
                        }
                        Log.d("O CARALHO:_>", "ola"+card_groups.size());
                        txt_details.setText(data);
                        card_groups_adapter = new ViewPageAdapterPersons(ShowGroups.this,card_groups,userEmail);
                        rvShowGroups.setAdapter(card_groups_adapter);
                        festivalRef
                                .whereEqualTo("name",festival_name)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                Festival festival = document.toObject(Festival.class);
                                            Picasso.get()
                                                    .load(festival.getImageurl())
                                                    .into(festival_image);
                                        }
                                    }
                                });
                    }
                });
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupsRef
                        .document(group_name)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Groups groups = documentSnapshot.toObject(Groups.class);
                                ArrayList<String> mylist = groups.getPersons();

                                if(groups.getName_owner().equals(userName))
                                {
                                    Toast.makeText(ShowGroups.this, "You can´t leave your group", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Boolean equal = false;

                                    for (String addPerson : groups.getPersons()) {
                                        if (addPerson.equals(userName)) {
                                            equal = true;
                                        }
                                    }
                                    if (equal) {
                                        mylist.remove(userName);
                                        groupsRef.document(group_name).update("persons", mylist);
                                        Toast.makeText(ShowGroups.this, "REMOVED FROM GROUP", Toast.LENGTH_SHORT).show();
                                    } else if (mylist.size() == groups.getMax_people()) {
                                        Toast.makeText(ShowGroups.this, "GROUP ALREADY FULL", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mylist.add(userName);
                                        groupsRef.document(group_name).update("persons", mylist);
                                        Toast.makeText(ShowGroups.this, "ADD TO GROUP", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });

        saved_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileRef.document(Doc).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Profile profile = documentSnapshot.toObject(Profile.class);
                        ArrayList<String> mylist = profile.getSavedGroups();
                        Boolean equal = false;

                        for(String savedGroupName: profile.getSavedGroups())
                        {
                            if(savedGroupName.equals(group_name))
                            {
                                equal = true;
                            }
                        }
                        if(equal)
                        {
                            mylist.remove(group_name);
                            profileRef.document(Doc).update("savedGroups",mylist);
                            Toast.makeText(ShowGroups.this, "REMOVED FROM SAVED", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mylist.add(group_name);
                            profileRef.document(Doc).update("savedGroups",mylist);
                            Toast.makeText(ShowGroups.this, "ADD TO SAVED", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_explorer:
                        Intent intent = new Intent(ShowGroups.this, ExplorerActivity.class);
                        intent.putExtra("USER_EMAIL",userEmail);
                        startActivity(intent);

                        break;

                    case R.id.navigation_travels:
                        if(addedGroups.isEmpty())
                        {
                            Intent intent1 = new Intent(ShowGroups.this, EmptyGroups.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        else
                        {
                            Intent intent1 = new Intent(ShowGroups.this, TravelsActivity.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        break;

                    case R.id.navigation_saved:
                        if(savedGroups1.isEmpty())
                        {
                            Intent intent1 = new Intent(ShowGroups.this, EmptySaved.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        else
                        {
                            Intent intent1 = new Intent(ShowGroups.this, SavedActivity.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                            break;
                    case R.id.navigation_perfil:
                        Intent intent3 = new Intent(ShowGroups.this, ProfileActivity.class);
                        intent3.putExtra("USER_EMAIL",userEmail);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });



    }
}
