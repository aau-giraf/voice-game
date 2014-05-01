package dk.aau.cs.giraf.cars.game.Overlay;

import android.graphics.Color;
import android.graphics.Paint;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.GameState;

public class StartOverlay extends Overlay {
    private float counterInMS;
    private float visualCounter;
    private int seconds;
    private String driveMessage;
    private Paint pButton;

    public StartOverlay(int seconds, String driveMessage) {
        this.seconds = seconds;
        pButton = new Paint();

        pButton.setTextSize(100);
        pButton.setTextAlign(Paint.Align.CENTER);
        pButton.setAntiAlias(true);
        pButton.setColor(Color.WHITE);

        this.driveMessage = driveMessage;
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
    public void Draw(Graphics graphics, float deltaTime) {
        int width = graphics.getWidth();
        int height = graphics.getHeight();
        graphics.drawARGB(155, 0, 0, 0);
        String out;
        out = visualCounter == 0 ? driveMessage : String.valueOf((int) visualCounter);
        graphics.drawString(out, width / 2, height / 2, pButton);
    }

    @Override
    public GameState Update(Input.TouchEvent[] touchEvents, float deltaTime) {
        if (isTimerDone(deltaTime))
            return GameState.Running;
        return GameState.Starting;
    }
}
