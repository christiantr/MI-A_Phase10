package com.mia.phase10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mia.phase10.activities.GameStartActivity;

public class MainActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
        goToGameStartActivity();
    }


    public void goToGameStartActivity() {
        Intent startActivity = new Intent(this, GameStartActivity.class);
        startActivity(startActivity);
    }

}
