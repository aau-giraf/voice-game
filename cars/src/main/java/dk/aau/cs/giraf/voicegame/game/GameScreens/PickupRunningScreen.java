package dk.aau.cs.giraf.voicegame.game.GameScreens;

import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.voicegame.Assets;
import dk.aau.cs.giraf.voicegame.game.Car;
import dk.aau.cs.giraf.voicegame.game.CarGame;
import dk.aau.cs.giraf.voicegame.game.GameItemCollection;
import dk.aau.cs.giraf.voicegame.Interfaces.CarControl;
import dk.aau.cs.giraf.voicegame.game.RoadItem;

public class PickupRunningScreen extends RunningScreen {
    public PickupRunningScreen(CarGame game, Car car, GameItemCollection obstacles, CarControl carControl, float carSpeed) {
        super(game, car, obstacles, carControl, carSpeed);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents, deltaTime);

        if (isRoadItemsEmpty() && getCarLocationX() + getCarWidth() > game.getWidth() - getFinishLineWidth()) {
            Assets.GetWellDone().Play();
            showWinningScreen();
        }
        else if (!isRoadItemsEmpty() && getCarLocationX() + getCarWidth() > getFinishLineX())
            showFailureScreen();

        RoadItem roadItem = getCollisionRoadItem();
        if (roadItem != null) {
            Assets.GetPickup().PlayAndReset();
            removeObstacle(roadItem);
        }


    }
}
