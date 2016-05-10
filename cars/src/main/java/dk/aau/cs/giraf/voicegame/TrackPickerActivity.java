package dk.aau.cs.giraf.voicegame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import dk.aau.cs.giraf.activity.GirafActivity;
import dk.aau.cs.giraf.gui.GComponent;
import dk.aau.cs.giraf.gui.GList;
import dk.aau.cs.giraf.gui.GirafButton;
import dk.aau.cs.giraf.voicegame.Settings.GameSettings;
import dk.aau.cs.giraf.voicegame.game.CarGame;
import dk.aau.cs.giraf.voicegame.game.RoadItem;

/**
 * The activity from where the user can see and choose the tracks that have been saved
 */
public class TrackPickerActivity extends GirafActivity {

    private static final int PLAY_BUTTON_ID = 1;
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

        track = null;
    }

    /**
     * Creates the play button, shown in the toolbar
     */
    private void createPlayButton() {
        GirafButton playButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_play));

        /**
         * This method will be called when the play button is pressen.
         * It checks if the user have pressed a row, giving track a value, if not then track is given a standard value, making the game start with an empty track.
         * If track has been given a value, that specific track is played.
         */
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (track == null) {
                    GameSettings settings = (GameSettings) getIntent().getSerializableExtra("settings");
                    track = new Track(-1, new ArrayList<RoadItem>(), "", settings.GetGameMode());
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
        GirafButton deleteButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_delete));

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(track != null) {
                    //Delete a track from the trackorganizer
                    trackOrganizer.deleteTrack(track.getID());
                    //Write the trackorganizer to the file.
                    IOService.instance().writeTrackOrganizerToFile(trackOrganizer);
                    updateTrackArrayList();

                    track = null;
                } else {
                    Toast.makeText(TrackPickerActivity.this, getResources().getString(R.string.track_pick_error), Toast.LENGTH_SHORT).show();
                }


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

    /**
     * Counts all the tracks, and create a dummy array at the size of half the amount of tracks.
     * We do this, because we hold two tracks on each row.
     * Both the dummy array and the trackId array is passed tot he adapter when creating the list.
     */
    private void updateTrackArrayList(){
        trackArrayList = new ArrayList<Integer>();
        if(!trackOrganizer.getTrackArray().isEmpty()){
            for (Track track: trackOrganizer.getTrackArray()) {
                if(track != null){
                    trackArrayList.add(track.getID());
                }
            }
        }

        // create dummy array at half the size of trackArrayList.
        ArrayList<Integer> numberOfRows = new ArrayList<Integer>();

        for (int i = 0; i < Math.ceil((double)trackArrayList.size() / 2); i++) {
            numberOfRows.add(0);
        }
        
        ListAdapter adapter = new TrackListAdapter(this, numberOfRows, trackArrayList, trackOrganizer.getScreenshotArray(), trackOrganizer, this);
        this.trackList = (GList) findViewById(R.id.list_tracks);

        this.trackList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        trackOrganizer = IOService.instance().readTrackOrganizerFromFile();

        track = null;
        trackOrganizer.setCurrentTrackID(-1);
        updateTrackArrayList();
    }

    public void setTrack(int trackID) {
        this.track = trackOrganizer.getTrack(trackID);
    }
}
