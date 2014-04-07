package dk.aau.cs.giraf.cars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import dk.aau.cs.giraf.cars.game.CarGame;
import dk.aau.cs.giraf.cars.game.GameSettings;

public class MainActivity extends Activity {

    private final static int SETTINGS_IDENTIFIER = 0;

    GameSettings gamesettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent.hasEcxtra("GameSettings"))
            gamesettings = intent.getParcelableExtra("GameSettings");
        else gamesettings = new GameSettings();

        setContentView(R.layout.activity_main_menu);
    }

    public void startGame(View view)
    {
        Intent intent =  new Intent(this, CarGame.class);
        intent.putExtra("GameSettings", gamesettings);
        startActivity(intent);
    }

    public void showSettings(View view)
    {
        Intent intent =  new Intent(this, Settings.class);
        intent.putExtra("GameSettings",gamesettings);
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
