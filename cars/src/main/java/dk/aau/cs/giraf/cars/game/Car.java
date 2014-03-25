package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Image;

public class Car extends GameItem {
    int color;
    Image image;

    public Car(float width, float height)
    {
        this(0, 0, width, height);
    }
    public Car(float x, float y, float height, float width) {
        super(x, y, height, width);
        this.image = Assets.GetCar();
    }

    public void setColor(Graphics graphics, int color) {
        this.color = color;
        this.image = graphics.recolorImage(Assets.GetCar(), color);
    }

    @Override
    public void Paint(Graphics graphics, float deltaTime) {
        Rect bounds = this.GetBounds();

        graphics.drawScaledImage(image,
                bounds.left, bounds.top, bounds.right - bounds.left, bounds.bottom - bounds.top,
                0, 0, Assets.GetCar().getWidth(), Assets.GetCar().getHeight());
    }

    @Override
    public void Update(float deltaTime) {

    }
}
