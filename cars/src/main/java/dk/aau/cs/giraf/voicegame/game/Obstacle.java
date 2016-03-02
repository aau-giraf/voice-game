package dk.aau.cs.giraf.voicegame.game;

import dk.aau.cs.giraf.voicegame.Assets;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;
// inheritence order: GameObject -> GameItem -> RoadItem -> Obstacle
public class Obstacle extends RoadItem {

    public Obstacle(float x, float y, float width, float height) {
        // TODO remove -2
        super(x, y, width, height, -2);
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        drawRoadItem(graphics, Assets.GetObstacle());
    }

    @Override
    public void Update(Input.TouchEvent[] touchEvents, float deltaTime) {

    }
}
