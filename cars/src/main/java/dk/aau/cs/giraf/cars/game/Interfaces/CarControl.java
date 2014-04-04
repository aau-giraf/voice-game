package dk.aau.cs.giraf.cars.game.Interfaces;

import dk.aau.cs.giraf.cars.framework.Game;

public interface CarControl {
    public float getMove(Game game);
    public void Reset();
}
