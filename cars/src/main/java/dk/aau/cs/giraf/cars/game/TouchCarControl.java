package dk.aau.cs.giraf.cars.game;

import android.util.Log;

import java.util.List;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Input;

public class TouchCarControl implements CarControl {
    private int lastMove = 0;
    private int pixelsPerSecond;

    public TouchCarControl(int pixelsPerSecond) {
        this.pixelsPerSecond = pixelsPerSecond;
    }

    @Override
    public float getMove(Game game) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_DOWN) {
                if (inBounds(event, 0, 0, 1280, 200)) {
                    lastMove = -1;
                    break;
                } else if (inBounds(event, 0, 600, 1280, 200)) {
                    lastMove = 1;
                    break;
                }
            } else if (event.type == Input.TouchEvent.TOUCH_UP) {
                lastMove = 0;
                break;
            }
        }

        return lastMove * pixelsPerSecond * (deltaTime / 100.0f);
    }

    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        return event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1;
    }
}
