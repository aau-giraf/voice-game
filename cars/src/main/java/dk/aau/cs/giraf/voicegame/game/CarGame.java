package dk.aau.cs.giraf.voicegame.game;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;

import dk.aau.cs.giraf.voicegame.Assets;
import dk.aau.cs.giraf.voicegame.Settings.GameSettings;
import dk.aau.cs.giraf.game_framework.Screen;
import dk.aau.cs.giraf.voicegame.CarsGames.CarsActivity;
import dk.aau.cs.giraf.voicegame.Track;
import dk.aau.cs.giraf.voicegame.game.Controller.VolumeCarControl;
import dk.aau.cs.giraf.voicegame.Interfaces.CarControl;
import dk.aau.cs.giraf.voicegame.game.GameScreens.FailureScreen;
import dk.aau.cs.giraf.voicegame.game.GameScreens.PauseScreen;
import dk.aau.cs.giraf.voicegame.game.GameScreens.RunningScreen;
import dk.aau.cs.giraf.voicegame.game.GameScreens.StartScreen;
import dk.aau.cs.giraf.voicegame.game.GameScreens.WinningScreen;

public class CarGame extends CarsActivity {
    private final int GRASS_HEIGHT = 70;
    private GameMode gameMode;

    GameSettings gamesettings;
    StartScreen startScreen;
    PauseScreen pauseScreen;
    WinningScreen winningScreen;
    RunningScreen runningScreen;
    FailureScreen failureScreen;
    private CarControl carControl;

    public CarGame() {
        super();
    }

    /**
     * Initializes the screens and gets the GameMode enum from the current track that is being played.
     * @return
     */
    @Override
    public Screen getFirstScreen() {
        Track track = (Track)getIntent().getSerializableExtra("track");
        this.gameMode = track.getMode();
        GameItemCollection roadItems = new GameItemCollection(new PreferencesObstacles(this.gamesettings));
        Car car = new Car(20 - Assets.GetCar().getWidth(), Resources.getSystem().getDisplayMetrics().heightPixels / 2);
        car.setShowValue(true);
        
        car.setColor(gamesettings.GetColor());
        carControl = new VolumeCarControl(gamesettings.GetMinVolume(), gamesettings.GetMaxVolume());

        startScreen = new StartScreen(this, car, roadItems);
        pauseScreen = new PauseScreen(this, car, roadItems, GRASS_HEIGHT);
        winningScreen = new WinningScreen(this, car, roadItems);
        failureScreen = new FailureScreen(this, car, roadItems);
        runningScreen = new RunningScreen(this, car, roadItems, carControl, gamesettings.GetSpeed(), (Track)getIntent().getSerializableExtra("track"), gamesettings.GetSoundMode());

        return startScreen;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void showStartScreen() {
        setScreen(startScreen);
    }

    /**
     * Use the failure screen as a crash screen.
     * @param gameItem
     */
    public void showCrashScreen(GameItem gameItem) {
        failureScreen.setLastCrash(failureScreen.getCollisionPoint(gameItem));
        setScreen(failureScreen);
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
        gamesettings = GameSettings.LoadSettings(getApplicationContext());//Might be a hack
        if (carControl instanceof VolumeCarControl)
            ((VolumeCarControl) carControl).Start();
    }
}
