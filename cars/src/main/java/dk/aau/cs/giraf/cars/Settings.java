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

import java.util.LinkedList;


public class Settings extends Activity {

    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    TextView speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        spinner1 = (Spinner)findViewById(R.id.spinner1);
        spinner2= (Spinner)findViewById(R.id.spinner2);
        spinner3 = (Spinner)findViewById(R.id.spinner3);
        speed = (TextView)findViewById(R.id.speed);

        Log.d("settings",String.valueOf(spinner1 == null));
        Log.d("settings",String.valueOf (spinner2 == null));
        Log.d("settings",String.valueOf (spinner3 == null));

        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.colorname_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        spinner3.setAdapter(adapter);


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

        int[] colorValues = new int[]{Color.CYAN,Color.GREEN,Color.MAGENTA,Color.BLUE,Color.YELLOW};


        colors.add(colorValues[spinner1.getSelectedItemPosition()]);
        colors.add(colorValues[spinner2.getSelectedItemPosition()]);
        colors.add(colorValues[spinner3.getSelectedItemPosition()]);

        Log.d("Settings",colors.toString());
        GameSettings gs = new GameSettings(colors, Integer.parseInt(speed.getText().toString()));

        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra("GameSettings",gs);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }



}
