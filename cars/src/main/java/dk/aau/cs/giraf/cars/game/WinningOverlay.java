package dk.aau.cs.giraf.cars.game;

import android.graphics.Paint;

import java.util.List;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;

public class WinningOverlay {

    public WinningOverlay(){}

    public GameState ButtonPressed(Game game)
    {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                if (inBounds(event, 0, 0, 800, 240)) {

                    if (!inBounds(event, 0, 0, 35, 35)) {
                        game.setScreen(new GameScreen(game, new TestObstacles()));
                        return GameState.Running;
                    }
                }

                if (inBounds(event, 0, 240, 800, 240)) {
                    //nullify();
                    //goToMenu();
                }
            }
        }
        return GameState.Won;
    }

    private boolean inBounds(Input.TouchEvent event, int x, int y, int width,
                             int height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y
                && event.y < y + height - 1)
            return true;
        else
            return false;
    }

    public void Draw(Game game, Paint paint)
    {
        Graphics g = game.getGraphics();
        g.drawARGB(155, 0, 0, 0);
        g.drawString("Spil igen", 400, 165, paint);
        g.drawString("Menu", 400, 360, paint);
    }
}
