package dk.aau.cs.giraf.cars.game.Overlay;

import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Assets;
import dk.aau.cs.giraf.cars.game.Car;
import dk.aau.cs.giraf.cars.game.CarGame;
import dk.aau.cs.giraf.cars.game.GameItemCollection;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;
import dk.aau.cs.giraf.cars.game.Obstacle;
import dk.aau.cs.giraf.cars.game.RoadItem;

public class PickupRunningScreen extends RunningScreen{
    public PickupRunningScreen(CarGame game, Car car, GameItemCollection obstacles, CarControl carControl, float carSpeed) {
        super(game, car, obstacles, carControl, carSpeed);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime){
        super.update(touchEvents,deltaTime);
        RoadItem roadItem = getCollisionRoadItem();
        if (roadItem != null) {
            Assets.GetPickup().PlayAndReset();
            removeObstacle(roadItem);
        }
    }
}
