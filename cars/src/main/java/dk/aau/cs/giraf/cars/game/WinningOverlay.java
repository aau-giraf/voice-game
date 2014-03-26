package dk.aau.cs.giraf.cars.game;

import android.graphics.Paint;

import java.util.List;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;

public class WinningOverlay implements Overlay {

    public WinningOverlay(){}

    public GameState ButtonPressed(Game game)
    {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int width = game.getWidth();
        int height = game.getHeight();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                if (inBounds(event, 0,height/2,width/2, height/2)) {
                        game.setScreen(new GameScreen(game, new TestObstacles()));
                        return GameState.Running;
                    }
                if (inBounds(event, width/2, height/2, width/2, height/2)) {
                    //Go to menu and garbagecollect game
                }
            }
        }
        return GameState.Won;
    }

    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height)
    {
        return event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1;
    }

    public void Draw(Game game, Paint paint)
    {
        int width = game.getWidth();
        int height = game.getHeight();
        Graphics g = game.getGraphics();
        g.drawARGB(155, 0, 0, 0);
        g.drawImage(Assets.GetTrophy(), (int)(width*.50) - Assets.GetTrophy().getWidth()/2, (int)(height*.25) - Assets.GetTrophy().getHeight()/2);
        g.drawString("Spil igen", (int)(width*.25), (int)(height*.85), paint);
        g.drawString("Menu", (int)(width*.75), (int)(height*.85), paint);
    }
}
