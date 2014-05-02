package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Paint;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Interfaces.Drawable;
import dk.aau.cs.giraf.cars.game.Interfaces.Updatable;

public class SpeedGauge implements Drawable, Updatable{

    private int x, y, width, height;

    private final int MIN_SPEED = 0;
    private final int MAX_SPEED = 10;
    private final int PADDING = 5;
    private int barHeight, valueLineHeight, midValueLineHeight, minorValueLineHeight;
    private float currentSpeed = 0.0f;
    private int lineDrawInterval;
    private Paint valueTextPaint;
    private final int VALUE_TEXT_SIZE = 20;

    public SpeedGauge(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        valueLineHeight = this.height - PADDING * 2;
        midValueLineHeight = valueLineHeight / 2;
        minorValueLineHeight = valueLineHeight / 3;
        barHeight = this.height / 8;
        lineDrawInterval = width - 2 * PADDING - 24;

        valueTextPaint = new Paint();
        valueTextPaint.setTextSize(VALUE_TEXT_SIZE);
        valueTextPaint.setTextAlign(Paint.Align.LEFT);
        valueTextPaint.setAntiAlias(true);
        valueTextPaint.setColor(Color.BLACK);
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        graphics.drawRect(x, y, width, height, Color.WHITE);
        int noOfLines = 10 * (MAX_SPEED - MIN_SPEED) + 1; //1 is for the final line
        float lineSpace = (float)lineDrawInterval / (noOfLines - 1);

        for (int i = 0; i < noOfLines; i++) {
            int lineXPos = (int)(lineSpace * i) + PADDING + this.x;
            int lineHeight = minorValueLineHeight;

            if (i % 10 == 0) {
                lineHeight = valueLineHeight;
                graphics.drawString(Integer.toString(i / 10), lineXPos + 3, y + height - 4, valueTextPaint);
            }
            else if (i % 5 == 0)
                lineHeight = midValueLineHeight;

            int lineTopBottomMargin = (height - lineHeight)/2 + this.y;
            graphics.drawLine(lineXPos, lineTopBottomMargin, lineXPos, lineTopBottomMargin + lineHeight, Color.BLACK);
        }

        if (currentSpeed > 0.0f) {
            float percentageSpeed = currentSpeed / MAX_SPEED;
            int speedRectWidth = (int)(percentageSpeed * lineDrawInterval);
            int barYPos = (height - barHeight) / 2 + this.y;

            graphics.drawRect(x + PADDING + 1, barYPos, speedRectWidth, barHeight, Color.RED);
        }
    }

    @Override
    public void Update(Input.TouchEvent[] touchEvents, float deltaTime) {
        for (int i = 0; i < touchEvents.length; i++) {
            Input.TouchEvent event = touchEvents[i];

            if (event.y > this.y && event.y < this.y + this.height) {
                int newX;
                if (event.x < this.x)
                    newX = this.x;
                else if (event.x > this.x + this.lineDrawInterval)
                    newX = this.x + this.lineDrawInterval;
                else
                    newX = event.x;
                float percentage = (float) (newX - this.x) / this.lineDrawInterval;
                this.currentSpeed = percentage * MAX_SPEED;
            }
        }
    }

    public void SetSpeed(float speed) {
        this.currentSpeed = speed;
    }

    public float GetSpeed() {
        return this.currentSpeed;
    }

    public int GetPictoPos(int speed) {
        if (speed == 0 || speed > MAX_SPEED-1)
            throw new IllegalArgumentException();

        int valuePos = lineDrawInterval / MAX_SPEED;
        return valuePos * speed + x + PADDING;
    }
}
