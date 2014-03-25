package dk.aau.cs.giraf.cars.game;

import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Image;

public class Garage extends GameItem {
    private int partWidth;
    private int drawnPart;
    private final float closingTime = 100;
    private float closingWait = 0;

    int color;
    Image image;

    public Garage(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.partWidth = Assets.getGarage().getWidth() / 3;
        this.drawnPart = 0;

        this.color = color;
        this.image = Assets.getGarage();
    }

    public void setColor(Graphics graphics, int color) {
        image = graphics.recolorImage(Assets.getGarage(), color);
    }

    @Override
    public void Paint(Graphics graphics, float deltaTime) {
        Rect bounds = this.GetBounds();

        graphics.drawScaledImage(image,
                bounds.left, bounds.top, bounds.right - bounds.left, bounds.bottom - bounds.top,
                drawnPart * partWidth, 0, partWidth, Assets.getGarage().getHeight());
    }

    public void Close() {
        if(drawnPart == 0) {
            drawnPart = 1;
            closingWait = 0;
        }
    }

    public boolean getIsClosed(){
        return drawnPart == 2;
    }

    @Override
    public void Update(float deltaTime) {
        if (drawnPart == 1) {
            closingWait += deltaTime;
            if (closingWait >= closingTime)
                drawnPart = 2;
        }
    }
}
