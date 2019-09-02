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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class ExplorerActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference groupsRef = db.collection("groups");
    private boolean groupsEmpty = true;
    private String userEmail;
    private String Doc ="";
    private String userName;
    private CollectionReference profileRef = db.collection("profile");
    private Profile userProfile;
    private CollectionReference festivalRef = db.collection("festival");
    Spinner festivalsSpinner;
    RecyclerView rvExplorer;
    private ArrayList<Groups> card_groups;
    private ViewPageAdapter card_groups_adapter;

    ArrayList<String> savedGroups;
    ArrayList<String> addedGroups;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_explorer);
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        BottomNavigationView navigation = findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        img = findViewById(R.id.img_explorer);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExplorerActivity.this, Inicio.class);
                intent.putExtra("USER_EMAIL",userEmail);
                startActivity(intent);
            }
        });

        rvExplorer = findViewById(R.id.rvExplorer);

        rvExplorer.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvExplorer.setLayoutManager(llm);

        festivalsSpinner = findViewById(R.id.spinnerExplorer);

        festivalRef
                //.whereEqualTo("name", true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots)
                    {

                    }
                });

        profileRef.whereEqualTo("email",userEmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Profile profile = document.toObject(Profile.class);
                    savedGroups = profile.getSavedGroups();
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

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_explorer:


                       break;

                    case R.id.navigation_travels:
                        if(addedGroups.isEmpty())
                        {
                            Intent intent1 = new Intent(ExplorerActivity.this, EmptyGroups.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        else
                        {

                            Intent intent1 = new Intent(ExplorerActivity.this, TravelsActivity.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        break;

                    case R.id.navigation_saved:
                        if(savedGroups.isEmpty())
                    {
                        Intent intent1 = new Intent(ExplorerActivity.this, EmptySaved.class);
                        intent1.putExtra("USER_EMAIL",userEmail);
                        startActivity(intent1);
                    }
                    else
                    {
                        Intent intent1 = new Intent(ExplorerActivity.this, SavedActivity.class);
                        intent1.putExtra("USER_EMAIL",userEmail);
                        startActivity(intent1);
                    }

                        break;
                    case R.id.navigation_perfil:
                        Intent intent3 = new Intent(ExplorerActivity.this, ProfileActivity.class);
                        intent3.putExtra("USER_EMAIL",userEmail);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        festivalRef.addSnapshotListener(this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<String> festivalsList = new ArrayList<>();

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Festival festival = document.toObject(Festival.class);
                    festivalsList.add(festival.getName());
                    Log.d("NOME__>", "_>" + festival.getName());
                }

                ArrayAdapter<String> arrayAdapter =  new ArrayAdapter<String>(ExplorerActivity.this,android.R.layout.simple_dropdown_item_1line,festivalsList);
                festivalsSpinner.setAdapter(arrayAdapter);

            }
        });
        festivalsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                groupsRef
                        .whereEqualTo("festival", parentView.getItemAtPosition(position).toString())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshot) {
                                card_groups  = new ArrayList<Groups>();
                                for (QueryDocumentSnapshot document : documentSnapshot) {
                                    Groups group = document.toObject(Groups.class);

                                    card_groups.add(new Groups(group.getName(),group.getPersons(),group.getName_owner()));

                                }
                                card_groups_adapter = new ViewPageAdapter(ExplorerActivity.this,card_groups,userEmail);
                                rvExplorer.setAdapter(card_groups_adapter);
                            }
                        });
            }
            @Override
                public void onNothingSelected (AdapterView < ? > parentView){
                    // your code here
                }

        });

        groupsRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

            }
        });
    }
}
