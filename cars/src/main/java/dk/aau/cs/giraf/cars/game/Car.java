package dk.aau.cs.giraf.cars.game;

import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.implementation.AndroidGraphics;

public class Car extends GameItem {
    public Car(Rect bounds) {
        super(bounds);
    }

    @Override
    public void Paint(AndroidGraphics graphics, float deltaTime) {
        Rect bounds = this.GetBounds();

        graphics.drawScaledImage(Assets.GetCar(),
                bounds.left, bounds.top, bounds.right-bounds.left, bounds.bottom - bounds.top,
                0, 0, Assets.GetCar().getWidth(), Assets.GetCar().getHeight());
    }

    @Override
    public void Update(float deltaTime) {

    }
}
