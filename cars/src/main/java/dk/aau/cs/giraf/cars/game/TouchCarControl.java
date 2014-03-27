package dk.aau.cs.giraf.cars.game;

import android.util.Log;

import java.util.List;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Input;

public class TouchCarControl implements CarControl {
    private int lastMove = 0;

    public TouchCarControl() {
    }

    @Override
    public float getMove(Game game) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int width = game.getWidth();
        int height = (int) (game.getHeight() * .25);
        int bottomOffset = game.getHeight() - height;

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_DOWN) {
                if (inBounds(event, 0, 0, width, height)) {
                    lastMove = -1;
                    break;
                } else if (inBounds(event, 0, bottomOffset, width, height)) {
                    lastMove = 1;
                    break;
                }
            } else if (event.type == Input.TouchEvent.TOUCH_UP) {
                lastMove = 0;
                break;
            }
        }

        return lastMove;
    }

    @Override
    public void Reset()
    {
        lastMove=0;
    }

    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        return event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1;
    }
}
