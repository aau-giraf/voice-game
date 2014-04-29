package dk.aau.cs.giraf.cars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import dk.aau.cs.giraf.cars.game.CarGame;
import dk.aau.cs.giraf.cars.game.GameSettings;
import dk.aau.cs.giraf.cars.game.MapEditor;
import dk.aau.cs.giraf.oasis.lib.controllers.ApplicationController;
import dk.aau.cs.giraf.oasis.lib.controllers.ProfileApplicationController;
import dk.aau.cs.giraf.oasis.lib.controllers.ProfileController;
import dk.aau.cs.giraf.oasis.lib.models.Application;
import dk.aau.cs.giraf.oasis.lib.models.Profile;
import dk.aau.cs.giraf.oasis.lib.models.ProfileApplication;
import dk.aau.cs.giraf.oasis.lib.models.Setting;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MainActivity extends Activity {

    private final static int SETTINGS_IDENTIFIER = 0;
    public static final String CHILD_ID = "currentChildID";

    GameSettings gamesettings;
    int child_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if(intent.hasExtra(CHILD_ID))
            child_id = intent.getIntExtra(CHILD_ID,0);

        DatabaseHelper database = new DatabaseHelper(this,child_id);
        gamesettings = database.ParseSettings(database.GetSettings());

        setContentView(R.layout.activity_main_menu);
    }



    public void startGame(View view)
    {
        Intent intent =  new Intent(this, CarGame.class);
        intent.putExtra("GameSettings", gamesettings);
        startActivity(intent);
    }

    public void startMapEditor(View view){
        Intent intent = new Intent(this, MapEditor.class);
        startActivity(intent);
    }

    public void showSettings(View view)
    {
        Intent intent =  new Intent(this, Settings.class);
        intent.putExtra("gamesettings",gamesettings);
        startActivityForResult(intent, SETTINGS_IDENTIFIER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case (SETTINGS_IDENTIFIER) : {
                if (resultCode == Activity.RESULT_OK)
                    gamesettings = data.getParcelableExtra("GameSettings");
                break;
            }
        }
    }


}
