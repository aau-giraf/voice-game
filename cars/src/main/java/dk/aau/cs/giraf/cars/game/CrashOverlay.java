package dk.aau.cs.giraf.cars.game;

import android.graphics.Point;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;

public class CrashOverlay extends Overlay {
    private OverlayButton continueButton;
    Point lastCrash = null;
    int explosionWidth = 100;
    int explosionHeight = 100;

    public CrashOverlay(Game game){
        super(game);
        continueButton = new OverlayButton(game, game.getWidth() / 2 - 75, game.getHeight()/2-50,game.getResources().getString(R.string.crash_button_text));
        super.Add(continueButton);
    }

    public boolean ContinueButtonPressed(Input.TouchEvent[] touchEvents) {
        return continueButton.IsButtonPressed(touchEvents);
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime)
    {
        graphics.drawScaledImage(Assets.GetExplosion(), lastCrash.x - explosionWidth/2, lastCrash.y - explosionHeight/2, explosionWidth, explosionHeight, 0, 0, Assets.GetExplosion().getWidth(), Assets.GetExplosion().getHeight());
        graphics.drawARGB(155,0,0,0);
        super.Draw(graphics,deltaTime);
    }
}
