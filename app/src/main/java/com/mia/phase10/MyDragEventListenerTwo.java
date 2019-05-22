package com.mia.phase10;

import android.content.ClipData;
import android.content.ClipDescription;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mia.phase10.exceptionClasses.CardNotFoundException;
import com.mia.phase10.exceptionClasses.EmptyHandException;
import com.mia.phase10.exceptionClasses.PlayerNotFoundException;
import com.mia.phase10.gameLogic.GameLogicHandler;
import com.mia.phase10.gameLogic.PlaystationType;


public class MyDragEventListenerTwo implements View.OnDragListener {

    public MyDragEventListenerTwo() {
    }

    // This is the method that the system calls when it dispatches a drag event to the listener.
    @Override
    public boolean onDrag(View v, DragEvent event) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(0, 0, 0, 0);
        lp.height = 200;

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
                // Invalidates the view to force a redraw
                v.invalidate();
                ImageView vw = (ImageView) event.getLocalState();
                ViewGroup owner = (ViewGroup) vw.getParent();

                String player = GameLogicHandler.getInstance().getGameData().getPlayers().get(GameLogicHandler.getInstance().getGameData().getActivePlayerId()).getId();

                if (v == GameLogicHandler.getInstance().getGameActivity().getPlaystationP1Layout() || v == GameLogicHandler.getInstance().getGameActivity().getPlaystationP1LayoutL()) {
                    GameLogicHandler.getInstance().getGameActivity().getCheck().setVisibility(View.VISIBLE);
                    GameLogicHandler.getInstance().getGameActivity().getCancel().setVisibility(View.VISIBLE);
                    try {
                        GameLogicHandler.getInstance().layoffPhase(PlaystationType.PLAYSTATION, player, vw.getId());
                    } catch (EmptyHandException | CardNotFoundException | PlayerNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (v == GameLogicHandler.getInstance().getGameActivity().getPlaystationP1LayoutR()) {
                    GameLogicHandler.getInstance().getGameActivity().getCheck().setVisibility(View.VISIBLE);
                    GameLogicHandler.getInstance().getGameActivity().getCancel().setVisibility(View.VISIBLE);
                    try {
                        GameLogicHandler.getInstance().layoffPhase(PlaystationType.PLAYSTATION_RIGHT, player, vw.getId());
                    } catch (EmptyHandException | CardNotFoundException | PlayerNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (v == GameLogicHandler.getInstance().getGameActivity().getPlaystationP2Layout() || v == GameLogicHandler.getInstance().getGameActivity().getPlaystationP2LayoutL()) {
                    GameLogicHandler.getInstance().getGameActivity().getCheckTwo().setVisibility(View.VISIBLE);
                    GameLogicHandler.getInstance().getGameActivity().getCancelTwo().setVisibility(View.VISIBLE);
                    try {
                        GameLogicHandler.getInstance().layoffPhase(PlaystationType.PLAYSTATION_TWO, player, vw.getId());
                    } catch (EmptyHandException | CardNotFoundException | PlayerNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    GameLogicHandler.getInstance().getGameActivity().getCheckTwo().setVisibility(View.VISIBLE);
                    GameLogicHandler.getInstance().getGameActivity().getCancelTwo().setVisibility(View.VISIBLE);
                    try {
                        GameLogicHandler.getInstance().layoffPhase(PlaystationType.PLAYSTATION_TWO_RIGHT, player, vw.getId());
                    } catch (EmptyHandException | CardNotFoundException | PlayerNotFoundException e) {
                        e.printStackTrace();
                    }
                }


                // Returns true. DragEvent.getResult() will return true.
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                // Invalidates the view to force a redraw
                v.invalidate();
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
