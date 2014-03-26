package dk.aau.cs.giraf.cars.game;

import android.graphics.Paint;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;

public class StartOverlay implements Overlay {
    private float counterInMS;
    private float visualCounter;
    public StartOverlay(int seconds)
    {
        counterInMS = seconds*1000;
        visualCounter = seconds;
    }

    public GameState UpdateTime(float deltaTime)
    {
        counterInMS -= deltaTime;
        if (counterInMS<=0)
            return GameState.Running;
        if (counterInMS<(visualCounter-1)*1000)
            visualCounter--;
        return GameState.Starting;
    }

    public void Draw(Game game, Paint paint)
    {
        int width = game.getWidth();
        int height = game.getHeight();
        Graphics g = game.getGraphics();
        g.drawString(visualCounter+"", width/2, height/2, paint);
    }
}
