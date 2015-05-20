package dk.aau.cs.giraf.voicegame.game;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import dk.aau.cs.giraf.voicegame.Assets;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Image;
import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.game_framework.MoveSineLine;
import dk.aau.cs.giraf.game_framework.mFloat;

public class Car extends GameItem {
    public static final float MAX_PIXELSPERSECOND = 300f;
    public static final float MAX_SCALE = 10f;

    private Paint paint;
    private mFloat verticalMover;
    private float scaleSpeed;
    private float pixelSpeed;
    private boolean showValue = false;

    private final float initialX, initialY;

    public void setShowValue(boolean showValue) {
        this.showValue = showValue;
    }

    private int color;
    private Image image;

    public Car(float x, float y) {
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

        this.setSpeed(0);
    }

    public void setSpeed(float speed) {
        this.scaleSpeed = speed;
        this.pixelSpeed = speed * MAX_PIXELSPERSECOND / MAX_SCALE;
    }

    public float getSpeed() {
        return scaleSpeed;
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

    public void setVerticalTarget(float target) {
        if (target != verticalMover.getTargetValue())
            verticalMover.setTargetValue(target);
    }
    public void setVerticalPosition(float position){
        verticalMover.setCurrentValue(position);
    }

    @Override
    public void Update(Input.TouchEvent[] touchEvents, float deltaTime) {
        verticalMover.Update();
        super.y = verticalMover.getCurrentValue();
        super.x += pixelSpeed * (deltaTime / 1000.0f);
    }

    public Car reset() {
        super.x = initialX;
        super.y = initialY;
        verticalMover.setCurrentValue(initialY);
        return this;
    }
}
