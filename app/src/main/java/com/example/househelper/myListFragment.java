package com.example.househelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.widget.AdapterView;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class myListFragment extends ListFragment implements AdapterView.OnItemClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Profile, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position) {


            case 0:
                AlertDialog.Builder emailDialog = new AlertDialog.Builder(getActivity());
                final View eView = getLayoutInflater().inflate(R.layout.email_alert_dialog, null);
                emailDialog.setTitle("Change E-mail");
                final AlertDialog eDialog = emailDialog.create();

                final EditText currEmail = (EditText) eView.findViewById(R.id.currEmail);
                final EditText newEmail = (EditText) eView.findViewById(R.id.newEmail);
                final EditText pw = (EditText) eView.findViewById(R.id.currPw);
                final Button changeButton = (Button) eView.findViewById(R.id.change_button);
                final Button cancelButton = (Button) eView.findViewById(R.id.cancel_button);

                eDialog.setView(eView);
                eDialog.show();

                changeButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        String currMail = currEmail.getText().toString();
                        String password = pw.getText().toString();
                        final String newMail = newEmail.getText().toString();


                        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
                        AuthCredential credential = EmailAuthProvider.getCredential(currMail, password);

                        currUser.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getActivity(), "E-mail updated", Toast.LENGTH_SHORT).show();
                                        eDialog.dismiss();
                                        if(task.isSuccessful()){
                                            updateEmail(newMail);
                                        } else {
                                            Toast.makeText(getActivity(), "E-mail or password entered is incorrect", Toast.LENGTH_SHORT).show();
                                            eDialog.setView(eView);
                                            eDialog.show();} } }); } });

                cancelButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        eDialog.dismiss(); }
                });

                break;

            case 1:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                final View mView = getLayoutInflater().inflate(R.layout.password_alert_dialog, null);
                alertDialog.setTitle("Change Password");
                final AlertDialog dialog = alertDialog.create();

                final EditText userEmail = (EditText) mView.findViewById(R.id.enter_email);
                final EditText currPw = (EditText) mView.findViewById(R.id.enter_currPw);
                final EditText newPw = (EditText) mView.findViewById(R.id.enter_newPw);
                final Button changePwButton = (Button) mView.findViewById(R.id.changePw_Button);
                final Button cancelPwButton = (Button) mView.findViewById(R.id.cancel_changePw);

                dialog.setView(mView);
                dialog.show();

                changePwButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        String enteredEmail = userEmail.getText().toString();
                        String currPassword = currPw.getText().toString();
                        final String newPassword = newPw.getText().toString();

                        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
                        AuthCredential credential = EmailAuthProvider.getCredential(enteredEmail, currPassword);

                        currUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getActivity(), "Password updated", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        if(task.isSuccessful()){
                                            updatePassword(newPassword);
                                        } else {
                                            Toast.makeText(getActivity(), "E-mail or password entered is incorrect", Toast.LENGTH_SHORT).show();
                                            dialog.setView(mView);
                                            dialog.show(); } }
                                }); }
                });

                cancelPwButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        dialog.dismiss(); }
                });


                break;

            case 2:
                signOut();
                break;
        }

    }






    public void updateEmail(String email){
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        currUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Log","Email is updated");
                } } });
    }


    public void updatePassword(String newPassword){
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        currUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Log","Password is updated"); } } });
    }


    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getActivity(), "Signed out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

    }
}