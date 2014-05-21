package dk.aau.cs.giraf.cars.Game.GameScreens;

import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.cars.Assets;
import dk.aau.cs.giraf.cars.Game.Car;
import dk.aau.cs.giraf.cars.Game.CarGame;
import dk.aau.cs.giraf.cars.Game.GameItemCollection;
import dk.aau.cs.giraf.cars.Interfaces.CarControl;
import dk.aau.cs.giraf.cars.Game.RoadItem;

public class AvoidRunningScreen extends RunningScreen {
    public AvoidRunningScreen(CarGame game, Car car, GameItemCollection obstacles, CarControl carControl, float carSpeed) {
        super(game, car, obstacles, carControl, carSpeed);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents, deltaTime);

        if (getCarLocationX() + getCarWidth() > game.getWidth() - getFinishLineWidth())
            showWinningScreen();

        RoadItem roadItem = getCollisionRoadItem();
        if (roadItem != null) {
            Assets.GetCrash().PlayAndReset();
            showCrashScreen(roadItem);
        }
    }
}
