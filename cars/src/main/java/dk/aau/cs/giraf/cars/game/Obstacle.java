package dk.aau.cs.giraf.cars.game;

import android.graphics.Paint;
import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;

public class Obstacle extends GameItem {
    private Paint paint;
    private int value;

    public Obstacle(float x, float y, float width, float height) {
        super(x, y, width, height);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(46);
        paint.setTextAlign(Paint.Align.CENTER);

        // The magic value below is the height of the game 800 minus the total height of the grass 2x70
        value = getBarometerNumber(GetBounds().centerY() - 70, 660);
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        Rect bounds = this.GetBounds();

        graphics.drawScaledImage(Assets.GetObstacle(),
                bounds.left, bounds.top, bounds.right - bounds.left, bounds.bottom - bounds.top,
                0, 0, Assets.GetObstacle().getWidth(), Assets.GetObstacle().getHeight());
        graphics.drawString(String.valueOf(value), bounds.centerX(), bounds.centerY() + 17, paint);
    }

    private int getBarometerNumber(float y, float height) {
        return Math.round(10 - (y / (height / 10)));
    }

    @Override
    public void Update(Input.TouchEvent[] touchEvents, float deltaTime) {

    }
}
