package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.Assets;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;

public class Obstacle extends RoadItem {

    public Obstacle(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        drawRoadItem(graphics, Assets.GetObstacle());
    }

    @Override
    public void Update(Input.TouchEvent[] touchEvents, float deltaTime) {

    }
}
