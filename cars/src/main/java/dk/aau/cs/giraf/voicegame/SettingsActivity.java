package dk.aau.cs.giraf.voicegame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
//import com.google.analytics.tracking.android.EasyTracker;
import dk.aau.cs.giraf.activity.GirafActivity;
import dk.aau.cs.giraf.gui.GirafButton;
import dk.aau.cs.giraf.voicegame.Settings.CalibrationFragment;
import dk.aau.cs.giraf.voicegame.game.GameMode;
import dk.aau.cs.giraf.voicegame.Settings.GameSettings;
import dk.aau.cs.giraf.voicegame.Settings.SpeedFragment;
import dk.aau.cs.giraf.gui.GColorPicker;
import dk.aau.cs.giraf.gui.GComponent;

public class SettingsActivity extends GirafActivity{
    GameSettings gamesettings;
    long current_id;

    GirafButton saveSettingsButton, cancelSettingsButton;
    ColorButton colorPickButton;

    SpeedFragment speed;
    CalibrationFragment calibration;

    GameMode gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        long guardianId = 0;
        DatabaseHelper database = new DatabaseHelper(this);

        if(intent.hasExtra(DatabaseHelper.CHILD_ID))
            current_id = intent.getLongExtra(DatabaseHelper.CHILD_ID, 0);
        if(current_id == -1)
        current_id = intent.getLongExtra(DatabaseHelper.GUARDIAN_ID, database.GetChildDefaultGuardian());

        Log.d("childid", "Childid ved Settings create: " + current_id);

        database.Initialize(current_id);
        gamesettings = database.GetGameSettings();
        View v = LayoutInflater.from(this).inflate(R.layout.activity_settings, null);
        v.setBackgroundColor(GComponent.GetBackgroundColor());
        setContentView(v);
        
        saveSettingsButton = (GirafButton) findViewById(R.id.saveSettings);
        cancelSettingsButton = (GirafButton) findViewById(R.id.settingsCancel);
        colorPickButton = (ColorButton) findViewById(R.id.colorPick);
        speed = (SpeedFragment) getFragmentManager().findFragmentById(R.id.speed);
        calibration = (CalibrationFragment)getFragmentManager().findFragmentById(R.id.calibration_fragment);

        speed.setSpeed(gamesettings.GetSpeed());
        speed.setCarColor(gamesettings.GetColor());
        calibration.SetMinVolume(gamesettings.GetMinVolume());
        calibration.SetMaxVolume(gamesettings.GetMaxVolume());
        colorPickButton.SetColor(gamesettings.GetColor());

        initializeGameMode();
    }
/*
    //Google analytics - start logging
    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);  // Start logging
    }
    //Google analytics - Stop logging
    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);  // stop logging
    }
*/
    public void ColorPickClick(View view) {
        final ColorButton button = (ColorButton)view;
        GColorPicker colorPicker = new GColorPicker(view.getContext(), new GColorPicker.OnOkListener() {
            @Override
            public void OnOkClick(GColorPicker diag, int color) {
                button.SetColor(color);
                speed.setCarColor(color);
            }
        });
        colorPicker.SetCurrColor(button.GetColor());
        colorPicker.show();
    }

    @Override
    public void onBackPressed() {
        Log.d("database","map " + Boolean.toString(gamesettings.GetMap() == null));
        GameSettings gs = new GameSettings(colorPickButton.GetColor(), speed.getSpeed(), calibration.GetMinVolume(), calibration.GetMaxVolume(),gamesettings.GetMap(), gameMode);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.Initialize(current_id);
        databaseHelper.SaveSettings(gs);

        this.finish();
    }
    public void onSaveSettingsClicked(View v){

    }

    public void onCancelSettingsClicked(View v){

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
