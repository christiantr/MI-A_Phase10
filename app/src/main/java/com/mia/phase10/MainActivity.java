package com.mia.phase10;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mia.phase10.classes.SimpleCard;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {

    private Button b;
    private ImageView card;
    private LinearLayout deck;
    private LinearLayout discardPile;

    Integer[] imageIDs = {
            R.drawable.card_b_1, R.drawable.card_b_2, R.drawable.card_b_3, R.drawable.card_g_1, R.drawable.card_y_4,
            R.drawable.card_expose, R.drawable.card_joker, R.drawable.card_r_3, R.drawable.card_g_5, R.drawable.card_y_12
    };

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = findViewById(R.id.openShuffling);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startShufflingActivity();
            }
        });

        card = findViewById(R.id.ID_discard_pile);
        deck = findViewById(R.id.ID_deck);
        discardPile = findViewById(R.id.ID_discard_layout);

        MyDragEventListener myDragEventListener = new MyDragEventListener();
        deck.setOnDragListener(myDragEventListener);
        discardPile.setOnDragListener(myDragEventListener);

        card.setTag("DISCARD PILE");
        card.setOnLongClickListener(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(-70, 0, 0, 0);
        for (int i = 0; i < 10; i++) {
            ImageView card = new ImageView(this);
            card.setImageResource(imageIDs[i]);
            card.setLayoutParams(lp);
            card.setTag("DISCARD PILE");
            card.setOnLongClickListener(this);
            deck.addView(card);
        }
    }

    public void startShufflingActivity() {
        //CardStack.mixStack();
        Intent intent = new Intent(this, ShufflingActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        // Create a new ClipData.Item from the ImageView object's tag
        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
        // Create a new ClipData using the tag as a label, the plain text MIME type, and
        // the already-created item. This will create a new ClipDescription object within the
        // ClipData, and set its MIME type entry to "text/plain"
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);
        // Instantiates the drag shadow builder.
        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);
        // Starts the drag
        v.startDrag(data      // data to be dragged
                , dragshadow  // drag shadow builder
                , v           // local data about the drag and drop operation
                , 0      // flags (not currently used, set to 0)
        );
        return true;
    }
}
