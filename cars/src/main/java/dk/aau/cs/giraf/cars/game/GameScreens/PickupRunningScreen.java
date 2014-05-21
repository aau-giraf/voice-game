package dk.aau.cs.giraf.cars.Game.GameScreens;

import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.cars.Assets;
import dk.aau.cs.giraf.cars.Game.Car;
import dk.aau.cs.giraf.cars.Game.CarGame;
import dk.aau.cs.giraf.cars.Game.GameItemCollection;
import dk.aau.cs.giraf.cars.Interfaces.CarControl;
import dk.aau.cs.giraf.cars.Game.RoadItem;

public class PickupRunningScreen extends RunningScreen {
    public PickupRunningScreen(CarGame game, Car car, GameItemCollection obstacles, CarControl carControl, float carSpeed) {
        super(game, car, obstacles, carControl, carSpeed);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents, deltaTime);

        if (isRoadItemsEmpty() && getCarLocationX() + getCarWidth() > game.getWidth() - getFinishLineWidth())
            showWinningScreen();
        else if (!isRoadItemsEmpty() && getCarLocationX() + getCarWidth() > getFinishLineX())
            showFailureScreen();

        RoadItem roadItem = getCollisionRoadItem();
        if (roadItem != null) {
            Assets.GetPickup().PlayAndReset();
            removeObstacle(roadItem);
        }


    }
}
