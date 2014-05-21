package dk.aau.cs.giraf.cars.Game.GameScreens;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.cars.Assets;
import dk.aau.cs.giraf.cars.Game.Car;
import dk.aau.cs.giraf.cars.Game.CarGame;
import dk.aau.cs.giraf.cars.Game.GameItemCollection;
import dk.aau.cs.giraf.cars.Game.GameScreen;

public class FailureScreen extends GameScreen {
    private OverlayButton continueButton;
    private boolean continuePressed;

    public FailureScreen(CarGame game, Car car, GameItemCollection obstacles) {
        super(game, car, obstacles);

        continueButton = new OverlayButton(game.getWidth() / 2, game.getHeight() / 2, game.getResources().getString(R.string.crash_button_text));

        this.continuePressed = false;
    }

    @Override
    public void showScreen() {
        setCarSpeed(0);
        freezeCar();
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        super.paint(graphics, deltaTime);
        //Cirkler om kasser evt
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
