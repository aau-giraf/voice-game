package dk.aau.cs.giraf.cars.game.GameScreens;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import dk.aau.cs.giraf.cars.Assets;
import dk.aau.cs.giraf.cars.game.Car;
import dk.aau.cs.giraf.cars.game.CarGame;
import dk.aau.cs.giraf.cars.game.GameItemCollection;
import dk.aau.cs.giraf.cars.game.GameScreen;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;

public class PauseScreen extends GameScreen {
    private final int scaleWidth = 100;
    private final int scaleSize = 11;
    private Rect playButtonSize = new Rect(20, 20, 100, 100);
    private Rect image = new Rect(0, 0, Assets.GetPlayButton().getWidth(), Assets.GetPlayButton().getHeight());
    private int y, height, width;
    private float x;

    public PauseScreen(CarGame game, Car car, GameItemCollection obstacles, int grassSize) {
        super(game, car, obstacles);
        this.y = grassSize;
        this.height = game.getHeight() - 2 * grassSize;
        this.width = game.getWidth();
    }

    private boolean pauseButtonPressed(Input.TouchEvent[] touchEvents) {
        for (Input.TouchEvent e : touchEvents)
            if (e.type == Input.TouchEvent.TOUCH_DOWN && e.inBounds(playButtonSize))
                return true;
        return false;
    }

    @Override
    public void showScreen() {
        this.x = getCarLocationX();
        setCarSpeed(0);
        freezeCar();
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        super.paint(graphics, deltaTime);
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
        super.update(touchEvents, deltaTime);
        if (pauseButtonPressed(touchEvents))
            showRunningScreen();
    }
}
