package dk.aau.cs.giraf.voicegame;

import android.app.ActionBar;
import android.content.Intent;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import java.util.ArrayList;

import dk.aau.cs.giraf.activity.GirafActivity;
import dk.aau.cs.giraf.gui.GComponent;
import dk.aau.cs.giraf.gui.GList;
import dk.aau.cs.giraf.gui.GirafButton;
import dk.aau.cs.giraf.voicegame.Interfaces.Drawable;
import dk.aau.cs.giraf.voicegame.game.CarGame;
import dk.aau.cs.giraf.voicegame.game.GameItem;

/**
 * The activity from where the user can see and choose the tracks that have been saved
 */
public class TrackPickerActivity extends GirafActivity {

    private static final int PLAY_BUTTON_ID = 1;
    private int listObjectClicked;
    private ArrayList<Integer> trackArrayList;
    private TrackOrganizer trackOrganizer = null;
    private GList trackList;
    private Track track;
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

        ArrayList<Integer> array = new ArrayList<>();
        array.add(0);
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(4);
        array.add(5);

        ListAdapter adapter = new TrackListAdapter(this, array);
        GList trackList = (GList) findViewById(R.id.list_tracks);

        trackOrganizer = IOService.instance().readTrackOrganizerFromFile();

        updateTrackArrayList();

        trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listObjectClicked = (int)parent.getItemAtPosition(position);
                Toast.makeText(TrackPickerActivity.this, String.valueOf(listObjectClicked), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void createPlayButton() {
        GirafButton playButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_play));
        //button.setId(R.id.button_playtrack);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrackPickerActivity.this, CarGame.class);
                intent.putExtra("settings", getIntent().getExtras());
                //intent.putExtra("track", )
                startActivity(intent);
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
                trackOrganizer.deleteTrack(listObjectClicked);
                updateTrackArrayList();
                System.out.println("It should be deleted by now");
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

    private void updateTrackArrayList(){
        trackArrayList = new ArrayList<>();
        if(trackOrganizer == null) System.out.println("trackorganizer is null");
        if(!trackOrganizer.getArray().isEmpty()){
            for (Track track: trackOrganizer.getArray()) {
                if(track != null){
                    trackArrayList.add(track.getID());
                }

            }
        }



        ListAdapter adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, trackArrayList);
        trackList = (GList) findViewById(R.id.list_tracks);

        trackList.setAdapter(adapter);
    }
}
