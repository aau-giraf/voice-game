package dk.aau.cs.giraf.voicegame.game.GameScreens;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;

public class OverlayButton extends OverlayText {


    private boolean pressed;
    private boolean clicked;

    private int buttonMargin;

    private Rect bounds;

    public OverlayButton(int X, int Y, int textColor, String buttonText, Paint.Align alignment, float textSize) {
        super(X, Y, textColor, buttonText, alignment, textSize);


        bounds = new Rect();

        //Calculates the margin from an M in order to have consistent margins on all text
        pButton.getTextBounds("S", 0, 1, bounds);
        buttonMargin = bounds.height() / 2;

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

        bounds = new Rect(bounds.left - buttonMargin, bounds.top - buttonMargin, bounds.right + buttonMargin, bounds.top + 3 * buttonMargin);
        bounds.offset(x - xoffset, y);
        this.pressed = false;
        this.clicked = false;
    }

    public OverlayButton(int x, int y, String buttonText) {
        this(x, y, Color.WHITE, buttonText, Paint.Align.CENTER, 75);
    }

    public OverlayButton(int x, int y, String buttonText, float textSize) {
        this(x, y, Color.WHITE, buttonText, Paint.Align.CENTER, textSize);
    }

    public OverlayButton(int x, int y, int textColor, String buttonText, Paint.Align alignment) {
        this(x, y, textColor, buttonText, alignment, 100);
    }

    public boolean IsPressed() {
        return pressed;
    }

    public boolean IsClicked() {
        return clicked;
    }

    @Override
    public void Update(Input.TouchEvent[] touchEvents, float deltaTime) {
        boolean clickedset = false;
        for (int i = 0; i < touchEvents.length; i++) {
            Input.TouchEvent event = touchEvents[i];
            if (inBounds(event)) {
                if (event.type == Input.TouchEvent.TOUCH_UP) {
                    clickedset = true;
                    clicked = pressed;
                    pressed = false;
                } else if (event.type == Input.TouchEvent.TOUCH_DOWN || event.type == Input.TouchEvent.TOUCH_DRAGGED)
                    pressed = true;
                else
                    pressed = false;
            } else
                pressed = false;
        }
        if (!clickedset)
            clicked = false;
    }

    protected boolean inBounds(Input.TouchEvent event) {
        return event.inBounds(bounds);
    }

    @Override
    public void Draw(Graphics g, float deltaTime) {
        g.drawBorder(bounds.left, bounds.top, bounds.width(), bounds.height(), Color.WHITE);
        g.drawRect(bounds.left, bounds.top, bounds.width(), bounds.height(), pressed ? Color.GRAY : Color.DKGRAY);
        g.drawString(buttonText, x, y, pButton);
    }

}
