package dk.aau.cs.giraf.cars.Game.GameScreens;

import android.graphics.Color;
import android.graphics.Paint;
import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.cars.Assets;
import dk.aau.cs.giraf.cars.Game.Car;
import dk.aau.cs.giraf.cars.Game.CarGame;
import dk.aau.cs.giraf.cars.Game.GameItemCollection;
import dk.aau.cs.giraf.cars.Game.GameScreen;

public class StartScreen extends GameScreen {
    private final int COUNTDOWN_IN_SECONDS = 3;
    private float counterInMS;
    private float visualCounter;
    private int seconds;
    private String driveMessage;
    private Paint pButton;

    public StartScreen(CarGame game, Car car, GameItemCollection obstacles) {
        super(game, car, obstacles);
        this.seconds = COUNTDOWN_IN_SECONDS;
        pButton = new Paint();

        pButton.setTextSize(100);
        pButton.setTextAlign(Paint.Align.CENTER);
        pButton.setAntiAlias(true);
        pButton.setColor(Color.WHITE);

        this.driveMessage = game.getResources().getString(R.string.countdown_drive);

        resetCounters();
    }

    private void resetCounters() {
        counterInMS = (seconds + 1) * 1000;
        visualCounter = seconds;
    }

    private boolean isTimerDone(float deltaTime) {
        counterInMS -= deltaTime;

        if (counterInMS <= 0) {
            resetCounters();
            return true;
        }

        if (counterInMS < visualCounter * 1000)
            visualCounter--;

        return false;
    }

    @Override
    public void showScreen() {
        resetObstacles();
        setCarSpeed(0);
        resetCar();
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        super.paint(graphics, deltaTime);
        int width = graphics.getWidth();
        int height = graphics.getHeight();
        graphics.drawARGB(155, 0, 0, 0);
        String out;
        out = visualCounter == 0 ? driveMessage : String.valueOf((int) visualCounter);
        graphics.drawString(out, width / 2, height / 2, pButton);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents, deltaTime);

        if (visualCounter == 2) {
            Assets.GetCarStart().Play();
        }

        if (isTimerDone(deltaTime)) {
            Assets.GetCarStart().Reset();
            showRunningScreen();
        }
    }
}
