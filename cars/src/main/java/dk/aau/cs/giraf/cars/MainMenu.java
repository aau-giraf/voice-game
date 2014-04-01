package dk.aau.cs.giraf.cars;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import dk.aau.cs.giraf.cars.game.CarGame;
import dk.aau.cs.giraf.cars.game.GameSettings;

import java.util.Arrays;
import java.util.LinkedList;

public class MainMenu extends Activity {

    private final static int SETTINGS_IDENTIFIER = 0;

    GameSettings gamesettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Log.d("Settings",String.valueOf(intent.hasExtra("GameSettings")));
        if(intent.hasExtra("GameSettings"))
            gamesettings = intent.getParcelableExtra("GameSettings");
        else gamesettings = new GameSettings();


        setContentView(R.layout.activity_main_menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void startGame(View view)
    {
        Intent intent =  new Intent(this, CarGame.class);
        intent.putExtra("GameSettings",gamesettings);
        startActivity(intent);
    }

    public void showSettings(View view)
    {
        Intent intent =  new Intent(this, Settings.class);
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
