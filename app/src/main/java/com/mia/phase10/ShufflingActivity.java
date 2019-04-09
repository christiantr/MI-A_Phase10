package com.mia.phase10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class ShufflingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuffling);
    }

    public void shuffle(View view){
        ImageView card = findViewById(R.id.movingCard);
        Animation animShuffle =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shuffle);
        card.startAnimation(animShuffle);

    }
}
