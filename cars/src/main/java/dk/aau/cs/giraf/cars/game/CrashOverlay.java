package dk.aau.cs.giraf.cars.game;

import android.graphics.Paint;
import android.util.Log;

import java.util.List;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;

public class CrashOverlay extends Overlay {

    private boolean continuePressed = false;

    public CrashOverlay(){ super(); }

    @Override
    public GameState ButtonPressed(Game game)
    {
        Input.TouchEvent[] touchEvents = game.getTouchEvents();
        int width = game.getWidth();
        int height = game.getHeight();

        int len = touchEvents.length;
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents[i];
            Log.d("EventType", event.type + "");
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                continuePressed = false;
                if (inBounds(event, width/2-75,height/2-50,150,100)) {
                    return GameState.Running;
                }
            }
            else if (event.type == Input.TouchEvent.TOUCH_DOWN || event.type == Input.TouchEvent.TOUCH_DRAGGED) {
                if (inBounds(event, width/2-75,height/2-50,150,100)) {
                    continuePressed = true;
                }
                else
                    continuePressed = false;
            }
            else
                continuePressed = false;
        }
        return GameState.Crashed;
    }

    public void Draw(Game game)
    {
        int width = game.getWidth();
        int height = game.getHeight();
        Graphics g = game.getGraphics();
        g.drawARGB(155,0,0,0);
        g.drawString(game.getResources().getString(R.string.crash_button_text),width/2,height/2, continuePressed ? pFocus : pButton);
    }
}
