package dk.aau.cs.giraf.cars.game.Overlay;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.GameActivity;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Assets;
import dk.aau.cs.giraf.cars.game.GameScreen;
import dk.aau.cs.giraf.cars.game.GameSettings;
import dk.aau.cs.giraf.cars.game.GameState;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;

public class WinningOverlay extends Overlay {
    private OverlayButton resetButton;
    private OverlayButton menuButton;
    private Game game;
    private GameScreen gameScreen;
    private CarControl carControl;
    private GameSettings gameSettings;

    private int trophyX;
    private int trophyY;

    public WinningOverlay(Game game, GameScreen gameScreen, String restartMessage, String showmenuMessage, CarControl carControl, GameSettings gameSettings) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.carControl = carControl;
        this.gameSettings = gameSettings;

        int gameWidth = game.getWidth();
        int gameHeight = game.getHeight();
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

    @Override
    public GameState Update(Input.TouchEvent[] touchEvents, float deltaTime) {
        carControl.Reset();
        super.Update(touchEvents, deltaTime);
        if (ResetButtonPressed()) {
            gameScreen.startNewGame();
            return GameState.Running;
        } else if (MenuButtonPressed()) {
            ((GameActivity) game).finish();
        }
        return GameState.Won;
    }
}
