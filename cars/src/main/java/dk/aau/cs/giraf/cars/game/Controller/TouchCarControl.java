package dk.aau.cs.giraf.cars.game.Controller;

import android.util.Log;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Car;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;


public class TouchCarControl implements CarControl {
    private int lastMove;
    private int height;


    public TouchCarControl(int height) {

        this.height = height;
        lastMove = height;
    }


    public float getMove(Input.TouchEvent[] touchEvents) {

        for (Input.TouchEvent e : touchEvents)
            if (e.type == Input.TouchEvent.TOUCH_DOWN || e.type == Input.TouchEvent.TOUCH_DRAGGED)
                lastMove = e.y;
            else if (e.type == Input.TouchEvent.TOUCH_UP)
                lastMove = height;

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

    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        return event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1;
    }
}
