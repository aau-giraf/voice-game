package dk.aau.cs.giraf.cars.game.Overlay;

import android.graphics.Point;
import android.graphics.Rect;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Assets;

public class CrashOverlay extends Overlay {
    private OverlayButton continueButton;
    private Rect lastCrash;
    private Rect explosionRect;

    public void setLastCrash(Point p) {
        lastCrash.offsetTo(p.x, p.y);
        lastCrash.offset(-lastCrash.width() / 2, -lastCrash.height() / 2);
    }

    public CrashOverlay(Game game) {
        continueButton = new OverlayButton(game.getWidth() / 2, game.getHeight() / 2, game.getResources().getString(R.string.crash_button_text));
        super.Add(continueButton);

        this.lastCrash = new Rect(0, 0, 100, 100);
        this.explosionRect = new Rect(0, 0, Assets.GetExplosion().getWidth(), Assets.GetExplosion().getHeight());
    }

    public boolean ContinueButtonPressed(Input.TouchEvent[] touchEvents) {
        return continueButton.IsButtonPressed(touchEvents);
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        graphics.drawScaledImage(Assets.GetExplosion(), lastCrash, explosionRect);
        graphics.drawARGB(155, 0, 0, 0);
        super.Draw(graphics, deltaTime);
    }
}
