package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;

public class CrashOverlay extends Overlay {

    private OverlayButton continueButton;

    public CrashOverlay(Game game){
        super(game);
        continueButton = new OverlayButton(game, game.getWidth() / 2 - 75, game.getHeight()/2-50, 150, 100, game.getWidth()/2, game.getHeight()/2,
                game.getResources().getString(R.string.crash_button_text));
        super.Add(continueButton);
    }

    public boolean ContinueButtonPressed(Input.TouchEvent[] touchEvents) {
        return continueButton.IsButtonPressed(touchEvents);
    }



    public void Draw(Game game)
    {
        Graphics g = game.getGraphics();
        g.drawARGB(155,0,0,0);
    }
}
