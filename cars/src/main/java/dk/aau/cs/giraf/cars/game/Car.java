package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Image;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.framework.mFloat;

public class Car extends GameItem {
    public static final float MAX_PIXELSPERSECOND = 300f;
    public static final float MAX_SCALE = 10f;

    private Paint paint;
    boolean showValue = false;

    public void SetShowValue(boolean showValue) {
        this.showValue = showValue;
    }

    int color;
    Image image;

    public Car(float width, float height) {
        this(0, 0, width, height);
    }

    public Car(float x, float y, float height, float width) {
        super(x, y, height, width);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(46);
        paint.setTextAlign(Paint.Align.CENTER);

        this.color = Color.WHITE;
        this.image = Assets.GetCar();
    }

    public void setColor(int color) {
        this.color = color;
        this.image = Graphics.recolorImage(Assets.GetCar(), color);
    }

    public int getColor() {
        return this.color;
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        Rect bounds = this.GetBounds();

        graphics.drawScaledImage(image,
                bounds.left, bounds.top, bounds.right - bounds.left, bounds.bottom - bounds.top,
                0, 0, image.getWidth(), image.getHeight());
        if (showValue)
            graphics.drawString(String.valueOf(getBarometerNumber(bounds.centerY() - 100, 600)),
                    bounds.centerX() - 20, bounds.centerY() + 17, paint);
    }

    private int getBarometerNumber(float y, float height) {
        return Math.round(10 - (y / (height / 10)));
    }

    @Override
    public void Update(Input.TouchEvent[] touchEvents, float deltaTime) {

    }

    public Car ResetCar(float gameHeight, float grassSize, mFloat verticalMover) {
        x = -width;
        y = gameHeight - grassSize - height / 2;
        verticalMover.setCurrentValue(y);
        return this;
    }
}
