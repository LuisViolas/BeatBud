package com.example.beatbud_1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference groupsRef = db.collection("groups");
    private String userEmail;
    private String Doc = "";
    private String userName;
    private CollectionReference profileRef = db.collection("profile");
    private Profile userProfile;
    private TextView profile_name, profile_email, profile_phone, profile_about, profile_birthday;
    private Button profile_btn;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Spinner profile_gender;
    private String gender;
    private ImageView profile_img, img;
    ArrayList<String> savedGroups;
    ArrayList<String> addedGroups;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);
        userEmail = getIntent().getStringExtra("USER_EMAIL");


        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        profile_img = findViewById(R.id.img_inicio);

        img = findViewById(R.id.img_goups);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, Inicio.class);
                intent.putExtra("USER_EMAIL",userEmail);
                startActivity(intent);
            }
        });


        profile_gender = findViewById(R.id.signin_spinner);
        profile_about = findViewById(R.id.profile_about);
        profile_name = findViewById(R.id.profile_name);
        profile_email = findViewById(R.id.profile_email);
        profile_phone = findViewById(R.id.profile_phone);
        profile_btn = findViewById(R.id.profile_btn);
        profile_birthday = findViewById(R.id.profile_birthday);
        profile_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ProfileActivity.this, android.R.style.Theme_DeviceDefault_Dialog_MinWidth
                        , dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                profile_birthday.setText(date);
            }
        };

        profileRef.whereEqualTo("email", userEmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Profile profile = document.toObject(Profile.class);
                    savedGroups = profile.getSavedGroups();
                    userName = profile.getName();
                }
                groupsRef.whereArrayContains("persons", userName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

        ArrayList<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");

        ArrayAdapter<String> spinner_gender = new ArrayAdapter<>(ProfileActivity.this, android.R.layout.simple_dropdown_item_1line, genders);
        profile_gender.setAdapter(spinner_gender);


        profileRef
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Toast.makeText(ProfileActivity.this, "DATAAA" + document.getData(), Toast.LENGTH_SHORT);
                                Log.d("ASAS", document.getId() + " => " + document.getData());
                                Doc = document.getId();
                            }
                            DocumentReference docRef = profileRef.document(Doc);
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    userProfile = documentSnapshot.toObject(Profile.class);
                                    Picasso.get()
                                            .load(userProfile.getImageurl())
                                            .into(profile_img);
                                    profile_about.setText(userProfile.getAbout());
                                    profile_birthday.setText(userProfile.getBirthday());
                                    profile_email.setText(userProfile.getEmail());
                                    profile_phone.setText(userProfile.getPhone());
                                    profile_name.setText(userProfile.getName());
                                    String user_gender = userProfile.getGender();
                                    if (user_gender.equals("Male")) {
                                        profile_gender.setSelection(0);
                                    } else {
                                        profile_gender.setSelection(1);
                                    }
                                    Log.d("HELOOO", "facil " + userName);
                                }
                            });
                        }
                    }
                });
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    Toast.makeText(ProfileActivity.this, "ENTRAAAAAAA", Toast.LENGTH_SHORT).show();
                                        String about = profile_about.getText().toString();
                                        String birthday = profile_birthday.getText().toString();
                                        String phone = profile_phone.getText().toString();
                                        String name = profile_name.getText().toString();

                                       // Profile profile = new Profile(name gender, about, birthday, phone);

                                        profileRef.document(Doc).update("about", about, "birthday", birthday, "phone", phone, "gender", gender);
                                        Toast.makeText(ProfileActivity.this, "Profile Saved!", Toast.LENGTH_SHORT).show();

                                    }
                                });
        BottomNavigationView navigation = findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_explorer:
                        Intent intent = new Intent(ProfileActivity.this, ExplorerActivity.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        startActivity(intent);

                        break;

                    case R.id.navigation_travels:

                        if (addedGroups.isEmpty()) {
                            Intent intent1 = new Intent(ProfileActivity.this, EmptyGroups.class);
                            intent1.putExtra("USER_EMAIL", userEmail);
                            startActivity(intent1);
                        } else {

                            Intent intent1 = new Intent(ProfileActivity.this, TravelsActivity.class);
                            intent1.putExtra("USER_EMAIL", userEmail);
                            startActivity(intent1);
                        }
                        break;

                    case R.id.navigation_saved:
                        if (savedGroups.isEmpty()) {
                            Intent intent1 = new Intent(ProfileActivity.this, EmptySaved.class);
                            intent1.putExtra("USER_EMAIL", userEmail);
                            startActivity(intent1);
                        } else {
                            Intent intent1 = new Intent(ProfileActivity.this, SavedActivity.class);
                            intent1.putExtra("USER_EMAIL", userEmail);
                            startActivity(intent1);
                        }
                        break;

                    case R.id.navigation_perfil:

                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        profile_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}

