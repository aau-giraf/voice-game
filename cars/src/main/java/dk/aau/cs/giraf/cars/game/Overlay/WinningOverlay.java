package dk.aau.cs.giraf.cars.game.Overlay;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.GameActivity;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Assets;
import dk.aau.cs.giraf.cars.game.CarGame;
import dk.aau.cs.giraf.cars.game.GameScreen;
import dk.aau.cs.giraf.cars.game.GameSettings;
import dk.aau.cs.giraf.cars.game.GameState;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;
import dk.aau.cs.giraf.cars.game.ObstacleGenerator;

public class WinningOverlay extends GameScreen {
    private OverlayButton resetButton;
    private OverlayButton menuButton;
    private GameScreen gameScreen;

    private int trophyX;
    private int trophyY;

    public WinningOverlay(GameActivity gameActivity, ObstacleGenerator obstacleGenerator, GameSettings gameSettings) {
        super(gameActivity, obstacleGenerator, gameSettings);
        this.gameScreen = gameScreen;

        int gameWidth = game.getWidth();
        int gameHeight = game.getHeight();
        this.trophyX = (gameWidth - Assets.GetTrophy().getWidth()) / 2;
        this.trophyY = (gameHeight - Assets.GetTrophy().getHeight()) / 2;

        resetButton = new OverlayButton((int) (gameWidth * 0.25), (int) (gameHeight * 0.65), "" + R.string.play_again_button_text);
        menuButton = new OverlayButton((int) (gameWidth * 0.75), (int) (gameHeight * 0.65), "" + R.string.menu_button_text);
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        super.paint(graphics, deltaTime);
        graphics.drawARGB(155, 0, 0, 0);
        graphics.drawImage(Assets.GetTrophy(), trophyX, trophyY);
        resetButton.Draw(graphics, deltaTime);
        menuButton.Draw(graphics, deltaTime);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents, deltaTime);
        carControl.Reset();
        resetButton.Update(touchEvents, deltaTime);
        menuButton.Update(touchEvents, deltaTime);
        if (ResetButtonPressed()) {
            CarGame newGame = new CarGame();
            game.setScreen(newGame.getFirstScreen());
        } else if (MenuButtonPressed()) {
            ((GameActivity) game).finish();
        }
            ((GameActivity) game).finish();
        }
    }
}
