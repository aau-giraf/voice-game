package dk.aau.cs.giraf.cars.game;

import android.graphics.Paint;

import java.util.List;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;

public class CrashOverlay extends Overlay {
    public CrashOverlay(){ super(); }

    public GameState ButtonPressed(Game game)
    {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int width = game.getWidth();
        int height = game.getHeight();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                if (inBounds(event, width/2-75,height/2-50,150,100)) {
                    return GameState.Running;
                }
            }
        }
        return GameState.Crashed;
    }

    public void Draw(Game game)
    {
        int width = game.getWidth();
        int height = game.getHeight();
        Graphics g = game.getGraphics();
        g.drawARGB(155,0,0,0);
        g.drawString(game.getResources().getString(R.string.crash_button_text),width/2,height/2,pButton);
    }
}
