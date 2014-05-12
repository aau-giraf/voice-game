package dk.aau.cs.giraf.cars.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.provider.ContactsContract;
import dk.aau.cs.giraf.cars.DatabaseHelper;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.game.CarsGames.CarsActivity;
import dk.aau.cs.giraf.cars.game.Overlay.CrashOverlay;
import dk.aau.cs.giraf.cars.game.Overlay.PauseOverlay;
import dk.aau.cs.giraf.cars.game.Overlay.RunningScreen;
import dk.aau.cs.giraf.cars.game.Overlay.StartOverlay;
import dk.aau.cs.giraf.cars.game.Overlay.WinningOverlay;

public class CarGame extends CarsActivity {
    GameSettings gamesettings;
    StartOverlay startOverlay;
    CrashOverlay crashOverlay;
    PauseOverlay pauseOverlay;
    WinningOverlay winningOverlay;
    RunningScreen runningScreen;

    public CarGame() {
        super();
    }

    @Override
    public Screen getFirstScreen() {
        PreferencesObstacles obstacles = new PreferencesObstacles(this);

        startOverlay = new StartOverlay(this, obstacles, gamesettings);
        crashOverlay = new CrashOverlay(this, obstacles, gamesettings);
        pauseOverlay = new PauseOverlay(this, obstacles, gamesettings);
        winningOverlay = new WinningOverlay(this, obstacles, gamesettings);
        runningScreen = new RunningScreen(this, obstacles, gamesettings);

        return startOverlay;
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
        setScreen(startOverlay);
    }

    public void showCrashScreen(GameItem gameItem) {
        crashOverlay.setCrashPoint(gameItem);
        setScreen(crashOverlay);
    }

    public void showPauseScreen() {
        setScreen(pauseOverlay);
    }

    public void showWinningScreen() {
        setScreen(winningOverlay);
    }

    public void showRunningScreen() {
        setScreen(runningScreen);
    }
}
