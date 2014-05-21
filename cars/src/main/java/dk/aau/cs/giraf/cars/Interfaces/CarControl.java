package dk.aau.cs.giraf.cars.Interfaces;

import dk.aau.cs.giraf.game_framework.Input;

public interface CarControl {
    public float getMove(Input.TouchEvent[] touchEvents);
    public int getBarometerNumber(float y, float height);
    public void Reset();
}
