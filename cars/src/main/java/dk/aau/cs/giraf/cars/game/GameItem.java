package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.implementation.AndroidGraphics;

public abstract class GameItem {
    public abstract void Paint(AndroidGraphics graphics, float deltaTime);
    public abstract void Update(float deltaTime);
}
