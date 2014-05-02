package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.GameActivity;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.framework.MoveSineLine;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.framework.mFloat;
import dk.aau.cs.giraf.cars.game.Controller.TouchCarControl;
import dk.aau.cs.giraf.cars.game.Controller.VolumeCarControl;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;

public abstract class GameScreen extends Screen {
    protected Car car;
    protected final mFloat verticalMover;
    protected final int grassSize = 70;
    protected CarControl carControl;

    private GameSettings gameSettings;
    private ObstacleGenerator obstacleGenerator;
    private GameActivity gameActivity;

    public GameActivity GetGameActivity() {
        return gameActivity;
    }

    public ObstacleGenerator GetObstacleGenerator() {
        return obstacleGenerator;
    }

    public GameSettings GetGameSettings() {
        return gameSettings;
    }

    public GameScreen(GameActivity game, ObstacleGenerator obstacleGenerator, GameSettings gs) {
        super(game);
        this.car = new Car(0, 0, 200, 99);
        this.gameActivity = game;
        this.obstacleGenerator = obstacleGenerator;
        this.gameSettings = gs;
        this.verticalMover = new mFloat(0, new MoveSineLine(0.5f, 200));

        this.carControl = new TouchCarControl(game.getHeight() - 2 * grassSize - (int)car.getHeight(), grassSize + (int)car.getHeight() / 2);
        //this.carControl = new VolumeCarControl(gs.GetMinVolume(), gs.GetMaxVolume());
    }

    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        if (car.x + car.width < animationZoneX)
            state = pauseOverlay.Update(touchEvents, deltaTime);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        if (carControl instanceof VolumeCarControl)
            ((VolumeCarControl) carControl).Stop();
    }

    @Override
    public void backButton() {

    }
}
