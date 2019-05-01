package com.example.househelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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

public class RegisterHousehold extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailInput, username, password, household;
    private ProgressDialog loadingBar;
    String email, pass, houseName, name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_household);

        household = (EditText) findViewById(R.id.householdName);
        username = (EditText) findViewById(R.id.username);
        emailInput = (EditText) findViewById(R.id.email1);
        password = (EditText) findViewById(R.id.pw);


        Button registerButton = (Button) findViewById(R.id.createUser);
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String house = household.getText().toString();
                String mail = emailInput.getText().toString();
                String pw = password.getText().toString();
                String user = username.getText().toString();
                registerUser(house, user, mail, pw);
            }
        });


        mAuth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(this);
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
                    Intent i = new Intent(RegisterHousehold.this, TaskListActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("houseName",houseName);
                    extras.putString("username", name);
                    i.putExtras(extras);
                    startActivity(i);
                    Animatoo.animateSwipeLeft(RegisterHousehold.this);


                } else {
                    String msg = task.getException().toString();
                    Toast.makeText(RegisterHousehold.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void registerUser(String house, String user, String mail, String pw) {
        if(!validateForm()) {
            return;
        }
        loadingBar.setTitle("Creating New Household & Account");
        loadingBar.setMessage("Please wait... ");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();

        email = mail;
        pass = pw;
        houseName = house;
        name = user;


        mAuth.createUserWithEmailAndPassword(mail, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterHousehold.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            loadingBar.setTitle("Account Created Successfully");
                            loadingBar.setMessage("Now logging into your account");
                            loadingBar.setCanceledOnTouchOutside(true);
                            loadingBar.show();

                            DatabaseReference addUser = FirebaseDatabase.getInstance().getReference("Households").child(houseName).child("Users");
//                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                            addUser.child(name).setValue(user.getUid());

                            login(email, pass);





                        } else {
                            String msg = task.getException().toString();
                            Toast.makeText(RegisterHousehold.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

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
