package dk.aau.cs.giraf.cars.game.Overlay;

import android.graphics.Rect;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Assets;
import dk.aau.cs.giraf.cars.game.Car;
import dk.aau.cs.giraf.cars.game.CarGame;
import dk.aau.cs.giraf.cars.game.GameScreen;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;
import dk.aau.cs.giraf.cars.game.Obstacle;
import dk.aau.cs.giraf.cars.game.ObstacleCollection;

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

        moveCarTo(1f - carControl.getMove(touchEvents));

        Obstacle obstacle = getCollisionObstacle();
        if (obstacle != null)
            showCrashScreen(obstacle);

        for (Input.TouchEvent e : touchEvents)
            if (e.type == Input.TouchEvent.TOUCH_DOWN && e.inBounds(pauseButtonRec))
                showPauseScreen();
    }

    @Override
    public void paint(Graphics graphics, float deltaTime) {
        super.paint(graphics, deltaTime);

        graphics.drawScaledImage(Assets.GetPauseButton(), pauseButtonRec, pauseButtonImageRec);
    }
}
