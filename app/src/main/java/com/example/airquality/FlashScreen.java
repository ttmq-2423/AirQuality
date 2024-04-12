package com.example.airquality;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FlashScreen extends AppCompatActivity {
    Animation topAnim;
    Animation bottomAnim;
    Animation wellcomeAnim;
    ImageView imageLogo;
    ImageView imageAirquality;
    ImageView imagewellcome;
    private static int flashScreen = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_flash_screen);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_amination);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.botton_amination);
        wellcomeAnim = AnimationUtils.loadAnimation(this, R.anim.wellcome_anim);

        imageLogo = findViewById(R.id.imageLogo);
        imageAirquality = findViewById(R.id.imageAirquality);
        imagewellcome = findViewById(R.id.imagewellcome);
        imageLogo.setAnimation(topAnim);
        imageAirquality.setAnimation(bottomAnim);
        imagewellcome.setAnimation(wellcomeAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FlashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, flashScreen);
    }
}