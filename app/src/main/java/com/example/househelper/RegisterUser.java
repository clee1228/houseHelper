package com.example.househelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterUser extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailInput, username, password, household;
    private ProgressDialog loadingBar;
    String email, pass, houseName, name;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        household = (EditText) findViewById(R.id.household);
        username = (EditText) findViewById(R.id.firstName);
        emailInput = (EditText) findViewById(R.id.register_email);
        password = (EditText) findViewById(R.id.pw);


        mToolbar = (Toolbar) findViewById(R.id.registerHouseToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Button registerButton = (Button) findViewById(R.id.loginButton);
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Households");
                DatabaseReference houseRef = rootRef.child(household.getText().toString());
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) {
                            Toast.makeText(RegisterUser.this, "This household doesn't exist", Toast.LENGTH_SHORT).show();
                        }else{
                            String house = household.getText().toString();
                            String mail = emailInput.getText().toString();
                            String pw = password.getText().toString();
                            String user = username.getText().toString();
                            registerUser(house, user, mail, pw);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("onDatachange", databaseError.getMessage());
                    }
                };
                houseRef.addListenerForSingleValueEvent(eventListener);

            }
        });


        mAuth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(this);
    }

    private void login(String username, String password) {
        if (!validateForm()){
            return;
        }

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user != null) {
                        DatabaseReference addUser = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                        addUser.child("display").setValue(user.getDisplayName());
                        addUser.child("email").setValue(user.getEmail());
                        addUser.child("house").setValue(houseName);

                        DatabaseReference addUserToHousehold = FirebaseDatabase.getInstance().getReference("Households").child(houseName).child("Users");
                        addUserToHousehold.child(user.getDisplayName()).setValue(user.getUid());
                    }


                    loadingBar.setTitle("Welcome " + user.getDisplayName());
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();
                    Intent i = new Intent(RegisterUser.this, TaskListActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("houseName",houseName);
                    extras.putString("username", name);
                    i.putExtras(extras);
                    startActivity(i);
                    Animatoo.animateSwipeLeft(RegisterUser.this);


                } else {
                    String msg = task.getException().toString();
                    Toast.makeText(RegisterUser.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void registerUser(String house, String user, String mail, String pw) {
        if(!validateForm()) {
            return;
        }
        loadingBar.setTitle("Creating New Account");
        loadingBar.setMessage("Please wait, while we are creating your account.. ");
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
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            user.updateProfile(profileUpdates);


                            Toast.makeText(RegisterUser.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            loadingBar.setTitle("Account Created Successfully");
                            loadingBar.setMessage("Now logging into your account");
                            loadingBar.setCanceledOnTouchOutside(true);
                            loadingBar.show();


//

                            login(email, pass);


//


                        } else {
                            String msg = task.getException().toString();
                            Toast.makeText(RegisterUser.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
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
