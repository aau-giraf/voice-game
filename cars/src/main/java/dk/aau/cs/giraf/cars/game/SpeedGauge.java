package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.graphics.Rect;

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

    public SpeedGauge(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.valueLineHeight = height - PADDING * 2;
        this.midValueLineHeight = this.valueLineHeight / 2;
        this.minorValueLineHeight = this.valueLineHeight / 3;
        this.barHeight = height / 8;
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        graphics.drawRect(x, y, width, height, Color.WHITE);
        int lineDrawInterval = width - 2 * PADDING;
        int noOfLines = 10 * (MAX_SPEED - MIN_SPEED) + 1; //1 is for the final line
        float lineSpace = (float)lineDrawInterval / (noOfLines - 1);

        for (int i = 0; i < noOfLines; i++) {
            int lineXPos = (int)(lineSpace * i) + PADDING + this.x;
            int lineHeight = minorValueLineHeight;

            if (i % 10 == 0)
                lineHeight = valueLineHeight;
            else if (i % 5 == 0)
                lineHeight = midValueLineHeight;

            int lineTopBottomMargin = (height - lineHeight)/2 + this.y;
            graphics.drawLine(lineXPos, lineTopBottomMargin, lineXPos, lineTopBottomMargin + lineHeight, Color.BLACK);
        }

        if (currentSpeed > 0.0f) {
            float percentageSpeed = currentSpeed / MAX_SPEED;
            int speedRectWidth = (int) ((width - 2 * PADDING) * percentageSpeed);
            int barYPos = (height - barHeight) / 2 + this.y;

            graphics.drawRect(x + PADDING, barYPos, speedRectWidth, barHeight, Color.RED);
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
                else if (event.x > this.x + this.width)
                    newX = this.x + this.width;
                else
                    newX = event.x;
                float percentage = (float) (newX - this.x) / this.width;
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
}
