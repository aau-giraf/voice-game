package dk.aau.cs.giraf.cars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import java.util.LinkedList;

import dk.aau.cs.giraf.cars.game.CalibrationFragment;
import dk.aau.cs.giraf.cars.game.GameMode;
import dk.aau.cs.giraf.cars.game.GameSettings;
import dk.aau.cs.giraf.cars.game.SpeedFragment;
import dk.aau.cs.giraf.gui.GColorPicker;
import dk.aau.cs.giraf.gui.GComponent;

public class Settings extends Activity {
    GameSettings gamesettings;
    int child_id;

    ColorButton colorPickButton;


    SpeedFragment speed;
    CalibrationFragment calibration;

    GameMode gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if(intent.hasExtra(DatabaseHelper.CHILD_ID))
            child_id = intent.getIntExtra(DatabaseHelper.CHILD_ID, 0);

        Log.d("childid","Childid ved Settings create: "+ child_id);

        DatabaseHelper database = new DatabaseHelper(this);
        database.Initialize(child_id);
        gamesettings = database.GetGameSettings();
        View v = LayoutInflater.from(this).inflate(R.layout.activity_settings, null);
        v.setBackgroundColor(GComponent.GetBackgroundColor());
        setContentView(v);

        colorPickButton = (ColorButton) findViewById(R.id.colorPick);
        speed = (SpeedFragment) getFragmentManager().findFragmentById(R.id.speed);
        calibration = (CalibrationFragment)getFragmentManager().findFragmentById(R.id.calibration_fragment);

        speed.setSpeed(gamesettings.GetSpeed());
        calibration.SetMinVolume(gamesettings.GetMinVolume());
        calibration.SetMaxVolume(gamesettings.GetMaxVolume());
        colorPickButton.SetColor(gamesettings.GetColor());

        initializeGameMode();
    }

    public void ColorPickClick(View view) {
        final ColorButton button = (ColorButton)view;
        GColorPicker diag = new GColorPicker(view.getContext(), new GColorPicker.OnOkListener() {
            @Override
            public void OnOkClick(GColorPicker diag, int color) {
                button.SetColor(color);
            }
        });
        diag.SetCurrColor(button.GetColor());
        diag.show();
    }

    @Override
    public void onBackPressed() {
        Log.d("database","map " + Boolean.toString(gamesettings.GetMap() == null));
        GameSettings gs = new GameSettings(colorPickButton.GetColor(), speed.getSpeed(), calibration.GetMinVolume(), calibration.GetMaxVolume(),gamesettings.GetMap(), gameMode);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.Initialize(child_id);
        databaseHelper.SaveSettings(gs);

        this.finish();
    }

    public void onRadioButtonClicked(View v)
    {
        switch(v.getId())
        {
            case R.id.radioButtonpPickup:
                gameMode = GameMode.pickup;
                break;
            case R.id.radioButtonAvoid:
                gameMode = GameMode.avoid;
                break;
        }
    }

    private void initializeGameMode(){
        this.gameMode = gamesettings.GetGameMode();
        if (gameMode==GameMode.pickup)
            ((RadioButton)findViewById(R.id.radioButtonpPickup)).setChecked(true);
        if (gameMode==GameMode.avoid)
            ((RadioButton)findViewById(R.id.radioButtonAvoid)).setChecked(true);
    }

}
