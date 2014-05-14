package dk.aau.cs.giraf.cars.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import dk.aau.cs.giraf.cars.DatabaseHelper;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.game.CarsGames.CarsActivity;
import dk.aau.cs.giraf.cars.game.Controller.TouchCarControl;
import dk.aau.cs.giraf.cars.game.Controller.VolumeCarControl;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;
import dk.aau.cs.giraf.cars.game.Overlay.CrashScreen;
import dk.aau.cs.giraf.cars.game.Overlay.PauseScreen;
import dk.aau.cs.giraf.cars.game.Overlay.RunningScreen;
import dk.aau.cs.giraf.cars.game.Overlay.StartScreen;
import dk.aau.cs.giraf.cars.game.Overlay.WinningScreen;

public class CarGame extends CarsActivity {
    private final int GRASS_HEIGHT = 70;

    GameSettings gamesettings;
    StartScreen startScreen;
    CrashScreen crashScreen;
    PauseScreen pauseScreen;
    WinningScreen winningScreen;
    RunningScreen runningScreen;
    private CarControl carControl;

    public CarGame() {
        super();
    }

    @Override
    public Screen getFirstScreen() {
        ObstacleCollection obstacles = new ObstacleCollection(new PreferencesObstacles(this.gamesettings));
        Car car = new Car(-Assets.GetCar().getWidth(), getHeight() - GRASS_HEIGHT - Assets.GetCar().getHeight());
        car.setColor(gamesettings.GetColor());
        carControl = new VolumeCarControl(gamesettings.GetMinVolume(), gamesettings.GetMaxVolume());
        //carControl = new TouchCarControl(getHeight() - 2 * GRASS_HEIGHT - (int) car.height, GRASS_HEIGHT + (int) car.height / 2);

        startScreen = new StartScreen(this, car, obstacles);
        crashScreen = new CrashScreen(this, car, obstacles);
        pauseScreen = new PauseScreen(this, car, obstacles, GRASS_HEIGHT);
        winningScreen = new WinningScreen(this, car, obstacles);
        runningScreen = new RunningScreen(this, car, obstacles, carControl, gamesettings.GetSpeed());

        return startScreen;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        int child_id = i.getIntExtra(DatabaseHelper.CHILD_ID, 0);

        DatabaseHelper database = new DatabaseHelper(this);
        database.Initialize(child_id);

        gamesettings = database.GetGameSettings();

        super.onCreate(savedInstanceState);


        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }

    public void showStartScreen() {
        setScreen(startScreen);
    }

    public void showCrashScreen(GameItem gameItem) {
        crashScreen.setLastCrash(crashScreen.getCollisionPoint(gameItem));
        setScreen(crashScreen);
    }

    public void showPauseScreen() {
        setScreen(pauseScreen);
    }

    public void showWinningScreen() {
        setScreen(winningScreen);
    }

    public void showRunningScreen() {
        setScreen(runningScreen);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (carControl instanceof VolumeCarControl)
            ((VolumeCarControl) carControl).Stop();
    }
}
