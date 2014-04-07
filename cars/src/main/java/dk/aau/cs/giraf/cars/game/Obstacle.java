package dk.aau.cs.giraf.cars.game;

import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;

public class Obstacle extends GameItem {
    public Obstacle(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        Rect bounds = this.GetBounds();

        graphics.drawScaledImage(Assets.GetObstacle(),
                bounds.left, bounds.top, bounds.right - bounds.left, bounds.bottom - bounds.top,
                0, 0, Assets.GetObstacle().getWidth(), Assets.GetObstacle().getHeight());
    }

    @Override
    public void Update(Input.TouchEvent[] touchEvents, float deltaTime) {

    }
}
