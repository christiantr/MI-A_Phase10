package com.mia.phase10;

import android.content.ClipData;
import android.content.ClipDescription;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mia.phase10.classes.GameData;
import com.mia.phase10.classes.Player;
import com.mia.phase10.exceptionClasses.CardNotFoundException;
import com.mia.phase10.exceptionClasses.EmptyHandException;
import com.mia.phase10.exceptionClasses.PlayerNotFoundException;
import com.mia.phase10.gameLogic.GameLogicHandler;

import java.util.Map;

public class MyDragEventListener implements View.OnDragListener {

    private GameActivity gameActivity = null;

    public MyDragEventListener(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    // This is the method that the system calls when it dispatches a drag event to the listener.
    @Override
    public boolean onDrag(View v, DragEvent event) {
        GameLogicHandler gameLogicHandler = GameLogicHandler.getInstance();
        GameData gameData = gameLogicHandler.getGameData();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);

        // Defines a variable to store the action type for the incoming event
        int action = event.getAction();
        // Handles each of the expected events

        switch (action) {

            case DragEvent.ACTION_DRAG_STARTED:
                // Determines if this View can accept the dragged data
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    return true;
                }
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                v.invalidate();
                return true;

            case DragEvent.ACTION_DROP:
                // Gets the item containing the dragged data
                ClipData.Item item = event.getClipData().getItemAt(0);
                // Gets the text data from the item.
                String dragData = item.getText().toString();
                // Displays a message containing the dragged data.
                Toast.makeText(v.getContext(), "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();
                // Invalidates the view to force a redraw
                v.invalidate();

                ImageView vw = (ImageView) event.getLocalState();
                ViewGroup owner = (ViewGroup) vw.getParent();
                try {
                    gameLogicHandler.layoffCard(gameData.getActivePlayerId(), vw.getId());
                } catch (EmptyHandException | CardNotFoundException | PlayerNotFoundException e) {
                    e.printStackTrace();
                }

                // remove old card from discard pile
                (gameActivity.getDiscardPileLayout()).removeAllViews();
                // switch player and remove cards from hand from active player
                Map<String, Player> players = gameData.getPlayers();
                if (gameData.getActivePlayerId().equals("player_1")) {
                    gameData.setActivePlayerId("player_2");
                    Player activePlayer = players.get(gameData.getActivePlayerId());
                    (gameActivity.getDeck()).removeAllViews();
                    gameActivity.getScore().setText(String.valueOf(activePlayer.getPoints()));
                    gameActivity.switchPlayerName(gameActivity.getPlayer2(), gameActivity.getPlayer1());

                } else {
                    gameData.setActivePlayerId("player_1");
                    Player activePlayer = players.get(gameData.getActivePlayerId());
                    (gameActivity.getDeck()).removeAllViews();
                    gameActivity.getScore().setText(String.valueOf(activePlayer.getPoints()));
                    gameActivity.switchPlayerName(gameActivity.getPlayer1(), gameActivity.getPlayer2());
                }
                gameActivity.showHandCards();

                owner.removeView(vw); //remove the dragged view
                //caste the view into LinearLayout as our drag acceptable layout is LinearLayout
                LinearLayout container = (LinearLayout) v;
                vw.setLayoutParams(lp);
                container.addView(vw);//Add the dragged view
                vw.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE
                // Returns true. DragEvent.getResult() will return true.
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                // Invalidates the view to force a redraw
                v.invalidate();
                // Does a getResult(), and displays what happened.
                if (event.getResult())
                    Toast.makeText(v.getContext(), "The drop was handled.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(v.getContext(), "The drop didn't work.", Toast.LENGTH_SHORT).show();
                // returns true; the value is ignored.
                return true;
            // An unknown action type was received.
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }
}
