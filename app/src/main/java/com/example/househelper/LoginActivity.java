package com.example.househelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /*** Id to identity READ_CONTACTS permission request.*/
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private FirebaseAuth mAuth;
    private EditText username, password, household;
    private ProgressDialog loadingBar;
    String email, pass, houseName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        household = findViewById(R.id.household);
        username = findViewById(R.id.email);
        password = (EditText) findViewById(R.id.pw);

        findViewById(R.id.loginButton).setOnClickListener(this);
        findViewById(R.id.registerLink).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String houseName = household.getText().toString();

        if(i == R.id.registerLink) {
            final Intent goToRegistration = new Intent(this, Registration.class);

            startActivity(goToRegistration);
            Animatoo.animateSwipeLeft(LoginActivity.this);


        } else if(i == R.id.loginButton) {
            login(user, pass, houseName);
        }
    }

    private void login(String userEmail, String password, String house) {
        if (!validateForm()){
            return;
        }

        email = userEmail;
        houseName = house;

        mAuth.signInWithEmailAndPassword(userEmail, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    loadingBar.setTitle("Welcome " + user.getDisplayName());
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();
                    Intent first = new Intent(LoginActivity.this, TaskListActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("houseName",houseName);
                    extras.putString("username", email);
                    first.putExtras(extras);
                    startActivity(first);


                } else {
                    String msg = task.getException().toString();
                    Toast.makeText(LoginActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean validateForm() {
        boolean valid = true;

        String user = username.getText().toString();
        String pass = password.getText().toString();

        if (TextUtils.isEmpty(user)) {
            username.setError("Required.");
            valid = false;
        } else {
            username.setError(null);
        }


        if (TextUtils.isEmpty(pass)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }


}

