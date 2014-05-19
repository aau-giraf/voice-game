package dk.aau.cs.giraf.cars.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import dk.aau.cs.giraf.cars.DatabaseHelper;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.game.CarsGames.CarsActivity;
import dk.aau.cs.giraf.cars.game.Controller.VolumeCarControl;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;
import dk.aau.cs.giraf.cars.game.Overlay.AvoidRunningScreen;
import dk.aau.cs.giraf.cars.game.Overlay.CrashScreen;
import dk.aau.cs.giraf.cars.game.Overlay.FailureScreen;
import dk.aau.cs.giraf.cars.game.Overlay.PauseScreen;
import dk.aau.cs.giraf.cars.game.Overlay.PickupRunningScreen;
import dk.aau.cs.giraf.cars.game.Overlay.RunningScreen;
import dk.aau.cs.giraf.cars.game.Overlay.StartScreen;
import dk.aau.cs.giraf.cars.game.Overlay.WinningScreen;

public class CarGame extends CarsActivity {
    private final int GRASS_HEIGHT = 70;
    private GameMode gameMode;

    GameSettings gamesettings;
    StartScreen startScreen;
    CrashScreen crashScreen;
    PauseScreen pauseScreen;
    WinningScreen winningScreen;
    RunningScreen runningScreen;
    FailureScreen failureScreen;
    private CarControl carControl;

    public CarGame() {
        super();
    }

    @Override
    public Screen getFirstScreen() {
        this.gameMode = gamesettings.GetGameMode();
        GameItemCollection roadItems = new GameItemCollection(new PreferencesObstacles(this.gamesettings));
        Car car = new Car(-Assets.GetCar().getWidth(), getHeight() - GRASS_HEIGHT - Assets.GetCar().getHeight());
        car.setShowValue(true);
        
        car.setColor(gamesettings.GetColor());
        carControl = new VolumeCarControl(gamesettings.GetMinVolume(), gamesettings.GetMaxVolume());
        //carControl = new TouchCarControl(getHeight() - 2 * GRASS_HEIGHT - (int) car.height, GRASS_HEIGHT + (int) car.height / 2);

        startScreen = new StartScreen(this, car, roadItems);
        pauseScreen = new PauseScreen(this, car, roadItems, GRASS_HEIGHT);
        winningScreen = new WinningScreen(this, car, roadItems);
        if (gameMode == GameMode.pickup){
            runningScreen = new PickupRunningScreen(this, car, roadItems, carControl, gamesettings.GetSpeed());
            failureScreen = new FailureScreen(this, car, roadItems);
        }
        if (gameMode == GameMode.avoid){
            runningScreen = new AvoidRunningScreen(this, car, roadItems, carControl, gamesettings.GetSpeed());
            crashScreen = new CrashScreen(this, car, roadItems);
        }
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

    public void showFailureScreen(){
        setScreen(failureScreen);
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
    public void onPause() {
        if (carControl instanceof VolumeCarControl)
            ((VolumeCarControl) carControl).Stop();

        ((GameScreen)getCurrentScreen()).freezeCar();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (carControl instanceof VolumeCarControl)
            ((VolumeCarControl) carControl).Start();
    }
}
