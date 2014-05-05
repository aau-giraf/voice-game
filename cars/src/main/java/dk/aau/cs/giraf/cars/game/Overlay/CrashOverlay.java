package dk.aau.cs.giraf.cars.game.Overlay;

import android.graphics.Point;
import android.graphics.Rect;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.GameActivity;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.framework.mFloat;
import dk.aau.cs.giraf.cars.game.Assets;
import dk.aau.cs.giraf.cars.game.Car;
import dk.aau.cs.giraf.cars.game.GameScreen;
import dk.aau.cs.giraf.cars.game.GameSettings;
import dk.aau.cs.giraf.cars.game.GameState;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;
import dk.aau.cs.giraf.cars.game.ObstacleGenerator;

public class CrashOverlay extends GameScreen {
    private OverlayButton continueButton;
    private Rect lastCrash;
    private Rect explosionRect;

    public void setLastCrash(Point p) {
        lastCrash.offsetTo(p.x, p.y);
        lastCrash.offset(-lastCrash.width() / 2, -lastCrash.height() / 2);
    }

    public CrashOverlay(GameActivity gameActivity, ObstacleGenerator obstacleGenerator, GameSettings gameSettings) {
        super(gameActivity,obstacleGenerator,gameSettings);

        continueButton = new OverlayButton(game.getWidth() / 2, game.getHeight() / 2, game.getResources().getString(R.string.crash_button_text));

        this.lastCrash = new Rect(0, 0, 100, 100);
        this.explosionRect = new Rect(0, 0, Assets.GetExplosion().getWidth(), Assets.GetExplosion().getHeight());
    }

    private boolean continueButtonPressed() {
        return continueButton.IsPressed();
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        graphics.drawScaledImage(Assets.GetExplosion(), lastCrash, explosionRect);
        graphics.drawARGB(155, 0, 0, 0);
        continueButton.Draw(graphics,deltaTime);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        continueButton.Update(touchEvents,deltaTime);
        if (continueButtonPressed()) {
            car.ResetCar(game.getHeight(),grassSize,verticalMover);
            game.setScreen(new RunningScreen(GetGameActivity(),GetObstacleGenerator(),GetGameSettings()));
        }
    }
}
