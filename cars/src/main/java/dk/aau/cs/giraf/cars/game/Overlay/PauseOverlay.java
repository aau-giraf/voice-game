package dk.aau.cs.giraf.cars.game.Overlay;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Assets;
import dk.aau.cs.giraf.cars.game.GameState;

public class PauseOverlay extends Overlay {
    private boolean paused = false;
    private Rect playButtonSize = new Rect(20, 20, 100, 100);
    private Rect image = new Rect(0, 0, Assets.GetPlayButton().getWidth(), Assets.GetPlayButton().getHeight());
    private int y, height, width;
    private float x;
    private final int scaleWidth = 100;
    private final int scaleSize = 11;
    private float carX;

    public PauseOverlay(float x, int y, int height, int width, float carX) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.carX = carX;
    }

    public boolean pauseButtonPressed(Input.TouchEvent[] touchEvents, float carX) {
        this.x = carX;
        for (Input.TouchEvent e : touchEvents)
            if (e.type == Input.TouchEvent.TOUCH_DOWN)
                if (e.inBounds( playButtonSize)) {
                    paused = !paused;
                    return paused;
                }
        return paused;
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        Paint paint = new Paint();
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        graphics.drawARGB(155, 0, 0, 0);
        graphics.drawScaledImage(Assets.GetPlayButton(), playButtonSize, image);
        Log.d("X", x + "");
        if (x > 200) {
            drawGauge(graphics, paint, (int) x - scaleWidth);
        } else if (x < width - 300) {
            drawGauge(graphics, paint, 100);
        }

    }

    private void drawGauge(Graphics graphics, Paint paint, int x) {
        graphics.drawRect(x, y, scaleWidth, height, Color.WHITE);
        for (int i = 0; i <= scaleSize; i++) {
            int tmp = 70 + (height / scaleSize * i);
            graphics.drawLine(x, tmp, x + scaleWidth, tmp, Color.BLACK, 5);
            if (i != 0)
                graphics.drawString(scaleSize - i + "", x + (scaleWidth / 2), tmp-10, paint);
        }
    }

    @Override
    public GameState Update(Input.TouchEvent[] touchEvents, float deltaTime) {
        if (pauseButtonPressed(touchEvents, carX))
            return GameState.Paused;
        return GameState.Running;
    }
}
