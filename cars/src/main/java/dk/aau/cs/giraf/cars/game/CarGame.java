package dk.aau.cs.giraf.cars.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import dk.aau.cs.giraf.cars.framework.GameActivity;
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
        gamesettings = i.getParcelableExtra("GameSettings");
        super.onCreate(savedInstanceState);


        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }

    public void ShowStartOverlay() {
        setScreen(startOverlay);
    }

    public void ShowRunningScreen() {
        setScreen(runningScreen);
    }
}
