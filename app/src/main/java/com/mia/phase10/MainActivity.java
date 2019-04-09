package com.mia.phase10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b=findViewById(R.id.openShuffling);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShufflingActivity();
            }
        });
    }

    public void openShufflingActivity() {
        Intent intent = new Intent(this, ShufflingActivity.class);
        startActivity(intent);
    }
}
