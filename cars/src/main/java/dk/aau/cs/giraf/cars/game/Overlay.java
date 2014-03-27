package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Paint;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Input;

public abstract class Overlay {

    protected Paint pButton;
    protected Paint pFocus;

    public Overlay() {
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

    public abstract void Draw(Game game);

    protected boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height)
    {
        return event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1;
    }
}
