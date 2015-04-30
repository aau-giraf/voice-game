package dk.aau.cs.giraf.cars.game.GameScreens;

import android.graphics.Point;
import android.graphics.Rect;
import dk.aau.cs.giraf.cars.Assets;
import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.game.Car;
import dk.aau.cs.giraf.cars.game.CarGame;
import dk.aau.cs.giraf.cars.game.GameItemCollection;
import dk.aau.cs.giraf.cars.game.GameScreen;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;

public class CrashScreen extends GameScreen {
    private OverlayButton continueButton;
    private Rect lastCrash;
    private Rect explosionRect;
    private boolean continuePressed;

    public CrashScreen(CarGame game, Car car, GameItemCollection obstacles) {
        super(game, car, obstacles);

        continueButton = new OverlayButton(game.getWidth() / 2, game.getHeight() / 2, game.getResources().getString(R.string.crash_button_text));

        this.lastCrash = new Rect(0, 0, 100, 100);
        this.explosionRect = new Rect(0, 0, Assets.GetExplosion().getWidth(), Assets.GetExplosion().getHeight());

        this.continuePressed = false;
    }

    public void setLastCrash(Point p) {
        lastCrash.offsetTo(p.x, p.y);
        lastCrash.offset(-lastCrash.width() / 2, -lastCrash.height() / 2);
    }

    @Override
    public void showScreen() {
        setCarSpeed(0);
        freezeCar();
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        super.paint(graphics, deltaTime);
        graphics.drawScaledImage(Assets.GetExplosion(), lastCrash, explosionRect);
        graphics.drawARGB(155, 0, 0, 0);
        continueButton.Draw(graphics, deltaTime);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents, deltaTime);

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
    }
}
