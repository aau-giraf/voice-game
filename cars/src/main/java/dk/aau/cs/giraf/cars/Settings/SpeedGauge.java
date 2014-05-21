package dk.aau.cs.giraf.cars.Settings;

import android.graphics.Color;
import android.graphics.Paint;
import dk.aau.cs.giraf.cars.Game.Car;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.cars.Interfaces.Drawable;
import dk.aau.cs.giraf.cars.Interfaces.Updatable;

public class SpeedGauge implements Drawable, Updatable{

    private final int MIN_SPEED = 0;
    private final int MAX_SPEED = (int) Car.MAX_SCALE;
    private final int PADDING = 5;
    private final int VALUE_TEXT_SIZE = 20;
    private final int VALUE_TEXT_MARGIN = 4;
    private float currentSpeed = 0.0f;
    private int x, y, width, height;
    private int barHeight, valueLineHeight, midValueLineHeight, minorValueLineHeight;
    private int gaugeActualWidth;
    private Paint valueTextPaint;

    public SpeedGauge(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        valueLineHeight = this.height - PADDING * 2; //Line height for the big/numbered lines
        midValueLineHeight = valueLineHeight / 2; //Line height for middle values
        minorValueLineHeight = valueLineHeight / 3; //Line height for minor values
        barHeight = this.height / 8; //Height of the current speed bar
        gaugeActualWidth = width - 2 * PADDING - 24; //Width of the gauge (24 is to make space for writing last value)

        //Paint used for writing gauge values
        valueTextPaint = new Paint();
        valueTextPaint.setTextSize(VALUE_TEXT_SIZE);
        valueTextPaint.setTextAlign(Paint.Align.LEFT);
        valueTextPaint.setAntiAlias(true);
        valueTextPaint.setColor(Color.BLACK);
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        graphics.drawRect(x, y, width, height, Color.WHITE); //Gauge background
        int noOfLines = 10 * (MAX_SPEED - MIN_SPEED) + 1;
        float lineSpace = (float) gaugeActualWidth / (noOfLines - 1);

        for (int i = 0; i < noOfLines; i++) {
            int lineX = (int)(lineSpace * i) + PADDING + this.x;
            int lineHeight = minorValueLineHeight; //Default line height

            if (i % 10 == 0) {
                lineHeight = valueLineHeight;
                graphics.drawString(Integer.toString(i / 10), lineX + VALUE_TEXT_MARGIN, y + height - VALUE_TEXT_MARGIN, valueTextPaint);
            }
            else if (i % 5 == 0)
                lineHeight = midValueLineHeight;

            int lineMargin = (height - lineHeight)/2 + this.y; //Space over and under line
            graphics.drawLine(lineX, lineMargin, lineX, lineMargin + lineHeight, Color.BLACK);
        }

        if (currentSpeed > 0.0f) {
            float percentageSpeed = currentSpeed / MAX_SPEED;
            int speedRectWidth = (int)(percentageSpeed * gaugeActualWidth);
            int barYPos = (height - barHeight) / 2 + this.y;

            graphics.drawRect(x + PADDING + 1, barYPos, speedRectWidth, barHeight, Color.BLUE); //The 1 is to draw it 1 pixel after first line
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
                else if (event.x > this.x + this.gaugeActualWidth)
                    newX = this.x + this.gaugeActualWidth;
                else
                    newX = event.x;
                float percentage = (float) (newX - this.x) / this.gaugeActualWidth;
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

    public int GetValueX(int speed) {
        int valuePos = gaugeActualWidth / (MAX_SPEED - MIN_SPEED);
        return valuePos * (speed - MIN_SPEED) + x + PADDING;
    }
}
