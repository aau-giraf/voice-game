package dk.aau.cs.giraf.voicegame;

import android.app.ActionBar;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import dk.aau.cs.giraf.activity.GirafActivity;
import dk.aau.cs.giraf.gui.GComponent;
import dk.aau.cs.giraf.gui.GList;
import dk.aau.cs.giraf.gui.GirafButton;
import dk.aau.cs.giraf.voicegame.Interfaces.Drawable;
import dk.aau.cs.giraf.voicegame.game.GameItem;

public class TrackPickerActivity extends GirafActivity {

    private static final int PLAY_BUTTON_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = LayoutInflater.from(this).inflate(R.layout.activity_track_picker, null);
        v.setBackgroundDrawable(GComponent.GetBackground(GComponent.Background.GRADIENT));

        createDeleteButton();
        createEditButton();
        createPlayButton();

        setContentView(v);

        //ImageView trackImage1 = (ImageView) findViewById(R.id.image_track1);
        //ImageView image = (ImageView) findViewById(R.id.button_track1_image);
        //image.setBackgroundColor(Color.BLACK);

        String array[] = {"Track1", "Track2", "Track3"};
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        GList trackList = (GList) findViewById(R.id.list_tracks);

        trackList.setAdapter(adapter);

        trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String string = String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(TrackPickerActivity.this, string, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void createPlayButton() {
        GirafButton playButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_play));
        //button.setId(R.id.button_playtrack);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        addGirafButtonToActionBar(playButton, GirafActivity.RIGHT);
    }

    private void createDeleteButton() {
        GirafButton deleteButton = new GirafButton(this, getResources().getDrawable(R.drawable.delete));
        //button.setId(R.id.button_playtrack);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        addGirafButtonToActionBar(deleteButton, GirafActivity.RIGHT);
    }

    private void createEditButton() {
        GirafButton editButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_edit));
        //button.setId(R.id.button_playtrack);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        addGirafButtonToActionBar(editButton, GirafActivity.RIGHT);
    }

}
