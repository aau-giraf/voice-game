package dk.aau.cs.giraf.cars.game.Controller;

import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;

public class TouchCarControl implements CarControl {
    private int lastMove = 0;
    private int width, height, bottomOffset;

    public TouchCarControl(int gameWidth, int gameHeight) {
        this.width = gameWidth;
        this.height = (int)(gameHeight * 0.25);
        this.bottomOffset = gameHeight - this.height;
    }

    @Override
    public float getMove(Input.TouchEvent[] touchEvents) {
        int len = touchEvents.length;
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents[i];
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
    public void Reset() {
        lastMove = 0;
    }

    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        return event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1;
    }
}
