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

    /**
     * Called when the activity is started.
     * Instantiates the TrackListAdapter, which makes the content of list shown in this activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = LayoutInflater.from(this).inflate(R.layout.activity_track_picker, null);
        v.setBackgroundDrawable(GComponent.GetBackground(GComponent.Background.GRADIENT));

        createDeleteButton();
        createEditButton();
        createPlayButton();

        setContentView(v);
        trackOrganizer = IOService.instance().readTrackOrganizerFromFile();

        updateTrackArrayList();

        trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listObjectClicked = (int)parent.getItemAtPosition(position);
                Toast.makeText(TrackPickerActivity.this, String.valueOf(listObjectClicked), Toast.LENGTH_SHORT).show();
                track = trackOrganizer.getTrack(listObjectClicked);
            }
        });

    }

    /**
     * Creates the play button, shown in the toolbar
     */
    private void createPlayButton() {
        GirafButton playButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_play));

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrackPickerActivity.this, CarGame.class);
                intent.putExtra("settings", getIntent().getExtras());
              
                startActivity(intent);
            }
        });
        addGirafButtonToActionBar(playButton, GirafActivity.RIGHT);
    }

    /**
     * Creates the delete button, shown in the toolbar
     */
    private void createDeleteButton() {
        GirafButton deleteButton = new GirafButton(this, getResources().getDrawable(R.drawable.delete));

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Delete a track from the trackorganizer
                trackOrganizer.deleteTrack(listObjectClicked);
                //Write the trackorganizer to the file.
                IOService.instance().writeTrackOrganizerToFile(trackOrganizer);
                updateTrackArrayList();
            }
        });
        addGirafButtonToActionBar(deleteButton, GirafActivity.RIGHT);
    }

    /**
     * Creates the edit track button, shown in the toolbar
     */
    private void createEditButton() {
        GirafButton editButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_edit));

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(track != null) {
                    trackOrganizer = IOService.instance().readTrackOrganizerFromFile();
                    Intent intent = new Intent(TrackPickerActivity.this, MapEditor.class);
                    intent.putExtra("settings", getIntent().getBundleExtra("settings"));
                    intent.putExtra("edit", true);
                    intent.putExtra("track", track);
                    System.out.println("From TrackPickerActivity, Number of stars: " + track.getObstacleArray().size());
                    startActivity(intent);
                } else {
                    Toast.makeText(TrackPickerActivity.this, getResources().getString(R.string.track_pick_error), Toast.LENGTH_SHORT).show();
                }

            }
        });
        addGirafButtonToActionBar(editButton, GirafActivity.RIGHT);
    }

    private void updateTrackArrayList(){
        trackArrayList = new ArrayList<>();
        if(!trackOrganizer.getArray().isEmpty()){
            for (Track track: trackOrganizer.getArray()) {
                if(track != null){
                    trackArrayList.add(track.getID());
                }

            }
        }



        ListAdapter adapter = new TrackListAdapter(this, trackArrayList);
        trackList = (GList) findViewById(R.id.list_tracks);

        trackList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        trackOrganizer = IOService.instance().readTrackOrganizerFromFile();
        track = null;
    }
}
