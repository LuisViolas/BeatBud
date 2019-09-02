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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SavedActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference groupsRef = db.collection("groups");
    private boolean groupsEmpty = true;
    private String userEmail;
    private String Doc ="";
    private String userName;
    private CollectionReference profileRef = db.collection("profile");
    private Profile userProfile;
    private List<String> savedGroups;
    private TextView saved_groups;
    RecyclerView rvSaved;
    private ArrayList<Groups> card_groups;
    private ViewPageAdapter card_groups_adapter;

    ArrayList<String> savedGroups1;
    ArrayList<String> addedGroups;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_saved);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        rvSaved = findViewById(R.id.rvSaved);

        rvSaved.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvSaved.setLayoutManager(llm);

        img = findViewById(R.id.img_goups);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SavedActivity.this, Inicio.class);
                intent.putExtra("USER_EMAIL",userEmail);
                startActivity(intent);
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

        profileRef
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                // Toast.makeText(Inicio.this,"DATAAA"+document.getData(),Toast.LENGTH_SHORT);
                                Log.d("ASAS", document.getId() + " => " + document.getData());
                                Doc = document.getId();
                            }
                            DocumentReference docRef = profileRef.document(Doc);
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                            {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot)
                                {
                                    Profile profile = documentSnapshot.toObject(Profile.class);
                                    for(String group_name: profile.getSavedGroups())
                                    {
                                        groupsRef
                                                .whereEqualTo("name",group_name)
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        card_groups = new ArrayList<Groups>();
                                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                            Groups group = document.toObject(Groups.class);
                                                            card_groups.add(new Groups(group.getName(),group.getPersons(),group.getName_owner()));
                                                        }
                                                        card_groups_adapter = new ViewPageAdapter(SavedActivity.this,card_groups,userEmail);
                                                        rvSaved.setAdapter(card_groups_adapter);
                                                    }
                                                });

                                    }

                                }
                            });
                        } else {
                            Log.d("jesusu", "Error getting documents: ", task.getException());
                        }
                    }});
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_explorer:
                        Intent intent = new Intent(SavedActivity.this, ExplorerActivity.class);
                        intent.putExtra("USER_EMAIL",userEmail);
                        startActivity(intent);

                        break;

                    case R.id.navigation_travels:
                        if(addedGroups.isEmpty())
                        {
                            Intent intent1 = new Intent(SavedActivity.this, EmptyGroups.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        else
                        {
                            Intent intent1 = new Intent(SavedActivity.this, TravelsActivity.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        break;

                    case R.id.navigation_saved:
                        if(savedGroups.isEmpty())
                        {
                            Intent intent1 = new Intent(SavedActivity.this, EmptySaved.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        else
                        {
                            Intent intent1 = new Intent(SavedActivity.this, SavedActivity.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        break;

                    case R.id.navigation_perfil:
                        Intent intent3 = new Intent(SavedActivity.this, ProfileActivity.class);
                        intent3.putExtra("USER_EMAIL",userEmail);
                        startActivity(intent3);
                        break;
                }
                return false;

            }
        });
    }


}
