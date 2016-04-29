package dk.aau.cs.giraf.voicegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import dk.aau.cs.giraf.activity.GirafActivity;
import dk.aau.cs.giraf.gui.GComponent;
import dk.aau.cs.giraf.gui.GList;
import dk.aau.cs.giraf.gui.GirafButton;
import dk.aau.cs.giraf.showcaseview.ShowcaseManager;
import dk.aau.cs.giraf.showcaseview.ShowcaseView;
import dk.aau.cs.giraf.showcaseview.targets.ViewTarget;
import dk.aau.cs.giraf.voicegame.Settings.GameSettings;
import dk.aau.cs.giraf.voicegame.game.CarGame;
import dk.aau.cs.giraf.voicegame.game.RoadItem;

/**
 * The activity from where the user can see and choose the tracks that have been saved
 */
public class TrackPickerActivity extends GirafActivity implements ShowcaseManager.ShowcaseCapable {

    private static final int PLAY_BUTTON_ID = 1;
    private ArrayList<Integer> trackArrayList;
    private TrackOrganizer trackOrganizer = null;
    private GList trackList;
    private Track track;
    private GameSettings settings;
    private int savedPosition = -1;
    GirafButton helpGirafButton, playButton, deleteButton, editButton;

    // Used for showcaseview
    private static final String IS_FIRST_RUN_KEY = "IS_FIRST_RUN_KEY_TRACK_PICKER_ACTIVITY";
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    private ShowcaseManager showcaseManager;
    private boolean isFirstRun;

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

        createHelpButton();
        createDeleteButton();
        createEditButton();
        createPlayButton();

        settings = (GameSettings)getIntent().getSerializableExtra("settings");

        setContentView(v);

        track = null;
    }

    private void createHelpButton() {
        helpGirafButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_help));

        helpGirafButton.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick method for helpGirafButton
             * @param v
             */
            @Override
            public void onClick(View v) {
                TrackPickerActivity.this.toggleShowcase();
            }
        });

        addGirafButtonToActionBar(helpGirafButton, GirafActivity.RIGHT);
    }

    /**
     * Creates the play button, shown in the toolbar
     */
    private void createPlayButton() {
        playButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_play));

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
        deleteButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_delete));

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
        editButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_edit));

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

        Log.v("TrackPickerActivity", "trackArray size: " + trackArrayList.size() + ", dummy size: " + numberOfRows.size());
        
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

        // Check if this is the first run of the app
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.isFirstRun = prefs.getBoolean(IS_FIRST_RUN_KEY, true);

        // If it is the first run display ShowcaseView
        if (isFirstRun) {
            this.findViewById(android.R.id.content).getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    showShowcase();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(IS_FIRST_RUN_KEY, false);
                    editor.commit();

                    synchronized (TrackPickerActivity.this) {

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            TrackPickerActivity.this.findViewById(android.R.id.content).getViewTreeObserver().removeGlobalOnLayoutListener(globalLayoutListener);
                        } else {
                            TrackPickerActivity.this.findViewById(android.R.id.content).getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
                        }

                        globalLayoutListener = null;
                    }
                }
            });
        }
    }

    public void setTrack(int trackID) {
        this.track = trackOrganizer.getTrack(trackID);
    }

    /**
     * Showcaseview that explain the functionality
     */
    public synchronized void showShowcase() {

        // Targets for the Showcase
        final ViewTarget playButtonTarget = new ViewTarget(playButton, 1.5f);
        final ViewTarget deleteButtonTarget = new ViewTarget(deleteButton, 1.5f);
        final ViewTarget editButtonTarget = new ViewTarget(editButton, 1.5f);
        final ViewTarget trackTarget = new ViewTarget(R.id.list_tracks,this, 0.2f);

        // Create a relative location for the next button
        final RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        final int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);

        // End button
        final RelativeLayout.LayoutParams stopLps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        stopLps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        stopLps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        final Button stopButton = (Button) LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.showcase_button, null);
        stopButton.setBackgroundColor(getResources().getColor(R.color.giraf_button_fill_end)); // Showcase Button background color
        stopButton.setText("Luk");
        stopLps.setMargins(margin, margin, margin, margin);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackPickerActivity.this.toggleShowcase();
            }
        });

        // Calculate position for the help text
        final int textX = getResources().getDisplayMetrics().widthPixels / 2 + margin;
        final int textY = getResources().getDisplayMetrics().heightPixels / 2 + margin;

        // Calculate position for the help text to right
        final int textX_right = (getResources().getDisplayMetrics().widthPixels / 3) * 2;

        showcaseManager = new ShowcaseManager();

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(playButtonTarget, true);
                showcaseView.setContentTitle(getString(R.string.play_button_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.play_button_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.addView(stopButton, stopLps);
                showcaseView.setTextPostion(textX, textY);
            }
        });

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(editButtonTarget, true);
                showcaseView.setContentTitle(getString(R.string.edit_button_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.edit_button_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(deleteButtonTarget, true);
                showcaseView.setContentTitle(getString(R.string.delete_button_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.delete_button_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(trackTarget, true);
                showcaseView.setContentTitle(getString(R.string.track_list_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.track_list_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.removeView(stopButton);
                showcaseView.addView(stopButton, lps);
                showcaseView.setTextPostion(textX_right, textY);
            }
        });

        ShowcaseManager.OnDoneListener onDoneCallback = new ShowcaseManager.OnDoneListener() {
            @Override
            public void onDone(ShowcaseView showcaseView) {
                showcaseManager = null;
                isFirstRun = false;
            }
        };
        showcaseManager.setOnDoneListener(onDoneCallback);

        showcaseManager.start(this);
    }

    @Override
    public synchronized void hideShowcase() {

        if (showcaseManager != null) {
            showcaseManager.stop();
            showcaseManager = null;
        }
    }

    @Override
    public synchronized void toggleShowcase() {

        if (showcaseManager != null) {
            hideShowcase();
        } else {
            showShowcase();
        }
    }
}
