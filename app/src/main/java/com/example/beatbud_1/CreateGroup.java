package com.example.beatbud_1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.List;

import javax.annotation.Nullable;

public class CreateGroup extends AppCompatActivity {

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

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private DatePickerDialog.OnDateSetListener dateSetListener1;

    TextView date_in,date_out,name,let_in;
    Switch sw_drink,sw_smoke,sw_couple,sw_after;
    Button btn;
    Boolean drink,smoke,couple,after;
    String festival_name;
    ArrayList<String> savedGroups;
    ArrayList<String> addedGroups;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_group);
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        festivalsSpinner = findViewById(R.id.explorer_spinner);

        img = findViewById(R.id.img_create);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGroup.this, Inicio.class);
                intent.putExtra("USER_EMAIL",userEmail);
                startActivity(intent);
            }
        });

        date_in = findViewById(R.id.explorer_date_in);
        date_out = findViewById(R.id.explorer_date_out);
        name = findViewById(R.id.explorer_name);
        sw_drink = findViewById(R.id.explorer_drink);
        sw_smoke = findViewById(R.id.explorer_smoke);
        sw_couple = findViewById(R.id.explorer_couple);
        sw_after = findViewById(R.id.explorer_after);
        btn = findViewById(R.id.explorer_btn);
        let_in = findViewById(R.id.explorer_let_in);

        date_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CreateGroup.this,android.R.style.Theme_DeviceDefault_Dialog_MinWidth
                        ,dateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month +1;
                String date = day +"/"+month+"/"+year;
                date_in.setText(date);
            }
        };

        date_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CreateGroup.this,android.R.style.Theme_DeviceDefault_Dialog_MinWidth
                        ,dateSetListener1,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month +1;
                String date = day +"/"+month+"/"+year;
                date_out.setText(date);
            }
        };

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Name = name.getText().toString();
                final String Date_in= date_in.getText().toString();
                final String Date_out = date_out.getText().toString();
                final Integer Let_in = Integer.parseInt(let_in.getText().toString());

                profileRef
                        .whereEqualTo("email", userEmail)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Doc = document.getId();
                                    }
                                    DocumentReference docRef = profileRef.document(Doc);
                                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Profile profile = documentSnapshot.toObject(Profile.class);
                                            userName = profile.getName();
                                            ArrayList<String> persons = new ArrayList<>();
                                            persons.add(userName);

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

                                            Toast.makeText(CreateGroup.this, festival_name, Toast.LENGTH_SHORT).show();
                                            Groups group = new Groups(userName,Name,Date_in,Date_out,Let_in,persons,festival_name,smoke,after,couple,drink);
                                            groupsRef.document(name.getText().toString()).set(group);
                                            Toast.makeText(CreateGroup.this, "Group Created", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(CreateGroup.this,ShowGroups.class);
                                            intent.putExtra("USER_EMAIL",userEmail);
                                            intent.putExtra("GROUP_NAME",Name);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                        });

            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

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
                        Intent intent = new Intent(CreateGroup.this, ExplorerActivity.class);
                        intent.putExtra("USER_EMAIL",userEmail);
                        startActivity(intent);

                        break;

                    case R.id.navigation_travels:
                        if(addedGroups.isEmpty())
                        {
                            Intent intent1 = new Intent(CreateGroup.this, EmptyGroups.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        else
                        {
                            Intent intent1 = new Intent(CreateGroup.this, TravelsActivity.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        break;

                    case R.id.navigation_saved:
                        if(savedGroups.isEmpty())
                        {
                            Intent intent1 = new Intent(CreateGroup.this, EmptySaved.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }
                        else
                        {
                            Intent intent1 = new Intent(CreateGroup.this, SavedActivity.class);
                            intent1.putExtra("USER_EMAIL",userEmail);
                            startActivity(intent1);
                        }

                    case R.id.navigation_perfil:
                        Intent intent3 = new Intent(CreateGroup.this, ProfileActivity.class);
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

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateGroup.this, android.R.layout.simple_dropdown_item_1line, festivalsList);
                festivalsSpinner.setAdapter(arrayAdapter);
            }
        });
        festivalsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                festival_name = parentView.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected (AdapterView < ? > parentView){
                // your code here
            }

        });
    }

}
