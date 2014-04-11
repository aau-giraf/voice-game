package dk.aau.cs.giraf.cars;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

import dk.aau.cs.giraf.cars.game.CalibrationFragment;
import dk.aau.cs.giraf.cars.game.GameSettings;
import dk.aau.cs.giraf.cars.game.SpeedFragment;


public class Settings extends Activity {
    GameSettings gamesettings;

    ArrayList<Integer> colorValues = new ArrayList<Integer>() {{
        add(Color.RED);
        add(Color.GREEN);
        add(Color.MAGENTA);
        add(Color.BLUE);
        add(Color.YELLOW);
    }};

    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;

    SpeedFragment speed;
    CalibrationFragment calibration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra("GameSettings"))
            gamesettings = intent.getParcelableExtra("GameSettings");

        setContentView(R.layout.activity_settings);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        speed = (SpeedFragment) getFragmentManager().findFragmentById(R.id.speed);
        calibration = (CalibrationFragment)getFragmentManager().findFragmentById(R.id.calibration_fragment);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.colorname_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        spinner3.setAdapter(adapter);

        speed.setSpeed(gamesettings.GetSpeed());
        calibration.SetMinVolume(gamesettings.GetMinVolume());
        calibration.SetMaxVolume(gamesettings.GetMaxVolume());

        LinkedList<Integer> colors = gamesettings.GetColors();
        spinner1.setSelection(colorValues.indexOf(colors.get(0)));
        spinner2.setSelection(colorValues.indexOf(colors.get(1)));
        spinner3.setSelection(colorValues.indexOf(colors.get(2)));
    }

    public void OkButtonClick(View view) {
        LinkedList<Integer> colors = new LinkedList<Integer>();

        colors.add(colorValues.get(spinner1.getSelectedItemPosition()));
        colors.add(colorValues.get(spinner2.getSelectedItemPosition()));
        colors.add(colorValues.get(spinner3.getSelectedItemPosition()));

        GameSettings gs = new GameSettings(colors, (int)speed.getSpeed(), calibration.GetMinVolume(),calibration.GetMaxVolume());

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("GameSettings", gs);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }
}
