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
import android.widget.Toast;

public class ShufflingActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sManager;
    private Sensor accelerometer;
    private View v;
    private long lastUpdate;
    float x,y,z;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuffling);

        v=new View(this);

        sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        sManager.unregisterListener(this);
        super.onPause();
    }

    public void shuffle(View view){
        ImageView card = findViewById(R.id.movingCard);
        Animation animShuffle =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shuffle);
        card.startAnimation(animShuffle);

    }


    public void onSensorChanged(SensorEvent se) {

        x = se.values[0];
        y = se.values[1];
        z = se.values[2];

        float accelerationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

        long actualTime = se.timestamp;
        if (accelerationSquareRoot >= 2)
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            shuffle(v);

        }

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
