package com.example.beatbud_1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {
    ArrayList<String> savedGroups;
    ArrayList<String> addedGroups;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference groupsRef = db.collection("groups");
    private boolean groupsEmpty = true;
    private String userEmail;
    private String Doc ="";
    private String userName;
    private CollectionReference profileRef = db.collection("profile");
    private CollectionReference festivalRef = db.collection("festival");
    Spinner festivalsSpinner;
    IFirebaseLoadDone iFirebaseLoadDone;
    ArrayList<Groups> results;
    TextView txt_results;
    private ArrayList<Groups> card_groups;
    private ViewPageAdapter card_groups_adapter;
    RecyclerView rvResults;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        userEmail = getIntent().getStringExtra("USER_EMAIL");

        txt_results = findViewById(R.id.txt_results);
        rvResults = findViewById(R.id.rvResults);

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

        img = findViewById(R.id.img_goups);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, Inicio.class);
                intent.putExtra("USER_EMAIL",userEmail);
                startActivity(intent);
            }
        });

        results = getIntent().getParcelableArrayListExtra("ARRAY");

            txt_results.setText("Your results");
            card_groups_adapter = new ViewPageAdapter(ResultsActivity.this,results,userEmail);
            rvResults.setAdapter(card_groups_adapter);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_explorer:
                        Intent intent = new Intent(ResultsActivity.this, ExplorerActivity.class);
                        intent.putExtra("USER_EMAIL",userEmail);
                        startActivity(intent);

                        break;

                    case R.id.navigation_travels:
                        if(addedGroups.isEmpty())
                        {
                            Intent intent1 = new Intent(ResultsActivity.this, EmptyGroups.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        else
                        {
                            Intent intent1 = new Intent(ResultsActivity.this, TravelsActivity.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        break;

                    case R.id.navigation_saved:
                        if(savedGroups.isEmpty())
                        {
                            Intent intent1 = new Intent(ResultsActivity.this, EmptySaved.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        else
                        {
                            Intent intent1 = new Intent(ResultsActivity.this, SavedActivity.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        break;

                    case R.id.navigation_perfil:
                        Intent intent3 = new Intent(ResultsActivity.this, ProfileActivity.class);
                        intent3.putExtra("USER_EMAIL",userEmail);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });
    }
}
