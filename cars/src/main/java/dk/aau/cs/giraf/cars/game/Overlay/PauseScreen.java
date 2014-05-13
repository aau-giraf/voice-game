package dk.aau.cs.giraf.cars.game.Overlay;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import dk.aau.cs.giraf.cars.framework.GameActivity;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Assets;
import dk.aau.cs.giraf.cars.game.Car;
import dk.aau.cs.giraf.cars.game.CarGame;
import dk.aau.cs.giraf.cars.game.GameScreen;
import dk.aau.cs.giraf.cars.game.GameSettings;
import dk.aau.cs.giraf.cars.game.GameState;
import dk.aau.cs.giraf.cars.game.ObstacleCollection;
import dk.aau.cs.giraf.cars.game.ObstacleGenerator;

public class PauseScreen extends GameScreen {
    private boolean paused = false;
    private Rect playButtonSize = new Rect(20, 20, 100, 100);
    private Rect image = new Rect(0, 0, Assets.GetPlayButton().getWidth(), Assets.GetPlayButton().getHeight());
    private int y, height, width;
    private float x;
    private final int scaleWidth = 100;
    private final int scaleSize = 11;

    public PauseScreen(CarGame game, Car car, ObstacleCollection obstacles) {
        super(game, car, obstacles);
        this.x = car.getX();
        this.y = grassSize;
        this.height = game.getHeight() - 2 * grassSize;
        this.width = game.getWidth();
    }

    private boolean pauseButtonPressed(Input.TouchEvent[] touchEvents) {
        this.x = car.getX();
        for (Input.TouchEvent e : touchEvents)
            if (e.type == Input.TouchEvent.TOUCH_DOWN)
                if (e.inBounds(playButtonSize)) {
                    paused = !paused;
                    return paused;
                }
        return paused;
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        super.paint(graphics,deltaTime);
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
                graphics.drawString(scaleSize - i + "", x + (scaleWidth / 2), tmp - 10, paint);
        }
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents,deltaTime);
        if (pauseButtonPressed(touchEvents))
            game.setScreen(new RunningScreen(GetGameActivity(), GetObstacleGenerator(), GetGameSettings()));
    }
}
