package dk.aau.cs.giraf.cars.game.Overlay;

import android.graphics.Point;
import android.graphics.Rect;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Assets;
import dk.aau.cs.giraf.cars.game.Car;
import dk.aau.cs.giraf.cars.game.GameState;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;

public class CrashOverlay extends Overlay {
    private OverlayButton continueButton;
    private Rect lastCrash;
    private Rect explosionRect;
    private CarControl carControl;
    private Car car;
    private Game game;
    private float grassSize;

    public void setLastCrash(Point p) {
        lastCrash.offsetTo(p.x, p.y);
        lastCrash.offset(-lastCrash.width() / 2, -lastCrash.height() / 2);
    }

    public CrashOverlay(Game game, CarControl carControl, Car car, float grassSize) {
        this.grassSize = grassSize;
        this.game = game;
        this.car = car;
        this.carControl = carControl;
        continueButton = new OverlayButton(game.getWidth() / 2, game.getHeight() / 2, game.getResources().getString(R.string.crash_button_text));
        super.Add(continueButton);

        this.lastCrash = new Rect(0, 0, 100, 100);
        this.explosionRect = new Rect(0, 0, Assets.GetExplosion().getWidth(), Assets.GetExplosion().getHeight());
    }

    private boolean continueButtonPressed(Input.TouchEvent[] touchEvents) {
        return continueButton.IsButtonPressed(touchEvents);
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        graphics.drawScaledImage(Assets.GetExplosion(), lastCrash, explosionRect);
        graphics.drawARGB(155, 0, 0, 0);
        super.Draw(graphics, deltaTime);
    }

    @Override
    public GameState Update(Input.TouchEvent[] touchEvents, float deltaTime) {
        //carControl.Reset();
        super.Update(touchEvents,deltaTime);

        if (continueButtonPressed(touchEvents)) {
            ResetCar();
            return GameState.Starting;
        }
        return GameState.Crashed;
    }
    public Car ResetCar() {
        car.setX(-car.getWidth());
        car.setY(game.getHeight() - grassSize - car.getHeight() / 2);
        return car;
    }

}
