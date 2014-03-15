package dk.aau.cs.giraf.cars.game;

import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Graphics;

public class Car extends GameItem {
    public Car(float x, float y, float height, float width) {
        super(x, y, height, width);
    }

    @Override
    public void Paint(Graphics graphics, float deltaTime) {
        Rect bounds = this.GetBounds();

        graphics.drawScaledImage(Assets.GetCar(),
                bounds.left, bounds.top, bounds.right - bounds.left, bounds.bottom - bounds.top,
                0, 0, Assets.GetCar().getWidth(), Assets.GetCar().getHeight());
    }

    @Override
    public void Update(float deltaTime) {

    }
}
