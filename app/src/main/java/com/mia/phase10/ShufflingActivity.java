package com.mia.phase10;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ShufflingActivity extends AppCompatActivity {
    private SensorManager sManager;
    private Sensor accelerometer;
    private View v;

    public int getShuffleCount() {
        return shuffleCount;
    }

    private int shuffleCount;
    private ProgressBar bar;


    private SensorEventListener listener=new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent se) {
            float x,y,z;
            x = se.values[0];
            y = se.values[1];
            z = se.values[2];
           if(enoughAcceleration(x,y,z)){
                shuffle(v);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuffling);

        v=new View(this);
        bar=findViewById(R.id.progressBar);

        sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        sManager.unregisterListener(listener);
        super.onPause();
    }

    protected boolean enoughAcceleration(float x, float y, float z) {

        float acceleration = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

        if (acceleration >= 2) { return true; }
        else return false;
    }

    protected void shuffle(View view){
        ImageView card = findViewById(R.id.movingCard);
        Animation animShuffle =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shuffle);
        card.startAnimation(animShuffle);

        animShuffle.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                sManager.unregisterListener(listener);
                shuffleCount++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                bar.incrementProgressBy(25);
                if(shuffleCount==4){
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }



}
