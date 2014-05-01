package dk.aau.cs.giraf.cars.game.Overlay;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Assets;

public class WinningOverlay extends Overlay {
    private OverlayButton resetButton;
    private OverlayButton menuButton;

    private int trophyX;
    private int trophyY;

    public WinningOverlay(int gameWidth, int gameHeight, String restartMessage, String showmenuMessage) {
        this.trophyX = (gameWidth - Assets.GetTrophy().getWidth()) / 2;
        this.trophyY = (gameHeight - Assets.GetTrophy().getHeight()) / 2;

        resetButton = new OverlayButton((int) (gameWidth * 0.25), (int) (gameHeight * 0.65), restartMessage);
        menuButton = new OverlayButton((int) (gameWidth * 0.75), (int) (gameHeight * 0.65), showmenuMessage);

        Add(resetButton);
        Add(menuButton);
    }

    public boolean ResetButtonPressed() {
        return resetButton.IsPressed();
    }

    public boolean MenuButtonPressed() {
        return menuButton.IsPressed();
    }

    @Override

    public void Draw(Graphics graphics, float deltaTime) {
        graphics.drawARGB(155, 0, 0, 0);
        graphics.drawImage(Assets.GetTrophy(), trophyX, trophyY);
        super.Draw(graphics, deltaTime);
    }
}
