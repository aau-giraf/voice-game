package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;

public class WinningOverlay extends Overlay {
    private OverlayButton resetButton;
    private OverlayButton menuButton;

    public WinningOverlay(Game game){
        super(game);

        int width = game.getWidth();
        int height = game.getHeight();

        resetButton = new OverlayButton(0,height/2,width/2, height/2, (int) (width * .25), (int) (height * .85),game.getResources().getString(R.string.play_again_button_text));
        menuButton = new OverlayButton(width/2, height/2, width/2, height/2, (int)(width*.75), (int)(height*.85),game.getResources().getString(R.string.menu_button_text));
    }

    public boolean ResetButtonPressed(Input.TouchEvent[] events) {
        return resetButton.IsButtonPressed(events, resetButton);
    }

    public boolean MenuButtonPressed(Input.TouchEvent[] events) {
        return menuButton.IsButtonPressed(events, menuButton);
    }

    public void Draw(Game game)
    {
        int width = game.getWidth();
        int height = game.getHeight();
        Graphics g = game.getGraphics();
        g.drawARGB(155, 0, 0, 0);
        g.drawImage(Assets.GetTrophy(), (int) (width * .50) - Assets.GetTrophy().getWidth() / 2, (int) (height * .25) - Assets.GetTrophy().getHeight() / 2);
        resetButton.Draw(g);
        menuButton.Draw(g);
    }
}
