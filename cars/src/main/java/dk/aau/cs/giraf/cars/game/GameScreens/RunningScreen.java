package dk.aau.cs.giraf.cars.Game.GameScreens;

import android.graphics.Rect;

import dk.aau.cs.giraf.cars.Assets;
import dk.aau.cs.giraf.cars.Game.Car;
import dk.aau.cs.giraf.cars.Game.CarGame;
import dk.aau.cs.giraf.cars.Game.GameItemCollection;
import dk.aau.cs.giraf.cars.Game.GameScreen;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.cars.Interfaces.CarControl;

public abstract class RunningScreen extends GameScreen {
    private Rect pauseButtonRec = new Rect(20, 20, 100, 100);
    private Rect pauseButtonImageRec = new Rect(0, 0, Assets.GetPlayButton().getWidth(), Assets.GetPlayButton().getHeight());

    private CarControl carControl;
    private float carSpeed;

    public RunningScreen(CarGame game, Car car, GameItemCollection obstacles, CarControl carControl, float carSpeed) {
        super(game, car, obstacles);
        this.carControl = carControl;
        this.carSpeed = carSpeed;
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents, deltaTime);

        float moveTo = 1f - carControl.getMove(touchEvents);
        moveTo = Math.max(0, Math.min(1, moveTo));
        moveCarTo(moveTo);

        for (Input.TouchEvent e : touchEvents)
            if (e.type == Input.TouchEvent.TOUCH_DOWN && e.inBounds(pauseButtonRec))
                showPauseScreen();
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        super.paint(graphics, deltaTime);

        graphics.drawScaledImage(Assets.GetPauseButton(), pauseButtonRec, pauseButtonImageRec);
    }

    @Override
    public void showScreen() {
        setCarSpeed(carSpeed);
    }
}