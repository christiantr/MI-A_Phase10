package com.mia.phase10;

import android.app.Activity;
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

    public MyDragEventListener() {
    }

    // This is the method that the system calls when it dispatches a drag event to the listener.
    @Override
    public boolean onDrag(View v, DragEvent event) {
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
                    GameLogicHandler.getInstance().layoffCard(GameLogicHandler.getInstance().getGameData().getActivePlayerId(), vw.getId()); //delete card of hand
                } catch (EmptyHandException | CardNotFoundException | PlayerNotFoundException e) {
                    e.printStackTrace();
                }

                // remove old card from discard pile
                (GameLogicHandler.getInstance().getGameActivity().getDiscardPileLayout()).removeAllViews();
                (GameLogicHandler.getInstance().getGameActivity().getPlaystationP1Layout()).removeAllViews();
                (GameLogicHandler.getInstance().getGameActivity().getPlaystationP1LayoutL()).removeAllViews();
                (GameLogicHandler.getInstance().getGameActivity().getPlaystationP1LayoutR()).removeAllViews();
                (GameLogicHandler.getInstance().getGameActivity().getPlaystationP2Layout()).removeAllViews();
                (GameLogicHandler.getInstance().getGameActivity().getPlaystationP2LayoutL()).removeAllViews();
                (GameLogicHandler.getInstance().getGameActivity().getPlaystationP2LayoutR()).removeAllViews();
                GameLogicHandler.getInstance().getGameActivity().showPlaystation2Cards();

                // switch player and remove cards from hand from active player
                if (GameLogicHandler.getInstance().getGameData().getActivePlayerId().equals("player_1")) {
                    GameLogicHandler.getInstance().getGameData().setActivePlayerId("player_2");
                    (GameLogicHandler.getInstance().getGameActivity().getDeck()).removeAllViews();
                    GameLogicHandler.getInstance().getGameActivity().getScore().setText(String.valueOf(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPoints()));
                    GameLogicHandler.getInstance().getGameActivity().switchPlayerName(GameLogicHandler.getInstance().getGameActivity().getPlayer2(), GameLogicHandler.getInstance().getGameActivity().getPlayer1());


                } else {
                    GameLogicHandler.getInstance().getGameData().setActivePlayerId("player_1");
                    (GameLogicHandler.getInstance().getGameActivity().getDeck()).removeAllViews();
                    GameLogicHandler.getInstance().getGameActivity().getScore().setText(String.valueOf(GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getPoints()));
                    GameLogicHandler.getInstance().getGameActivity().switchPlayerName(GameLogicHandler.getInstance().getGameActivity().getPlayer1(), GameLogicHandler.getInstance().getGameActivity().getPlayer2());
                }
                GameLogicHandler.getInstance().getGameActivity().showPlaystation1Cards();
                GameLogicHandler.getInstance().getGameActivity().showHandCards();

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
