package dk.aau.cs.giraf.cars;

import android.app.Activity;
import android.os.Bundle;
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

        spinner1 = (Spinner)findViewById(R.id.spinner);
        spinner2= (Spinner)findViewById(R.id.spinner2);
        spinner3 = (Spinner)findViewById(R.id.spinner3);
        speed = (TextView)findViewById(R.id.speed);

        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.colors_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        spinner3.setAdapter(adapter);

        setContentView(R.layout.activity_settings);
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
        colors.add((Integer)spinner1.getSelectedItem());
        colors.add((Integer)spinner2.getSelectedItem());
        colors.add((Integer)spinner3.getSelectedItem());

        GameSettings gs = new GameSettings(colors, Integer.parseInt(speed.getText().toString()));


    }

}
