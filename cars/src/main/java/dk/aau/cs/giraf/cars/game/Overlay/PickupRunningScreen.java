package dk.aau.cs.giraf.cars.game.Overlay;

import dk.aau.cs.giraf.cars.game.Car;
import dk.aau.cs.giraf.cars.game.CarGame;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;
import dk.aau.cs.giraf.cars.game.ObstacleCollection;

public class PickupRunningScreen extends RunningScreen{
    public PickupRunningScreen(CarGame game, Car car, ObstacleCollection obstacles, CarControl carControl, float carSpeed) {
        super(game, car, obstacles, carControl, carSpeed);
    }
}
