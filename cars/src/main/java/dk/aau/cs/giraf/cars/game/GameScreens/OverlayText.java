package dk.aau.cs.giraf.cars.game.GameScreens;

import android.graphics.Color;
import android.graphics.Paint;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.cars.Interfaces.GameObject;

public class OverlayText implements GameObject {
    protected Paint pButton;

    protected int x, y;


    protected String buttonText;


    public OverlayText(int X, int Y, int textColor, String buttonText, Paint.Align alignment, float textSize) {
        pButton = new Paint();

        pButton.setTextSize(textSize);
        pButton.setTextAlign(alignment);
        pButton.setAntiAlias(true);
        pButton.setColor(textColor);


        this.x = X;
        this.y = Y;


        this.buttonText = buttonText;

    }

    /**
     * Create a Overlaybutton with the default colors and alignment(White text, yellow when touched)
     *
     * @param x
     * @param y
     * @param buttonText
     */
    public OverlayText(int x, int y, String buttonText) {
        this(x, y, Color.WHITE, buttonText, Paint.Align.CENTER, 100);
    }

    public OverlayText(int x, int y, String buttonText, float textSize) {
        this(x, y, Color.WHITE, buttonText, Paint.Align.CENTER, textSize);
    }

    public OverlayText(int x, int y, int textColor, int touchColor, String buttonText, Paint.Align alignment) {
        this(x, y, textColor, buttonText, alignment, 100);
    }

    @Override
    public void Update(Input.TouchEvent[] touchEvents, float deltaTime) {
    }

    @Override
    public void Draw(Graphics g, float deltaTime) {
        g.drawString(buttonText, x, y, pButton);

    }

}
