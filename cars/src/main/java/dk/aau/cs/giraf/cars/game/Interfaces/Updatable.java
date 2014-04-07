package dk.aau.cs.giraf.cars.game.Interfaces;

import dk.aau.cs.giraf.cars.framework.Input;

public interface Updatable {
    public abstract void Update(Input.TouchEvent[] touchEvents, float deltaTime);
}
