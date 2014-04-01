package dk.aau.cs.giraf.cars;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import dk.aau.cs.giraf.cars.game.GameSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


public class Settings extends Activity {
    GameSettings gamesettings;

    ArrayList<Integer> colorValues = new ArrayList<Integer>() {{add(Color.RED);add(Color.GREEN);add(Color.MAGENTA);add(Color.BLUE);add(Color.YELLOW);}};

    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    TextView speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent.hasExtra("GameSettings"))
            gamesettings = intent.getParcelableExtra("GameSettings");

        setContentView(R.layout.activity_settings);

        spinner1 = (Spinner)findViewById(R.id.spinner1);
        spinner2= (Spinner)findViewById(R.id.spinner2);
        spinner3 = (Spinner)findViewById(R.id.spinner3);
        speed = (TextView)findViewById(R.id.speed);


        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.colorname_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        spinner3.setAdapter(adapter);

        Log.d("Settings",Integer.toString(gamesettings.GetSpeed()));
        Log.d("Settings",gamesettings.GetColors().toString());

        speed.setText(Integer.toString(gamesettings.GetSpeed()));

        LinkedList<Integer> colors = gamesettings.GetColors();
        Log.d("Settings","Colors at creation of settings" + colors);
        spinner1.setSelection(colorValues.indexOf(colors.get(0)));
        spinner2.setSelection(colorValues.indexOf(colors.get(1)));
        spinner3.setSelection(colorValues.indexOf(colors.get(2)));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
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

    public void OkButtonClick(View view)
    {
        LinkedList<Integer> colors = new LinkedList<Integer>();

        colors.add(colorValues.get(spinner1.getSelectedItemPosition()));
        colors.add(colorValues.get(spinner2.getSelectedItemPosition()));
        colors.add(colorValues.get(spinner3.getSelectedItemPosition()));

        GameSettings gs = new GameSettings(colors, Integer.parseInt(speed.getText().toString()));

        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra("GameSettings",gs);
        startActivity(intent);
    }



}
