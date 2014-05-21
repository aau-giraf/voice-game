package dk.aau.cs.giraf.cars.Game.GameScreens;

import dk.aau.cs.giraf.cars.Assets;
import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.Game.Car;
import dk.aau.cs.giraf.cars.Game.CarGame;
import dk.aau.cs.giraf.cars.Game.GameItemCollection;
import dk.aau.cs.giraf.cars.Game.GameScreen;
import dk.aau.cs.giraf.game_framework.GameActivity;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;

public class WinningScreen extends GameScreen {
    private OverlayButton resetButton;
    private OverlayButton menuButton;

    private int trophyX;
    private int trophyY;

    public WinningScreen(CarGame game, Car car, GameItemCollection obstacles) {
        super(game, car, obstacles);

        int gameWidth = game.getWidth();
        int gameHeight = game.getHeight();
        this.trophyX = (gameWidth - Assets.GetTrophy().getWidth()) / 2;
        this.trophyY = (gameHeight - Assets.GetTrophy().getHeight()) / 2;

        resetButton = new OverlayButton((int) (gameWidth * 0.25), (int) (gameHeight * 0.65), game.getResources().getString(R.string.play_again_button_text));
        menuButton = new OverlayButton((int) (gameWidth * 0.75), (int) (gameHeight * 0.65), game.getResources().getString(R.string.menu_button_text));
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
        Assets.GetWellDone().Play();
        resetButton.Update(touchEvents, deltaTime);
        menuButton.Update(touchEvents, deltaTime);
        if (resetButton.IsClicked()) {
            Assets.GetPlayAgain().PlayAndReset();
            Assets.GetWellDone().Reset();
            while (Assets.GetPlayAgain().IsPlaying()) {
                //Do nothing while sound is playing
            }
            showStartScreen();
        } else if (menuButton.IsClicked()) {
            Assets.GetDone().PlayAndReset();
            Assets.GetWellDone().Reset();
            while (Assets.GetDone().IsPlaying()) {
                //Do nothing while sound is playing
            }
            ((GameActivity) game).finish();
        }
    }
}
