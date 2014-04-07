package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Image;
import dk.aau.cs.giraf.cars.framework.Input;

public class Car extends GameItem {
    int color;
    Image image;

    public Car(float width, float height)
    {
        this(0, 0, width, height);
    }
    public Car(float x, float y, float height, float width) {
        super(x, y, height, width);

        this.color = Color.WHITE;
        this.image = Assets.GetCar();
    }

    public void setColor(int color) {
        this.color = color;
        this.image = Graphics.recolorImage(Assets.GetCar(), color);
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        Rect bounds = this.GetBounds();

        graphics.drawScaledImage(image,
                bounds.left, bounds.top, bounds.right - bounds.left, bounds.bottom - bounds.top,
                0, 0, image.getWidth(), image.getHeight());
    }

    @Override
    public void Update(Input.TouchEvent[] touchEvents, float deltaTime) {

    }
}
