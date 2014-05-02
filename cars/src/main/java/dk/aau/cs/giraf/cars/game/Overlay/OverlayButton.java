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

    protected Paint pButton;
    protected Paint pFocus;
    int x, y;
    boolean Pressed;

    String buttonText;
    Rect bounds;

    public OverlayButton(int X, int Y, int textColor, int touchColor, String buttonText, Paint.Align alignment, float textSize) {
        pButton = new Paint();
        pFocus = new Paint();

        pButton.setTextSize(textSize);
        pButton.setTextAlign(alignment);
        pButton.setAntiAlias(true);
        pButton.setColor(textColor);

        pFocus.setTextSize(textSize);
        pFocus.setTextAlign(alignment);
        pFocus.setAntiAlias(true);
        pFocus.setColor(touchColor);

        this.x = X;
        this.y = Y;

        bounds = new Rect();
        pButton.getTextBounds(buttonText, 0, buttonText.length(), bounds);

        int xoffset = 0;

        switch (alignment) {
            case CENTER:
                xoffset = bounds.width() / 2;
                break;
            case LEFT:
                xoffset = 0;
                break;
            case RIGHT:
                xoffset = bounds.width();
                break;
        }
        bounds.offset(x -xoffset, y);


        this.buttonText = buttonText;

        this.Pressed = false;
    }

    public boolean IsPressed()
    {
        return Pressed;
    }

    /**
     * Create a Overlaybutton with the default colors and alignment(White text, yellow when touched)
     *
     * @param x
     * @param y
     * @param buttonText
     */
    public OverlayButton(int x, int y, String buttonText) {
        this(x, y, Color.WHITE, Color.YELLOW, buttonText, Paint.Align.CENTER, 100);
    }

    public OverlayButton(int x, int y, String buttonText, float textSize){
        this(x, y, Color.WHITE, Color.YELLOW, buttonText, Paint.Align.CENTER, textSize);
    }

    public OverlayButton(int x, int y, int textColor, int touchColor, String buttonText, Paint.Align alignment){
        this(x, y, textColor, touchColor, buttonText, alignment, 100);
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

    protected boolean inBounds(Input.TouchEvent event) {
        return event.inBounds( bounds);
    }

    @Override
    public void Draw(Graphics g, float deltaTime) {
        g.drawString(buttonText, x, y, Pressed ? pFocus : pButton);

    }
}
