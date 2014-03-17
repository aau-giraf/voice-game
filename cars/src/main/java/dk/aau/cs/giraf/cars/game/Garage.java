package dk.aau.cs.giraf.cars.game;

import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Graphics;

public class Garage extends GameItem {
    private int partWidth;

    public Garage(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.partWidth = Assets.getGarage().getWidth() / 3;
    }

    @Override
    public void Paint(Graphics graphics, float deltaTime) {
        Rect bounds = this.GetBounds();

        graphics.drawScaledImage(Assets.getGarage(),
                bounds.left, bounds.top, bounds.right - bounds.left, bounds.bottom - bounds.top,
                0, 0, partWidth, Assets.getGarage().getHeight());
    }

    @Override
    public void Update(float deltaTime) {

    }
}
