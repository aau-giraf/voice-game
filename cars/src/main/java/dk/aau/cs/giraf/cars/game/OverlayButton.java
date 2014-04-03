package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Paint;
import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Interfaces.GameObject;

public class OverlayButton implements GameObject{

    int TouchX, TouchY, TouchWidth, TouchHeight;
    int DrawX, DrawY;
    boolean Pressed;
    String buttonText;
    protected Paint pButton;
    protected Paint pFocus;
    Game game;

    public OverlayButton(Game game, int touchX, int touchY, int touchWidth, int touchHeight, int drawX, int drawY, int textColor, int touchColor, String buttonText) {
        pButton = new Paint();
        pFocus = new Paint();

        this.game = game;

        this.buttonText = buttonText;

        pButton.setTextSize(100);
        pButton.setTextAlign(Paint.Align.CENTER);
        pButton.setAntiAlias(true);
        pButton.setColor(textColor);

        pFocus.setTextSize(100);
        pFocus.setTextAlign(Paint.Align.CENTER);
        pFocus.setAntiAlias(true);
        pFocus.setColor(touchColor);

        this.TouchX = touchX;
        this.TouchY = touchY;
        this.TouchWidth = touchWidth;
        this.TouchHeight = touchHeight;

        this.DrawX = drawX;
        this.DrawY = drawY;

        this.Pressed = false;
    }

    /**
     * Create a Overlaybutton with the default colors (White text, yellow when touched)
     * @param touchX
     * @param touchY
     * @param touchWidth
     * @param touchHeight
     * @param drawX
     * @param drawY
     * @param buttonText
     */
    public OverlayButton(Game game,int touchX, int touchY, int touchWidth, int touchHeight, int drawX, int drawY, String buttonText)
    {
           this(game,touchX,touchY,touchWidth,touchHeight,drawX,drawY,Color.WHITE,Color.YELLOW,buttonText);
    }



    /**
     * Updates the value of the Pressed variable so it is true when the button is touched
     */
    @Override
    public void Update(float deltaTime) {
        Input.TouchEvent[] touchEvents =game.getTouchEvents();

        for (int i = 0; i < touchEvents.length; i++) {
            Input.TouchEvent event = touchEvents[i];
            if (inBounds(event, this)) {
                if (event.type == Input.TouchEvent.TOUCH_UP) {
                    Pressed = false;
                }
                else if (event.type == Input.TouchEvent.TOUCH_DOWN || event.type == Input.TouchEvent.TOUCH_DRAGGED)
                    Pressed = true;
                else
                    Pressed = false;
            }
            else
                Pressed = false;
        }
    }

    protected boolean IsButtonPressed(Input.TouchEvent[] touchEvents) {
        for (int i = 0; i < touchEvents.length; i++) {
            Input.TouchEvent event = touchEvents[i];
            if (inBounds(event, this)) {
                if (event.type == Input.TouchEvent.TOUCH_UP) {
                    Pressed = false;
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height)
    {
        return event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1;
    }

    protected boolean inBounds(Input.TouchEvent event, OverlayButton button) {
        return inBounds(event, button.TouchX, button.TouchY, button.TouchWidth, button.TouchHeight);
    }

    @Override
    public void Draw(Graphics g, float deltaTime) {
        g.drawString(buttonText, DrawX, DrawY, Pressed ? pFocus : pButton);
    }
}
