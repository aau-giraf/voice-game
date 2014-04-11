package dk.aau.cs.giraf.cars.game.Overlay;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Assets;

public class PauseOverlay extends Overlay {
    private boolean paused = false;
    private Rect playButtonSize = new Rect(20, 20, 100, 100);
    private Rect image = new Rect(0, 0, Assets.GetPlayButton().getWidth(), Assets.GetPlayButton().getHeight());
    private int y, height, width;
    private float x;
    private final int scaleWidth = 100;
    private final int scaleSize = 11;

    public PauseOverlay(float x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public boolean pauseButtonPressed(Input.TouchEvent[] touchEvents, float carX) {
        this.x = carX;
        for (Input.TouchEvent e : touchEvents)
            if (e.type == Input.TouchEvent.TOUCH_DOWN)
                if (inBounds(e, playButtonSize)) {
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
            graphics.drawRect((int) x - scaleWidth, y, scaleWidth, height, Color.WHITE);
            for (int i = 0; i <= scaleSize; i++) {
                int tmp = 70 + (height / scaleSize * i);
                graphics.drawLine((int) x - scaleWidth, tmp, (int) x, tmp, Color.BLACK, 5);
                if (i != 0)
                    graphics.drawString(scaleSize - i + "", (int)x-scaleWidth + (scaleWidth / 2), tmp-10, paint);
            }
        } else if (x < width - 300) {
            graphics.drawRect(100, y, scaleWidth, height, Color.WHITE);
            for (int i = 0; i <= scaleSize; i++) {
                int tmp = 70 + (height / scaleSize * i);
                graphics.drawLine(100, tmp, 100 + scaleWidth, tmp, Color.BLACK, 5);
                if (i != 0)
                    graphics.drawString(scaleSize - i + "", 100 + (scaleWidth / 2), tmp-10, paint);
            }
        }

    }

    private boolean inBounds(Input.TouchEvent event, Rect r) {
        return event.x > r.left && event.x < r.left + r.right - 1 && event.y > r.top && event.y < r.top + r.bottom - 1;
    }
}
