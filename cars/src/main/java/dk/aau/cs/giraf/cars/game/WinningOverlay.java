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

        resetButton = new OverlayButton(0,height/2,width/2, height/2, (int) (width * .25), (int) (height * .85));
        menuButton = new OverlayButton(width/2, height/2, width/2, height/2, (int)(width*.75), (int)(height*.85));
    }

    public boolean ResetButtonPressed(Input.TouchEvent[] events) {
        return IsButtonPressed(events, resetButton);
    }

    public boolean MenuButtonPressed(Input.TouchEvent[] events) {
        return IsButtonPressed(events, menuButton);
    }

    public void Draw(Game game)
    {
        int width = game.getWidth();
        int height = game.getHeight();
        Graphics g = game.getGraphics();
        g.drawARGB(155, 0, 0, 0);
        g.drawImage(Assets.GetTrophy(), (int) (width * .50) - Assets.GetTrophy().getWidth() / 2, (int) (height * .25) - Assets.GetTrophy().getHeight() / 2);
        g.drawString(game.getResources().getString(R.string.play_again_button_text), resetButton.DrawX, resetButton.DrawY, resetButton.Pressed ? pFocus : pButton);
        g.drawString(game.getResources().getString(R.string.menu_button_text), menuButton.DrawX, menuButton.DrawY, menuButton.Pressed ? pFocus : pButton);
    }
}
