package dk.aau.cs.giraf.cars.Game.Controller;

import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.cars.Interfaces.CarControl;


public class TouchCarControl implements CarControl {
    private float lastMove;
    private final float height;
    private final float offset;

    public TouchCarControl(int height, int offset) {
        this.height = height;
        this.offset = offset;
        lastMove = 0;
    }

    public float getMove(Input.TouchEvent[] touchEvents) {
        for (Input.TouchEvent e : touchEvents)
            if (e.type == Input.TouchEvent.TOUCH_DOWN || e.type == Input.TouchEvent.TOUCH_DRAGGED)
                lastMove = 1f - ((e.y - offset) / height);
            else if (e.type == Input.TouchEvent.TOUCH_UP)
                lastMove = 0f;

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
