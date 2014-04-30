package dk.aau.cs.giraf.cars.game.Controller;

import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;


public class TouchCarControl implements CarControl {
    private int lastMove;
    private final int height;
    private final int offset;

    public TouchCarControl(int height, int offset) {
        this.height = height;
        this.offset = offset;
        lastMove = height;
    }

    public float getMove(Input.TouchEvent[] touchEvents) {
        for (Input.TouchEvent e : touchEvents)
            if (e.type == Input.TouchEvent.TOUCH_DOWN || e.type == Input.TouchEvent.TOUCH_DRAGGED)
                lastMove = 1 - ((e.y - offset) / height);
            else if (e.type == Input.TouchEvent.TOUCH_UP)
                lastMove = 0;

        return lastMove;
    }

    @Override
    public void Reset() {
        lastMove = height;
    }

    @Override
    public int getBarometerNumber(float y, float height) {
        return Math.round(10 - (y / (height / 10)));
    }
}
