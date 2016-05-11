package dk.aau.cs.giraf.voicegame.game.GameScreens;

import android.graphics.Point;
import android.graphics.Rect;

import dk.aau.cs.giraf.game_framework.GameActivity;
import dk.aau.cs.giraf.voicegame.R;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.voicegame.Assets;
import dk.aau.cs.giraf.voicegame.game.Car;
import dk.aau.cs.giraf.voicegame.game.CarGame;
import dk.aau.cs.giraf.voicegame.game.GameItemCollection;
import dk.aau.cs.giraf.voicegame.game.GameScreen;

public class FailureScreen extends GameScreen {
    //private OverlayButton continueButton;
    private OverlayButton resetButton;
    private OverlayButton menuButton;
    private Rect lastCrash;
    private Rect explosionRect;
    private boolean crashOccured;

    public FailureScreen(CarGame game, Car car, GameItemCollection obstacles) {
        super(game, car, obstacles);

        int gameWidth = game.getWidth();
        int gameHeight = game.getHeight();

        //continueButton = new OverlayButton(game.getWidth() / 2, game.getHeight() / 2, game.getResources().getString(R.string.crash_button_text));

        resetButton = new OverlayButton((int) (gameWidth * 0.25), (int) (gameHeight * 0.65), game.getResources().getString(R.string.crash_button_text));
        menuButton = new OverlayButton((int) (gameWidth * 0.75), (int) (gameHeight * 0.65), game.getResources().getString(R.string.menu_button_text));

        this.lastCrash = new Rect(0, 0, 100, 100);
        this.explosionRect = new Rect(0, 0, Assets.GetExplosion().getWidth(), Assets.GetExplosion().getHeight());

        this.crashOccured = false;
    }

    @Override
    public void showScreen() {
        setCarSpeed(0);
        freezeCar();
    }

    public void setLastCrash(Point p) {
        lastCrash.offsetTo(p.x, p.y);
        lastCrash.offset(-lastCrash.width() / 2, -lastCrash.height() / 2);
        crashOccured = true;
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        super.paint(graphics, deltaTime);

        if(crashOccured = true) {
            graphics.drawScaledImage(Assets.GetExplosion(), lastCrash, explosionRect);
        }

        resetButton.Draw(graphics, deltaTime);
        menuButton.Draw(graphics, deltaTime);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents, deltaTime);
        /*
        if (Assets.GetNewTurn().IsPlaying())
            return;
        else if (continuePressed) {
            continuePressed = false;
            Assets.GetNewTurn().Reset();
            resetCar();
            showRunningScreen();
        }
        else {
            continueButton.Update(touchEvents, deltaTime);
            if (continueButton.IsClicked()) {
                Assets.GetNewTurn().Play();
                continuePressed = true;
            }
        }
        */
        resetButton.Update(touchEvents, deltaTime);
        menuButton.Update(touchEvents, deltaTime);
        if (resetButton.IsClicked()) {
            Assets.GetNewTurn().PlayAndReset();
            Assets.GetWellDone().Reset();
            while (Assets.GetNewTurn().IsPlaying()) {
                //Do nothing while sound is playing
            }
            crashOccured = false;
            showStartScreen();
        } else if (menuButton.IsClicked()) {

            Assets.GetWellDone().Reset();
            ((GameActivity) game).finish();
            crashOccured = false;
        }
    }

}
