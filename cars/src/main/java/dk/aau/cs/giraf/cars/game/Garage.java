package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Image;

public class Garage extends GameItem {
    private int partWidth;
    private int drawnPart;
    private final float closingTime = 1000;
    private float closingWait = 0;

    int color;
    Image image;

    public Garage(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.partWidth = Assets.getGarage().getWidth() / 3;
        this.drawnPart = 0;

        this.color = Color.WHITE;
        this.image = Assets.getGarage();
    }

    public void setColor(int color) {
        this.color = color;
        image = Graphics.recolorImage(Assets.getGarage(), color);
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

    public boolean isClosing () { return drawnPart == 1; }

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
