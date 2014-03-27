package dk.aau.cs.giraf.cars.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import dk.aau.cs.giraf.cars.framework.GameActivity;
import dk.aau.cs.giraf.cars.framework.Screen;

public class CarGame extends GameActivity {
    GameSettings gamesettings;

    @Override
    public Screen getInitScreen() {
        return new LoadingScreen(this,gamesettings);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        gamesettings = i.getParcelableExtra("GameSettings");
        super.onCreate(savedInstanceState);


        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }
}
