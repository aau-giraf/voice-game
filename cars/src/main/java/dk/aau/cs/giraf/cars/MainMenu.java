package dk.aau.cs.giraf.cars;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import dk.aau.cs.giraf.cars.game.CarGame;
import dk.aau.cs.giraf.cars.game.GameSettings;

import java.util.Arrays;
import java.util.LinkedList;

public class MainMenu extends Activity {

    GameSettings gamesettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(  intent.hasExtra("GameSettings"))
            gamesettings = intent.getParcelableExtra("GameSettings");
        else gamesettings = new GameSettings(new LinkedList<Integer>(Arrays.asList(Color.BLUE,Color.GREEN,Color.RED)), 70); //TODO make more convenient default gamesettings


        setContentView(R.layout.activity_main_menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
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
        startActivity(intent);
    }
}
