package dk.aau.cs.giraf.cars.game.Overlay;

import android.graphics.Rect;
import android.util.Log;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Assets;

public class PauseOverlay extends Overlay {
    private boolean paused = false;
    private Rect playButtonSize = new Rect(20, 20, 100, 100);
    private Rect image = new Rect(0, 0, Assets.GetPlayButton().getWidth(), Assets.GetPlayButton().getHeight());

    public PauseOverlay() {
    }

    public boolean pauseButtonPressed(Input.TouchEvent[] touchEvents) {
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
        graphics.drawARGB(155, 0, 0, 0);
        graphics.drawScaledImage(Assets.GetPlayButton(), playButtonSize, image);
    }

    private boolean inBounds(Input.TouchEvent event, Rect r) {
        return event.x > r.left && event.x < r.left + r.right - 1 && event.y > r.top && event.y < r.top + r.bottom - 1;
    }
}
