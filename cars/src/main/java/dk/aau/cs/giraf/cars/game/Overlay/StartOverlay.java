package dk.aau.cs.giraf.cars.game.Overlay;

import android.graphics.Color;
import android.graphics.Paint;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.GameActivity;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.GameScreen;
import dk.aau.cs.giraf.cars.game.GameSettings;
import dk.aau.cs.giraf.cars.game.ObstacleGenerator;

public class StartOverlay extends GameScreen {
    private final int COUNTDOWN_IN_SECONDS = 3;
    private float counterInMS;
    private float visualCounter;
    private int seconds;
    private String driveMessage;
    private Paint pButton;

    public StartOverlay(GameActivity game,ObstacleGenerator obstacleGenerator, GameSettings gs) {
        super(game,obstacleGenerator,gs);
        this.seconds = COUNTDOWN_IN_SECONDS;
        pButton = new Paint();

        pButton.setTextSize(100);
        pButton.setTextAlign(Paint.Align.CENTER);
        pButton.setAntiAlias(true);
        pButton.setColor(Color.WHITE);

        this.driveMessage = game.getResources().getString(R.string.countdown_drive);
        resetCounters();
    }

    private void resetCounters()
    {
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
    public void paint(Graphics graphics, float deltaTime) {
        super.paint(graphics,deltaTime);
        int width = graphics.getWidth();
        int height = graphics.getHeight();
        graphics.drawARGB(155, 0, 0, 0);
        String out;
        out = visualCounter == 0 ? driveMessage : String.valueOf((int) visualCounter);
        graphics.drawString(out, width / 2, height / 2, pButton);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents,deltaTime);
        if (isTimerDone(deltaTime))
            game.setScreen(new RunningScreen(GetGameActivity(),GetObstacleGenerator(),GetGameSettings()));
    }
}
