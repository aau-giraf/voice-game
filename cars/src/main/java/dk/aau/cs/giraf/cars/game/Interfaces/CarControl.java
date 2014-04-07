package dk.aau.cs.giraf.cars.game.Interfaces;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.game.Car;

public interface CarControl {
    public float getMove(Game game, Car car);
    public int getBarometerNumber(float y, float height);
    public void Reset();
}
