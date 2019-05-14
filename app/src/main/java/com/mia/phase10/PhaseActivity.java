package com.mia.phase10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class PhaseActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phasen_show);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.back) {
            Intent PhaseActivity = new Intent(this, GameActivity.class);
            startActivity(PhaseActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
