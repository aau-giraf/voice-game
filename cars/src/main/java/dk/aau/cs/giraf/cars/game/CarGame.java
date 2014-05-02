package dk.aau.cs.giraf.cars.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.provider.ContactsContract;
import dk.aau.cs.giraf.cars.DatabaseHelper;
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
        int child_id = i.getIntExtra(DatabaseHelper.CHILD_ID,0);

        DatabaseHelper database = new DatabaseHelper(this);
        database.Initialize(child_id);

        gamesettings = database.GetGameSettings();

        super.onCreate(savedInstanceState);


        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }
}
