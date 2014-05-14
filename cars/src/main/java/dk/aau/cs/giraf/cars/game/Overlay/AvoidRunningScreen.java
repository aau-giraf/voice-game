package dk.aau.cs.giraf.cars.game.Overlay;

import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Assets;
import dk.aau.cs.giraf.cars.game.Car;
import dk.aau.cs.giraf.cars.game.CarGame;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;
import dk.aau.cs.giraf.cars.game.Obstacle;
import dk.aau.cs.giraf.cars.game.ObstacleCollection;

public class AvoidRunningScreen extends RunningScreen{
    public AvoidRunningScreen(CarGame game, Car car, ObstacleCollection obstacles, CarControl carControl, float carSpeed) {
        super(game, car, obstacles, carControl, carSpeed);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime){
        super.update(touchEvents,deltaTime);
        Obstacle obstacle = getCollisionObstacle();
        if (obstacle != null) {
            Assets.GetCrash().play(1.0f);
            showCrashScreen(obstacle);
        }
    }
}
