package dk.aau.cs.giraf.cars;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;

import dk.aau.cs.giraf.cars.game.CalibrationFragment;
import dk.aau.cs.giraf.cars.game.GameSettings;
import dk.aau.cs.giraf.cars.game.SpeedFragment;
import dk.aau.cs.giraf.gui.GColorPicker;

public class Settings extends Activity {
    GameSettings gamesettings;
    int child_id;

    ColorButton colorPickButton1;
    ColorButton colorPickButton2;
    ColorButton colorPickButton3;


    SpeedFragment speed;
    CalibrationFragment calibration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra(DatabaseHelper.SETTINGS))
            gamesettings = intent.getParcelableExtra(DatabaseHelper.SETTINGS);

        if(intent.hasExtra(DatabaseHelper.CHILD_ID))
            child_id = intent.getIntExtra(DatabaseHelper.CHILD_ID, 0);

        setContentView(R.layout.activity_settings);

        colorPickButton1 = (ColorButton) findViewById(R.id.colorPick1);
        colorPickButton2 = (ColorButton) findViewById(R.id.colorPick2);
        colorPickButton3 = (ColorButton) findViewById(R.id.colorPick3);
        speed = (SpeedFragment) getFragmentManager().findFragmentById(R.id.speed);
        calibration = (CalibrationFragment)getFragmentManager().findFragmentById(R.id.calibration_fragment);

        speed.setSpeed(gamesettings.GetSpeed());
        calibration.SetMinVolume(gamesettings.GetMinVolume());
        calibration.SetMaxVolume(gamesettings.GetMaxVolume());

        LinkedList<Integer> colors = gamesettings.GetColors();
        colorPickButton1.SetColor(colors.get(0));
        colorPickButton2.SetColor(colors.get(1));
        colorPickButton3.SetColor(colors.get(2));
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
        LinkedList<Integer> colors = new LinkedList<Integer>();

        colors.add(colorPickButton1.GetColor());
        colors.add(colorPickButton2.GetColor());
        colors.add(colorPickButton3.GetColor());

        Log.d("database","map " + Boolean.toString(gamesettings.GetMap() == null));
        GameSettings gs = new GameSettings(colors, speed.getSpeed(), calibration.GetMinVolume(), calibration.GetMaxVolume(),gamesettings.GetMap());

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.Initialize(child_id);
        databaseHelper.SaveSettings(gs);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("GameSettings", gs);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }




}
