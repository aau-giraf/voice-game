package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Image;

public class Garage extends GameItem {

    private enum GarageState { Open, Closing, Closed };

    private GarageState currentState;
    private final float closingTimeInMs = 3000;
    private float closingWait = 0;

    private int doorLength;
    private final int START_ANGLE = 45;
    private final int HINGE_SIZE = 5;

    int color;
    Image image;

    public Garage(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.currentState = GarageState.Open;

        this.color = Color.WHITE;
        this.image = Assets.GetGarage();

        this.x = this.x + this.width/2;

        this.doorLength = (int)height/2 - HINGE_SIZE;
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

        Point topDoorHinge = new Point(bounds.left + HINGE_SIZE, bounds.top + HINGE_SIZE);
        Point bottomDoorHinge = new Point(bounds.left + HINGE_SIZE, bounds.bottom - HINGE_SIZE);

        int topAngle = 180 + START_ANGLE;
        int bottomAngle = 180 - START_ANGLE;

        if (currentState == GarageState.Closing) {
            topAngle -= closingWait / closingTimeInMs * (90 + START_ANGLE);
            bottomAngle += closingWait / closingTimeInMs * (90 + START_ANGLE);
        }
        else if (currentState == GarageState.Closed) {
            topAngle -= 90 + START_ANGLE;
            bottomAngle += 90 + START_ANGLE;
        }

        Point topDoorEnd = GetCircumferencePoint(topDoorHinge, doorLength, topAngle);
        Point bottomDorEnd = GetCircumferencePoint(bottomDoorHinge, doorLength, bottomAngle);

        graphics.drawLine(
                topDoorHinge.x,
                topDoorHinge.y,
                topDoorEnd.x,
                topDoorEnd.y,
                this.color,
                5
        );

        graphics.drawLine(
                bottomDoorHinge.x,
                bottomDoorHinge.y,
                bottomDorEnd.x,
                bottomDorEnd.y,
                this.color,
                5
        );
    }

    public void Close() {
        if(currentState == GarageState.Open) {
            currentState = GarageState.Closing;
            closingWait = 0;
        }
    }

    public boolean getIsClosing() { return currentState == GarageState.Closing; }

    public boolean getIsClosed(){
        return currentState == GarageState.Closed;
    }

    @Override
    public void Update(float deltaTime) {
        if (currentState == GarageState.Closing) {
            closingWait += deltaTime;
            if (closingWait >= closingTimeInMs)
                currentState = GarageState.Closed;
        }
    }

    private Point GetCircumferencePoint(Point center, int radius, int angle) {
        int x = (int)(center.x + radius * Math.cos(Math.toRadians(angle)));
        int y = (int)(center.y + radius * Math.sin(Math.toRadians(angle)));

        return new Point(x, y);
    }
}
