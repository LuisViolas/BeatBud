package com.example.beatbud_1;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class TravelsActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference groupsRef = db.collection("groups");
    private boolean groupsEmpty = true;
    private String userEmail;
    private String Doc ="";
    private String userName;
    private TextView groupsShow;
    private CollectionReference profileRef = db.collection("profile");
    RecyclerView rvTravels;
    private ArrayList<Groups> card_groups;
    private ViewPageAdapter card_groups_adapter;
    ArrayList<String> savedGroups1;
    ArrayList<String> addedGroups;
    ImageView img;


    BottomSheetDialog bottomSheetDialog;

    TextView group_name, group_name1,textview;
    ImageView group_image;
    FloatingActionButton btn_fav;
    Context context;
    LayoutParams layoutparams;
    ConstraintLayout constraintLayout;
    Button btn;
  //  Groups groups;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_travels2);
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        BottomNavigationView navigation = findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);


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
     //   groupsShow = findViewById(R.id.groupsShow);

        img = findViewById(R.id.img_travels);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TravelsActivity.this, Inicio.class);
                intent.putExtra("USER_EMAIL",userEmail);
                startActivity(intent);
            }
        });

        btn= findViewById(R.id.btn_travels);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(TravelsActivity.this, CreateGroup.class);
                intent2.putExtra("USER_EMAIL",userEmail);
                startActivity(intent2);
            }
        });

        rvTravels = findViewById(R.id.rvTravels);

        rvTravels.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvTravels.setLayoutManager(llm);

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
                                    userName=profile.getName();
                                    Log.d("HELOOO","facil "+userName);

                                    groupsRef.whereArrayContains("persons",userName).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots)
                                                {
                                                    card_groups  = new ArrayList<Groups>();
                                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                        Groups group = document.toObject(Groups.class);

                                                        card_groups.add(new Groups(group.getName(),group.getPersons(),group.getName_owner()));
                                                    }
                                                    card_groups_adapter = new ViewPageAdapter(TravelsActivity.this,card_groups,userEmail);
                                                    rvTravels.setAdapter(card_groups_adapter);
                                                }
                                            });
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
                        Intent intent = new Intent(TravelsActivity.this, ExplorerActivity.class);
                        intent.putExtra("USER_EMAIL",userEmail);
                        startActivity(intent);

                        break;

                    case R.id.navigation_travels:


                        break;

                    case R.id.navigation_saved:
                        if(savedGroups1.isEmpty())
                        {
                            Intent intent1 = new Intent(TravelsActivity.this, EmptySaved.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        else
                        {
                            Intent intent1 = new Intent(TravelsActivity.this, SavedActivity.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        break;
                    case R.id.navigation_perfil:
                        Intent intent3 = new Intent(TravelsActivity.this, ProfileActivity.class);
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
    }
}
