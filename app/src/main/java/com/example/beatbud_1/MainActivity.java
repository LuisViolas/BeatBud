package com.example.beatbud_1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private Button btnSignup, btnLogin, btnReset;


    public boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Sem conexão à internet");
        builder.setMessage("Precisa de usar Wi-Fi ou dados móveis!");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        return builder;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);


        // Function that will check if internet is available
        boolean connection = isInternetAvailable();
        if(connection == false) {
            try {
                buildDialog(MainActivity.this).show();
            }
            catch (Exception e) {
                Log.d("Error", "Show Dialog: " + e.getMessage());
            }

        }

        //Get Firebase auth instance
        //FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, Inicio.class));
            finish();
        }

        // set the view now
        getSupportActionBar().hide();
       setContentView(R.layout.activity_main);
        TextView login = (TextView) findViewById(R.id.txtLogin);
        TextView Registo = (TextView) findViewById(R.id.txtRegisto);


        inputEmail = (EditText) findViewById(R.id.UserName);
        inputPassword = (EditText) findViewById(R.id.Password);


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final String email = inputEmail.getText().toString();
                    final String password = inputPassword.getText().toString();

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //authenticate user
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (password.length() < 6) {
                                            inputPassword.setError(getString(R.string.minimum_password));
                                        } else {
                                            Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Intent intent = new Intent(MainActivity.this, ExplorerActivity.class);
                                        intent.putExtra("USER_EMAIL",email);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            });
        Registo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignIn.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
            // Check if internet is available
            boolean connection = isInternetAvailable();
            if(connection == false) {
                try {
                    buildDialog(MainActivity.this).show();
                }
                catch (Exception e) {
                    Log.d("Error", "Show Dialog: " + e.getMessage());
                }

            }
        }
}
