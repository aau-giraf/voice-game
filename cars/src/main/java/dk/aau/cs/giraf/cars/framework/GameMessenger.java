package dk.aau.cs.giraf.cars.framework;

import android.graphics.Point;

public abstract class GameMessenger {
    abstract void setTouchEvents(Input.TouchEvent[] touchEvents);
    abstract void setSize(int width, int height);
}
