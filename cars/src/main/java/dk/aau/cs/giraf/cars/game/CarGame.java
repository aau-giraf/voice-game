package dk.aau.cs.giraf.cars.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import dk.aau.cs.giraf.cars.DatabaseHelper;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.game.CarsGames.CarsActivity;
import dk.aau.cs.giraf.cars.game.Overlay.CrashScreen;
import dk.aau.cs.giraf.cars.game.Overlay.PauseScreen;
import dk.aau.cs.giraf.cars.game.Overlay.RunningScreen;
import dk.aau.cs.giraf.cars.game.Overlay.StartScreen;
import dk.aau.cs.giraf.cars.game.Overlay.WinningScreen;

public class CarGame extends CarsActivity {
    GameSettings gamesettings;
    StartScreen startScreen;
    CrashScreen crashScreen;
    PauseScreen pauseScreen;
    WinningScreen winningScreen;
    RunningScreen runningScreen;

    public CarGame() {
        super();
    }

    @Override
    public Screen getFirstScreen() {
        PreferencesObstacles obstacles = new PreferencesObstacles(this);

        startScreen = new StartScreen(this, obstacles, gamesettings);
        crashScreen = new CrashScreen(this, obstacles, gamesettings);
        pauseScreen = new PauseScreen(this, obstacles, gamesettings);
        winningScreen = new WinningScreen(this, obstacles, gamesettings);
        runningScreen = new RunningScreen(this, obstacles, gamesettings);

        return startScreen;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        int child_id = i.getIntExtra(DatabaseHelper.CHILD_ID,0);

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
        crashScreen.setCrashPoint(gameItem);
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
}
