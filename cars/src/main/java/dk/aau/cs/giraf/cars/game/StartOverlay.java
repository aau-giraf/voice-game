package dk.aau.cs.giraf.cars.game;

import android.graphics.Paint;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;

public class StartOverlay extends Overlay {
    private float counterInMS;
    private float visualCounter;
    public StartOverlay(int seconds)
    {
        super();

        counterInMS = (seconds+1)*1000;
        visualCounter = seconds;
    }

    public GameState UpdateTime(float deltaTime)
    {
        counterInMS -= deltaTime;
        if (counterInMS<=0)
            return GameState.Running;
        if (counterInMS<visualCounter*1000)
            visualCounter--;
        return GameState.Starting;
    }

    @Override
    public GameState ButtonPressed(Game game) {
        return null;
    }

    public void Draw(Game game)
    {
        int width = game.getWidth();
        int height = game.getHeight();
        Graphics g = game.getGraphics();
        g.drawARGB(155,0,0,0);
        String out;
        out = visualCounter == 0 ? game.getResources().getString(R.string.countdown_drive) : (int)visualCounter+"";
        g.drawString(out, width/2, height/2, pButton);
    }
}
