package dk.aau.cs.giraf.voicegame.game.GameScreens;

import dk.aau.cs.giraf.voicegame.Assets;
import dk.aau.cs.giraf.voicegame.R;
import dk.aau.cs.giraf.voicegame.game.Car;
import dk.aau.cs.giraf.voicegame.game.CarGame;
import dk.aau.cs.giraf.voicegame.game.GameItemCollection;
import dk.aau.cs.giraf.voicegame.game.GameScreen;
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

            Assets.GetWellDone().Reset();
            ((GameActivity) game).finish();
        }
    }
}
