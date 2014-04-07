package dk.aau.cs.giraf.cars.game.Interfaces;

import dk.aau.cs.giraf.cars.framework.Input;

public interface CarControl {
    public float getMove(Input.TouchEvent[] touchEvents);
    public void Reset();
}
