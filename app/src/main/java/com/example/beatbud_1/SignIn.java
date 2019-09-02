package com.example.beatbud_1;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.opencensus.tags.Tag;

public class SignIn extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText inputEmail, inputPassword, inputFirstName, inputLastName;
    private Button btnSignIn;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private Spinner signin_spinner;
    private String gender;
    private ImageView img_signIn;
    private StorageReference mStorageRef;
    private Uri mImageUri;


    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    // private CollectionReference profileRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_in);

        auth = FirebaseAuth.getInstance();


        //  profileRef =database.collection("profile");
        signin_spinner = findViewById(R.id.signin_spinner);
        btnSignIn = (Button) findViewById(R.id.btn_signIn);
        inputEmail = (EditText) findViewById(R.id.email_signIn);
        inputPassword = (EditText) findViewById(R.id.password_signIn);
        inputFirstName = (EditText) findViewById(R.id.inputFirstName);
        inputLastName = (EditText) findViewById(R.id.inputLastName);
        img_signIn = findViewById(R.id.img_signIn);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        ArrayList<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");

        ArrayAdapter<String> spinner_gender = new ArrayAdapter<>(SignIn.this, android.R.layout.simple_dropdown_item_1line, genders);
        signin_spinner.setAdapter(spinner_gender);

        img_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageUri == null) {
                    Toast.makeText(SignIn.this, "Não selecionou nenhuma foto de perfil", Toast.LENGTH_SHORT).show();
                } else {
                    final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
                    fileReference.putFile(mImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                    //  Toast.makeText(register.this, "Upload successful", Toast.LENGTH_LONG).show();

                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri downloadUrL = uri;


                                            final String email = inputEmail.getText().toString().trim();
                                            String password = inputPassword.getText().toString().trim();
                                            final String name = inputFirstName.getText().toString() + " "+inputLastName.getText().toString();

                                            //mDatabase= FirebaseDatabase.getInstance().getReference();
                                            // final String id = mDatabase.push().getKey();

                                            final Profile profile = new Profile(name, email, gender, downloadUrL.toString());
                                            // final Map<String, Object> perfil = new HashMap<>();
                                            // perfil.put("Name",name);
                                            // perfil.put("Email",email);

                                            if (TextUtils.isEmpty(email)) {
                                                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            if (TextUtils.isEmpty(password)) {
                                                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            if (password.length() < 6) {
                                                Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            //create user
                                            auth.createUserWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            Toast.makeText(SignIn.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                                            // If sign in fails, display a message to the user. If sign in succeeds
                                                            // the auth state listener will be notified and logic to handle the
                                                            // signed in user can be handled in the listener.
                                                            if (!task.isSuccessful()) {
                                                                Toast.makeText(SignIn.this, "Authentication failed." + task.getException(),
                                                                        Toast.LENGTH_SHORT).show();
                                                            } else {

                                                                database.collection("profile").document(email).set(profile)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Toast.makeText(SignIn.this, "Profile Saved", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(SignIn.this, "ERROR", Toast.LENGTH_SHORT).show();
                                                                                // Log.d(e.toString());
                                                                            }
                                                                        });
                                                                Intent intent = new Intent(SignIn.this, Inicio.class);
                                                                intent.putExtra("USER_EMAIL", email);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                                }

                            });
                }
            }
        });

    }
        @Override
        protected void onStart() {
        super.onStart();

        signin_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


}
