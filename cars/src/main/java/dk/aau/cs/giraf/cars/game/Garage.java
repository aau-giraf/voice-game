package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Image;

public class Garage extends GameItem {
    private int drawnPart;
    private final float closingTime = 1000;
    private float closingWait = 0;

    int color;
    Image image;

    public Garage(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.drawnPart = 0;

        this.color = Color.WHITE;
        this.image = Assets.GetGarage();

        this.x = this.x + this.width/2;
    }

    public void setColor(int color) {
        this.color = color;
        image = Graphics.recolorImage(Assets.GetGarage(), color);
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        Rect bounds = this.GetBounds();

        graphics.drawScaledImage(image,
                bounds.left, bounds.top, bounds.right - bounds.left, bounds.bottom - bounds.top,
                0, 0, Assets.GetGarage().getWidth(), Assets.GetGarage().getHeight());

        graphics.drawLine(bounds.left+5, bounds.top+5, bounds.left - bounds.height()/2, bounds.top-45,this.color,5);
        graphics.drawLine(bounds.left+5, bounds.bottom-5, bounds.left - bounds.height()/2, bounds.bottom+45,this.color,5);
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
