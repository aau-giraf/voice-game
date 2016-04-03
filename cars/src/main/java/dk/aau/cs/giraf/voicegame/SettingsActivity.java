package dk.aau.cs.giraf.voicegame;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import com.google.analytics.tracking.android.EasyTracker;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import dk.aau.cs.giraf.activity.GirafActivity;
import dk.aau.cs.giraf.gui.GirafButton;
import dk.aau.cs.giraf.voicegame.Settings.CalibrationFragment;
import dk.aau.cs.giraf.voicegame.Settings.SpeedGauge;
import dk.aau.cs.giraf.voicegame.game.GameMode;
import dk.aau.cs.giraf.voicegame.Settings.GameSettings;
import dk.aau.cs.giraf.voicegame.Settings.SpeedFragment;
import dk.aau.cs.giraf.gui.GColorPicker;
import dk.aau.cs.giraf.gui.GComponent;

public class SettingsActivity extends GirafActivity{
    protected GameSettings gameSettings, initSettings;

    GirafButton saveSettingsButton, cancelSettingsButton;
    ColorButton colorPickButton;

    SpeedFragment speed;
    CalibrationFragment calibration;

    GameMode gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if(intent.hasExtra("settings")) {
            gameSettings = (GameSettings) intent.getSerializableExtra("settings");
        }
        initSettings = gameSettings;

        View v = LayoutInflater.from(this).inflate(R.layout.activity_settings, null);
        v.setBackgroundColor(GComponent.GetBackgroundColor());
        setContentView(v);

        saveSettingsButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_save));
        cancelSettingsButton = new GirafButton(this,getResources().getDrawable(R.drawable.icon_cancel));

        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick method for saveSettingsButton
             * @param v
             */
            @Override
            public void onClick(View v) {
                SaveSettings(new GameSettings(colorPickButton.GetColor(), speed.getSpeed(), calibration.GetMinVolume(),
                        calibration.GetMaxVolume(),/*gameSettings.GetMap(),*/ gameMode), getApplicationContext());
            }
        });

        cancelSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameSettings = initSettings;
            }
        });
        addGirafButtonToActionBar(saveSettingsButton, GirafActivity.RIGHT);
        addGirafButtonToActionBar(cancelSettingsButton, GirafActivity.RIGHT);

        colorPickButton = (ColorButton) findViewById(R.id.colorPick);
        speed = (SpeedFragment) getFragmentManager().findFragmentById(R.id.speed);
        calibration = (CalibrationFragment)getFragmentManager().findFragmentById(R.id.calibration_fragment);

        speed.setSpeed(gameSettings.GetSpeed());
        speed.setCarColor(gameSettings.GetColor());
        calibration.SetMinVolume(gameSettings.GetMinVolume());
        calibration.SetMaxVolume(gameSettings.GetMaxVolume());
        colorPickButton.SetColor(gameSettings.GetColor());

        initializeGameMode();
    }

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
        //TODO Cancel changed settings
        //TODO Add "There are unsaved changes" dialog
        Intent result = new Intent();
        result.putExtra("settings", gameSettings);
        setResult(Activity.RESULT_OK, result);
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
        if(gameMode == null){
            gameMode = gameSettings.GetGameMode();//Get default
        }
        if (gameMode==GameMode.pickup) {
            ((RadioButton) findViewById(R.id.radioButtonpPickup)).setChecked(true);
            ((RadioButton) findViewById(R.id.radioButtonAvoid)).setChecked(false);
        }
        if (gameMode==GameMode.avoid) {
            ((RadioButton) findViewById(R.id.radioButtonAvoid)).setChecked(true);
            ((RadioButton) findViewById(R.id.radioButtonpPickup)).setChecked(false);
        }
    }

    /**
     * Saves object GameSettings, which implements Serializable to local file.
     */
    public static void SaveSettings(GameSettings gs, Context context){
        if(gs == null){
            gs = new GameSettings();//Causes creation of file with default settings
        }
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput("vg_settings", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(fos);
            os.writeObject(gs);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSettings = gameSettings;
    }
}
