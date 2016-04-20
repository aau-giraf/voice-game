package dk.aau.cs.giraf.voicegame.game.GameScreens;

import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.voicegame.Assets;
import dk.aau.cs.giraf.voicegame.Settings.GameSettings;
import dk.aau.cs.giraf.voicegame.Track;
import dk.aau.cs.giraf.voicegame.game.Car;
import dk.aau.cs.giraf.voicegame.game.CarGame;
import dk.aau.cs.giraf.voicegame.game.Enums.MoveState;
import dk.aau.cs.giraf.voicegame.game.GameItemCollection;
import dk.aau.cs.giraf.voicegame.Interfaces.CarControl;
import dk.aau.cs.giraf.voicegame.game.RoadItem;

public class AvoidRunningScreen extends RunningScreen {
    public AvoidRunningScreen(CarGame game, Car car, GameItemCollection obstacles, CarControl carControl, float carSpeed, Track track, MoveState moveState) {
        super(game, car, obstacles, carControl, carSpeed, track, moveState);
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents, deltaTime);


        RoadItem roadItem = getCollisionRoadItem();
        if (roadItem != null) {
            Assets.GetCrash().PlayAndReset();
            showCrashScreen(roadItem);
        }
    }
}
