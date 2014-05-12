package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Image;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.framework.MoveSineLine;
import dk.aau.cs.giraf.cars.framework.mFloat;

public class Car extends GameItem {
    private static final float MAX_PIXELSPERSECOND = 300f;
    private static final float MAX_SCALE = 10f;

    private Paint paint;
    private boolean driving = true;
    private mFloat verticalMover;
    private boolean showValue = false;

    private final float initialX, initialY;

    public void setShowValue(boolean showValue) {
        this.showValue = showValue;
    }

    private int color;
    private Image image;

    public Car(float x, float y, float speed) {
        super(x, y, 200, 99);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(46);
        paint.setTextAlign(Paint.Align.CENTER);

        this.color = Color.WHITE;
        this.image = Assets.GetCar();

        this.verticalMover = new mFloat(0, new MoveSineLine(0.5f, 200));

        this.initialX = x;
        this.initialY = y;
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
        if (driving) {
            Rect bounds = this.GetBounds();

            graphics.drawScaledImage(image,
                    bounds.left, bounds.top, bounds.right - bounds.left, bounds.bottom - bounds.top,
                    0, 0, image.getWidth(), image.getHeight());
            if (showValue)
                graphics.drawString(String.valueOf(getBarometerNumber(bounds.centerY() - 100, 600)),
                        bounds.centerX() - 20, bounds.centerY() + 17, paint);
        }
    }

    private int getBarometerNumber(float y, float height) {
        return Math.round(10 - (y / (height / 10)));
    }

    public void setVerticalTarget(float target) {
        if (target != verticalMover.getTargetValue())
            verticalMover.setTargetValue(target);
    }

    @Override
    public void Update(Input.TouchEvent[] touchEvents, float deltaTime) {
        if (driving) {
            verticalMover.Update();
            super.y = verticalMover.getCurrentValue();
        }
    }

    public Car ResetCar() {
        super.x = initialX;
        super.y = initialY;
        verticalMover.setCurrentValue(initialY);
        return this;
    }

    public void Stop() {
        driving = false;
    }

    public void Start() {
        driving = true;
    }
}
