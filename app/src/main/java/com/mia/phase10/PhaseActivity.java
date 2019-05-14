package com.mia.phase10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class PhaseActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phasen_show);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_phasen) {
            Toast.makeText(getApplicationContext(), "Phase gedrückt!", Toast.LENGTH_LONG).show();
            Intent PhaseActivity = new Intent(this, PhaseActivity.class);
            startActivity(PhaseActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
