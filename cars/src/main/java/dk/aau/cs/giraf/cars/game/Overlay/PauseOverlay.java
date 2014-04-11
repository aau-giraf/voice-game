package dk.aau.cs.giraf.cars.game.Overlay;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;

public class PauseOverlay extends Overlay{
    private OverlayButton continueButton;

    public PauseOverlay(Game game)
    {
        continueButton = new OverlayButton(game.getWidth() / 2, game.getHeight() / 2, game.getResources().getString(R.string.crash_button_text));
        super.Add(continueButton);
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime)
    {

    }
}
