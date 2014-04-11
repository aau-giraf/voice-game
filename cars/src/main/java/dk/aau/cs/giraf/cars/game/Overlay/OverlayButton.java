package dk.aau.cs.giraf.cars.game.Overlay;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Interfaces.GameObject;

public class OverlayButton implements GameObject {

    int x, y;

    boolean Pressed;
    String buttonText;
    protected Paint pButton;
    protected Paint pFocus;
    Rect bounds;

    public OverlayButton(int X, int Y, int textColor, int touchColor, String buttonText,Paint.Align alignment ) {
        pButton = new Paint();
        pFocus = new Paint();

        pButton.setTextSize(100);
        pButton.setTextAlign(alignment);
        pButton.setAntiAlias(true);
        pButton.setColor(textColor);

        pFocus.setTextSize(100);
        pFocus.setTextAlign(alignment);
        pFocus.setAntiAlias(true);
        pFocus.setColor(touchColor);

        this.x = X;
        this.y = Y;

        bounds = new Rect();
        pButton.getTextBounds(buttonText, 0, buttonText.length(), bounds);
        bounds.offset(x - bounds.width()/2, y);

        this.buttonText = buttonText;

        this.Pressed = false;
    }

    /**
     * Create a Overlaybutton with the default colors and alignment(White text, yellow when touched)
     *
     * @param x
     * @param y
     * @param buttonText
     */
    public OverlayButton(int x, int y, String buttonText) {
        this(x, y, Color.WHITE, Color.YELLOW, buttonText,Paint.Align.LEFT);
    }


    /**
     * Updates the value of the Pressed variable so it is true when the button is touched
     */
    @Override
    public void Update(Input.TouchEvent[] touchEvents, float deltaTime) {
        for (int i = 0; i < touchEvents.length; i++) {
            Input.TouchEvent event = touchEvents[i];
            if (inBounds(event)) {
                if (event.type == Input.TouchEvent.TOUCH_UP) {
                    Pressed = false;
                } else if (event.type == Input.TouchEvent.TOUCH_DOWN || event.type == Input.TouchEvent.TOUCH_DRAGGED)
                    Pressed = true;
                else
                    Pressed = false;
            } else
                Pressed = false;
        }
    }

    public boolean IsButtonPressed(Input.TouchEvent[] touchEvents) {
        for (int i = 0; i < touchEvents.length; i++) {
            Input.TouchEvent event = touchEvents[i];
            if (inBounds(event)) {
                if (event.type == Input.TouchEvent.TOUCH_UP) {
                    Pressed = false;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean IsButtonHeld(Input.TouchEvent[] touchEvents) {
        for (int i = 0; i < touchEvents.length; i++) {
            Input.TouchEvent event = touchEvents[i];
            if (inBounds(event)) {
                if (event.type == Input.TouchEvent.TOUCH_DOWN || event.type == Input.TouchEvent.TOUCH_DRAGGED) {
                    Pressed = false;
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean inBounds(Input.TouchEvent event, Rect bounds) {
        //return event.x > bounds.left && event.x < bounds.right && event.y > bounds.bottom && event.y < bounds.bottom;
        return bounds.contains(event.x, event.y);

    }

    protected boolean inBounds(Input.TouchEvent event) {
        return inBounds(event, bounds);
    }

    @Override
    public void Draw(Graphics g, float deltaTime) {
        g.drawString(buttonText, x, y, Pressed ? pFocus : pButton);

    }
}
