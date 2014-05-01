package dk.aau.cs.giraf.cars.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import dk.aau.cs.giraf.cars.framework.GameActivity;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.game.CarsGames.CarsActivity;

public class CarGame extends CarsActivity {
    GameSettings gamesettings;

    @Override
    public Screen getFirstScreen() {
        return new GameScreen(this, new PreferencesObstacles(gamesettings), gamesettings);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        gamesettings = i.getParcelableExtra("GameSettings");
        super.onCreate(savedInstanceState);


        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }
}
