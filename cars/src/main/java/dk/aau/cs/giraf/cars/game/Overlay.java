package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Paint;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Input;

public abstract class Overlay {

    protected Paint pButton;
    protected Paint pFocus;

    public Overlay(Game game) {
        pButton = new Paint();
        pFocus = new Paint();

        pButton.setTextSize(100);
        pButton.setTextAlign(Paint.Align.CENTER);
        pButton.setAntiAlias(true);
        pButton.setColor(Color.WHITE);

        pFocus.setTextSize(100);
        pFocus.setTextAlign(Paint.Align.CENTER);
        pFocus.setAntiAlias(true);
        pFocus.setColor(Color.YELLOW);
    }

    protected boolean IsButtonPressed(Input.TouchEvent[] touchEvents, OverlayButton button) {
        for (int i = 0; i < touchEvents.length; i++) {
            Input.TouchEvent event = touchEvents[i];
            if (inBounds(event, button)) {
                if (event.type == Input.TouchEvent.TOUCH_UP) {
                    button.Pressed = false;
                    return true;
                }
                else if (event.type == Input.TouchEvent.TOUCH_DOWN || event.type == Input.TouchEvent.TOUCH_DRAGGED)
                    button.Pressed = true;
                else
                    button.Pressed = false;
            }
            else
                button.Pressed = false;
        }

        return false;
    }

    public abstract void Draw(Game game);

    protected boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height)
    {
        return event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1;
    }

    protected boolean inBounds(Input.TouchEvent event, OverlayButton button) {
        return inBounds(event, button.TouchX, button.TouchY, button.TouchWidth, button.TouchHeight);
    }
}
