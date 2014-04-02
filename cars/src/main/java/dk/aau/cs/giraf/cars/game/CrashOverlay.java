package dk.aau.cs.giraf.cars.game;

import android.graphics.Point;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;

public class CrashOverlay extends Overlay {

    private OverlayButton continueButton;

    public CrashOverlay(Game game){
        super(game);
        continueButton = new OverlayButton(game.getWidth() / 2 - 75, game.getHeight()/2-50, 150, 100, game.getWidth()/2, game.getHeight()/2,
                game.getResources().getString(R.string.crash_button_text));
    }

    public boolean ContinueButtonPressed(Input.TouchEvent[] touchEvents) {
        return continueButton.IsButtonPressed(touchEvents, continueButton);
    }

    public void Draw(Game game, Point lastCrash)
    {
        Graphics g = game.getGraphics();
        int explosionWidth = 100;
        int explosionHeight = 100;
        g.drawScaledImage(Assets.GetExplosion(), lastCrash.x - explosionWidth/2, lastCrash.y - explosionHeight/2, explosionWidth, explosionHeight, 0, 0, Assets.GetExplosion().getWidth(), Assets.GetExplosion().getHeight());
        g.drawARGB(155,0,0,0);
        continueButton.Draw(g);
    }
}
