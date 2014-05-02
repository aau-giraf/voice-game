package dk.aau.cs.giraf.cars.game.Interfaces;

import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.GameState;

public interface UpdatableGameState {
    public abstract GameState Update(Input.TouchEvent[] touchEvents, float deltaTime);
}
