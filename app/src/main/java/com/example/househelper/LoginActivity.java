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

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /*** Id to identity READ_CONTACTS permission request.*/
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private FirebaseAuth mAuth;
    private EditText username, password;
    private ProgressDialog loadingBar;
    String email, pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        username = findViewById(R.id.household);
        password = (EditText) findViewById(R.id.pw);

        findViewById(R.id.createUser).setOnClickListener(this);
        findViewById(R.id.registerLink).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        String user = username.getText().toString();
        String pass = password.getText().toString();
        if(i == R.id.registerLink) {
            final Intent goToRegistration = new Intent(this, Registration.class);
            startActivity(goToRegistration);
            Animatoo.animateSwipeLeft(LoginActivity.this);


        } else if(i == R.id.createUser) {
            login(user, pass);
        }
    }

    private void login(String username, String password) {
        if (!validateForm()){
            return;
        }

        email = username;

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loadingBar.setTitle("Welcome, " + email);
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();
                    Intent first = new Intent(LoginActivity.this, TaskListActivity.class);
                    first.putExtra("username", email);
                    startActivity(first);


                } else {
                    String msg = task.getException().toString();
                    Toast.makeText(LoginActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    private void registerUser(String un, String pw) {
//
//
//        if(!validateForm()) {
//            return;
//        }
//        loadingBar.setTitle("Creating New Account");
//        loadingBar.setMessage("Please wait, while we are creating your account.. ");
//        loadingBar.setCanceledOnTouchOutside(true);
//        loadingBar.show();
//
//        email = un;
//        pass = pw;
//
//        mAuth.createUserWithEmailAndPassword(un, pw)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(LoginActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
//                            loadingBar.dismiss();
//                            loadingBar.setTitle("Account Created Successfully");
//                            loadingBar.setMessage("Now logging into your account");
//                            loadingBar.setCanceledOnTouchOutside(true);
//                            loadingBar.show();
//                            login(email, pass);
//                            Intent first = new Intent(LoginActivity.this, TaskListActivity.class);
//                            first.putExtra("username", email);
//                            startActivity(first);
//
//                        } else {
//                            String msg = task.getException().toString();
//                            Toast.makeText(LoginActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
//                            loadingBar.dismiss();
//
//                        }
//                    }
//                });
//    }

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


//        final Intent goToTasks = new Intent(this, TaskListActivity.class);

//        loginButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                attemptLogin();
//                startActivity(goToTasks);
//            }
//        });
//
//        TextView register = (TextView) findViewById(R.id.registerLink);
//        final Intent goToRegistration = new Intent(this, Registration.class);
//        register.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(goToRegistration);
//                Animatoo.animateSwipeLeft(LoginActivity.this);
//            }
//        });





}

