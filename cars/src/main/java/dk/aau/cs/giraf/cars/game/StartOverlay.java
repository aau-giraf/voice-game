package dk.aau.cs.giraf.cars.game;

import android.graphics.Paint;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;

public class StartOverlay implements Overlay {
    public StartOverlay(){}

    public void Draw(Game game, Paint paint)
    {
        int width = game.getWidth();
        int height = game.getHeight();
        Graphics g = game.getGraphics();
        int i;
        for (i=3;i>0;i--)
        {
            g.drawString(i+"",width/2-10,height/2-10,paint);
        }
    }
}
