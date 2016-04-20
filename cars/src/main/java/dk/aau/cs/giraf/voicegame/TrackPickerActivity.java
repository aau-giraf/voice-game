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

import java.io.Console;
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
import dk.aau.cs.giraf.voicegame.Settings.GameSettings;
import dk.aau.cs.giraf.voicegame.game.CarGame;
import dk.aau.cs.giraf.voicegame.game.GameItem;
import dk.aau.cs.giraf.voicegame.game.RoadItem;

/**
 * The activity from where the user can see and choose the tracks that have been saved
 */
public class TrackPickerActivity extends GirafActivity {

    private static final int PLAY_BUTTON_ID = 1;
    private int listObjectClicked = -1;
    private ArrayList<Integer> trackArrayList;
    private TrackOrganizer trackOrganizer = null;
    private GList trackList;
    private Track track;
    private GameSettings settings;
    private int savedPosition = -1;

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

        settings = (GameSettings)getIntent().getSerializableExtra("settings");

        setContentView(v);
        
        trackOrganizer = IOService.instance().readTrackOrganizerFromFile();

        updateTrackArrayList();

        this.trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * This method is called each time a row in the list is clicked
             *
             * @param parent   the parent view
             * @param view     the row view that was clicked
             * @param position the position in the list that was clicked
             * @param id       view id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listObjectClicked = (int) parent.getItemAtPosition(position);
                Toast.makeText(TrackPickerActivity.this, String.valueOf(listObjectClicked), Toast.LENGTH_SHORT).show();
                track = trackOrganizer.getTrack(listObjectClicked);
                trackOrganizer.setCurrentTrackID(track.getID());


                parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.listRowFocused));

                if (savedPosition != -1 && savedPosition != position) {
                    if (parent.getChildAt(savedPosition) != null) {
                        parent.getChildAt(savedPosition).setBackgroundColor(getResources().getColor(R.color.listBackground));
                    }
                }

                savedPosition = position;
            }
        });
    }

    /**
     * Creates the play button, shown in the toolbar
     */
    private void createPlayButton() {
        GirafButton playButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_play));

        /**
         * This method will be called when the play button is pressen.
         * It checks if the user have pressed a row, giving track a value, if not then track is given a standard value, mkaing the game start with an empty track.
         * If track has been given a value, that specific track is played.
         */
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(track == null) {
                    GameSettings settings = (GameSettings)getIntent().getSerializableExtra("settings");
                    track = new Track(-1, new ArrayList<RoadItem>(), settings.GetGameMode());
                    settings.setRoadItems(new ArrayList<RoadItem>());
                } else {
                    settings.setRoadItems(track.getObstacleArray());
                }

                Intent intent = new Intent(TrackPickerActivity.this, CarGame.class);
                intent.putExtra("settings", getIntent().getSerializableExtra("settings"));
                intent.putExtra("track", track);
                IOService.instance().writeTrackOrganizerToFile(trackOrganizer);
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

                track = null;
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
                    intent.putExtra("settings", getIntent().getSerializableExtra("settings"));
                    intent.putExtra("edit", true);
                    intent.putExtra("track", track);
                    startActivity(intent);
                } else {
                    Toast.makeText(TrackPickerActivity.this, getResources().getString(R.string.track_pick_error), Toast.LENGTH_SHORT).show();
                }

            }
        });
        addGirafButtonToActionBar(editButton, GirafActivity.RIGHT);
    }

    private void updateTrackArrayList(){
        trackArrayList = new ArrayList<Integer>();
        if(!trackOrganizer.getArray().isEmpty()){
            for (Track track: trackOrganizer.getArray()) {
                if(track != null){
                    trackArrayList.add(track.getID());
                }

            }
        }
        ListAdapter adapter = new TrackListAdapter(this, trackArrayList);
        this.trackList = (GList) findViewById(R.id.list_tracks);

        this.trackList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        trackOrganizer = IOService.instance().readTrackOrganizerFromFile();
        if(listObjectClicked == -1) {
            track = null;
        } else {
            track = trackOrganizer.getTrack(listObjectClicked);
        }
    }
}
