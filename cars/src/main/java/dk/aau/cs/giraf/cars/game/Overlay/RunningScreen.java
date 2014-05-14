package dk.aau.cs.giraf.cars.game.Overlay;

import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.*;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;

public class RunningScreen extends GameScreen {
    private Rect pauseButtonRec = new Rect(20, 20, 100, 100);
    private Rect pauseButtonImageRec = new Rect(0, 0, Assets.GetPlayButton().getWidth(), Assets.GetPlayButton().getHeight());

    private CarControl carControl;
    private float carSpeed;

    public RunningScreen(CarGame game, Car car, ObstacleCollection obstacles, CarControl carControl, float carSpeed) {
        super(game, car, obstacles);
        this.carControl = carControl;
        this.carSpeed = carSpeed;
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents, deltaTime);

        if (getCarLocationX() > game.getWidth())
            showWinningScreen();

        float moveTo = 1f - carControl.getMove(touchEvents);
        moveTo = Math.max(0, Math.min(1, moveTo));
        moveCarTo(moveTo);

        Obstacle obstacle = getCollisionObstacle();
        if (obstacle != null) {
            Assets.GetCrash().play(1.0f);
            showCrashScreen(obstacle);
        }

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
