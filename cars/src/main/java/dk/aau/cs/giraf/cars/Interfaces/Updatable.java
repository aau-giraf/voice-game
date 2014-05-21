package dk.aau.cs.giraf.cars.Interfaces;

import dk.aau.cs.giraf.game_framework.Input;

public interface Updatable {
    public abstract void Update(Input.TouchEvent[] touchEvents, float deltaTime);
}
