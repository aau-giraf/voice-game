package dk.aau.cs.giraf.cars.game.Overlay;

import android.graphics.Color;
import android.graphics.Paint;

import dk.aau.cs.giraf.cars.framework.Graphics;

public class StartOverlay extends Overlay {
    private float counterInMS;
    private float visualCounter;
    private String driveMessage;
    private Paint pButton;

    public StartOverlay(int seconds, String driveMessage) {
        pButton = new Paint();

        pButton.setTextSize(100);
        pButton.setTextAlign(Paint.Align.CENTER);
        pButton.setAntiAlias(true);
        pButton.setColor(Color.WHITE);

        this.driveMessage = driveMessage;

        counterInMS = (seconds + 1) * 1000;
        visualCounter = seconds;
    }

    public boolean IsTimerDone(float deltaTime) {
        counterInMS -= deltaTime;

        if (counterInMS <= 0)
            return true;

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
}
