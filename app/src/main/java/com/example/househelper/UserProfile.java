package com.example.househelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));


    }


}
