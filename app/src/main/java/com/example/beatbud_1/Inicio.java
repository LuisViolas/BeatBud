package com.example.beatbud_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class Inicio extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference groupsRef = db.collection("groups");

    private String userEmail;
    private String Doc ="";
    private String userName;
    private CollectionReference profileRef = db.collection("profile");
    private CollectionReference festivalRef = db.collection("festival");

    Spinner festivalsSpinner;
    String festivalSelected;
    TextView max_people;
    Switch sw_drink, sw_smoke,sw_couple,sw_after;
    Boolean drink,couple,smoke,after;
    Button btn;
    ArrayList<Groups> cards;
    ArrayList<String> savedGroups;
    ArrayList<String> addedGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_inicio);
        userEmail = getIntent().getStringExtra("USER_EMAIL");




        festivalsSpinner = findViewById(R.id.festivals_spinner_inicio);
        sw_after = findViewById(R.id.sw_after_inicio);
        sw_couple = findViewById(R.id.sw_couple_inicio);
        sw_drink = findViewById(R.id.sw_drink_inicio);
        sw_smoke = findViewById(R.id.sw_smoke_inicio);
        btn = findViewById(R.id.btn_inicio);
        max_people = findViewById(R.id.let_in_inicio);

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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw_after.isChecked())
                {
                    after = true;
                }
                else
                {
                    after = false;
                }
                if(sw_couple.isChecked())
                {
                    couple = true;
                }
                else
                {
                    couple = false;
                }
                if(sw_drink.isChecked())
                {
                    drink = true;
                }
                else
                {
                    drink = false;
                }
                if(sw_smoke.isChecked())
                {
                    smoke = true;
                }
                else
                {
                    smoke = false;
                }
                Integer max = Integer.parseInt(max_people.getText().toString());
                if(smoke && drink &&couple && after)
                {
                    groupsRef
                            .whereEqualTo("festival",festivalSelected)
                            .whereLessThan("max_pleople",max)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    cards = new ArrayList<Groups>();
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        Groups group = document.toObject(Groups.class);
                                        cards.add(new Groups(group.getName(),group.getPersons(),group.getName_owner()));
                                    }
                                    Intent intent = new Intent(Inicio.this,ResultsActivity.class);
                                    intent.putParcelableArrayListExtra("ARRAY",cards);
                                    intent.putExtra("USER_EMAIL",userEmail);
                                    startActivity(intent);
                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Inicio.this, "Couldn´t find any group!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {


                    groupsRef
                            .whereEqualTo("festival", festivalSelected)
                            .whereEqualTo("drink", drink)
                            .whereEqualTo("after", after)
                            .whereEqualTo("smoke", smoke)
                            .whereEqualTo("couple", couple)
                            .whereLessThan("max_pleople", max)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    cards = new ArrayList<Groups>();
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        Groups group = document.toObject(Groups.class);
                                        cards.add(new Groups(group.getName(), group.getPersons(), group.getName_owner()));
                                    }
                                    Intent intent = new Intent(Inicio.this, ResultsActivity.class);
                                    intent.putParcelableArrayListExtra("ARRAY", cards);
                                    intent.putExtra("USER_EMAIL", userEmail);
                                    startActivity(intent);
                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Inicio.this, "Couldn´t find any group!", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_explorer:
                        Intent intent = new Intent(Inicio.this, ExplorerActivity.class);
                        intent.putExtra("USER_EMAIL",userEmail);
                        startActivity(intent);
                        break;

                    case R.id.navigation_travels:
                        if(addedGroups.isEmpty())
                        {
                            Intent intent1 = new Intent(Inicio.this, EmptyGroups.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        else
                        {
                            Intent intent1 = new Intent(Inicio.this, TravelsActivity.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        break;

                    case R.id.navigation_saved:
                        if(savedGroups.isEmpty())
                        {
                            Intent intent1 = new Intent(Inicio.this, EmptySaved.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        else
                        {
                            Intent intent1 = new Intent(Inicio.this, SavedActivity.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }

                    case R.id.navigation_perfil:
                        Intent intent3 = new Intent(Inicio.this, ProfileActivity.class);
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
        festivalRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<String> festivalsList = new ArrayList<>();

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Festival festival = document.toObject(Festival.class);
                    festivalsList.add(festival.getName());
                    Log.d("NOME__>", "_>" + festival.getName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Inicio.this, android.R.layout.simple_dropdown_item_1line, festivalsList);
                festivalsSpinner.setAdapter(arrayAdapter);

            }
        });
        festivalsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                festivalSelected = parentView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }
}
