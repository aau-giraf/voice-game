package dk.aau.cs.giraf.cars.game.Overlay;

import android.graphics.Rect;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Assets;

public class PauseOverlay extends Overlay{
    private OverlayButton continueButton;
    private boolean paused = false;

    Rect button = new Rect(20,20,100,100);
    Rect image = new Rect(0,0, Assets.GetPauseButton().getWidth(),Assets.GetPauseButton().getHeight());

    public PauseOverlay(Game game)
    {
        continueButton = new OverlayButton(game.getWidth() / 2, game.getHeight() / 2, game.getResources().getString(R.string.crash_button_text));
        super.Add(continueButton);
    }

    public boolean pauseButtonPressed(Input.TouchEvent[] touchEvents)
    {
        for (Input.TouchEvent e : touchEvents)
            if (e.type == Input.TouchEvent.TOUCH_DOWN && inBounds(e,button))
                paused = !paused;
        return paused;
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime)
    {
        if (paused)
        {
            graphics.drawScaledImage(Assets.GetPlayButton(), button, image);
            graphics.drawARGB(155, 0, 0, 0);
        }
        else graphics.drawScaledImage(Assets.GetPauseButton(), button, image);
    }

    private boolean inBounds(Input.TouchEvent event, Rect r) {
        return event.x > r.left && event.x < r.left + r.right - 1 && event.y > r.top && event.y < r.top + r.bottom - 1;
    }
}
