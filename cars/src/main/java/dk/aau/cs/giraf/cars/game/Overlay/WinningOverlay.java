package dk.aau.cs.giraf.cars.game.Overlay;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Assets;

public class WinningOverlay extends Overlay {
    private OverlayButton resetButton;
    private OverlayButton menuButton;

    private int gameWidth;
    private int gameHeight;
    private int trophyX;
    private int trophyY;

    public WinningOverlay(Game game, int gameWidth, int gameHeight) {
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.trophyX = (gameWidth - Assets.GetTrophy().getWidth()) / 2;
        this.trophyY = (gameHeight - Assets.GetTrophy().getHeight()) / 2;

        resetButton = new OverlayButton(game, (int) (gameWidth * 0.25), (int) (gameHeight * 0.85), game.getResources().getString(R.string.play_again_button_text));
        menuButton = new OverlayButton(game, (int) (gameWidth * 0.75), (int) (gameHeight * 0.85), game.getResources().getString(R.string.menu_button_text));

        Add(resetButton);
        Add(menuButton);
    }

    public boolean ResetButtonPressed(Input.TouchEvent[] events) {
        return resetButton.IsButtonPressed(events);
    }

    public boolean MenuButtonPressed(Input.TouchEvent[] events) {
        return menuButton.IsButtonPressed(events);
    }

    @Override

    public void Draw(Graphics graphics, float deltaTime) {
        graphics.drawARGB(155, 0, 0, 0);
        graphics.drawImage(Assets.GetTrophy(), trophyX, trophyY);
        super.Draw(graphics, deltaTime);
    }
}
