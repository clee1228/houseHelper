package com.example.househelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        ImageView myImageView= (ImageView)findViewById(R.id.listIcon);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        myImageView.startAnimation(myFadeInAnimation);

//        AnimateBell();

        ImageView shop= (ImageView)findViewById(R.id.dishsoap);
        Animation shopAni = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        shop.startAnimation(shopAni);


        ImageView chat= (ImageView)findViewById(R.id.chatIcon);
        Animation chatAni = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        chat.startAnimation(chatAni);


        Button createHousehold = (Button) findViewById(R.id.createNewHousehold);
        final Intent registerHouse = new Intent(this, RegisterHousehold.class);

        createHousehold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(registerHouse);
                Animatoo.animateSwipeLeft(Registration.this);
            }
        });

        Button joinHousehold = (Button) findViewById(R.id.joinHousehold);
        final Intent registerUser = new Intent(this, RegisterUser.class);

        joinHousehold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(registerUser);
                Animatoo.animateSwipeLeft(Registration.this);

            }
        });


    }

    public void AnimateBell() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        ImageView imgBell= (ImageView) findViewById(R.id.listIcon);
        imgBell.setAnimation(shake);
    }
}
