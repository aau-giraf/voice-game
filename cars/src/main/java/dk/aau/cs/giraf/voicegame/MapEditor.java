package dk.aau.cs.giraf.voicegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import dk.aau.cs.giraf.activity.GirafActivity;
import dk.aau.cs.giraf.gui.GirafInflatableDialog;
import dk.aau.cs.giraf.showcaseview.ShowcaseManager;
import dk.aau.cs.giraf.showcaseview.ShowcaseView;
import dk.aau.cs.giraf.showcaseview.targets.ViewTarget;
import dk.aau.cs.giraf.voicegame.CarsGames.CarsActivity;
import dk.aau.cs.giraf.voicegame.game.GameMode;
import dk.aau.cs.giraf.voicegame.game.RoadItem;
import dk.aau.cs.giraf.voicegame.Settings.GameSettings;
import dk.aau.cs.giraf.voicegame.Settings.SettingsScreen;
import dk.aau.cs.giraf.game_framework.FastRenderView;
import dk.aau.cs.giraf.game_framework.Game;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.game_framework.Screen;
import dk.aau.cs.giraf.gui.GirafButton;

public class MapEditor extends CarsActivity implements GirafInflatableDialog.OnCustomViewCreatedListener, ShowcaseManager.ShowcaseCapable {
    private GameSettings gamesettings;
    // Holder of screen settings
    private MapScreen mapScreen;
    private Bitmap screenshot;
    private GirafInflatableDialog saveDialog;
    private FastRenderView renderView;
    private boolean actualBack = false;
    private boolean isChanged = false;

    // Used for showcase
    private static final String IS_FIRST_RUN_KEY = "IS_FIRST_RUN_KEY_MAP_EDITOR";
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    private ShowcaseManager showcaseManager;
    private boolean isFirstRun;

    GirafButton helpGirafButton;
    GirafButton saveButton;
    GirafButton trashButton;
    GirafButton backButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if(intent.hasExtra("settings")){
            gamesettings = (GameSettings)intent.getSerializableExtra("settings");
        } else {
            gamesettings = new GameSettings(); //Default settings
        }
    }

    /**
     * gets the mapscreen which is instantied. The false value indicate that the mapscreen is by default working with a blank map.
     * If set to true, the activity starting the MapEditor activity must supply a track under the name of "track" as extras. TrackPickerActivity does so.
     * @return the MapScreen that was requested
     */
    @Override
    public Screen getFirstScreen() {

        mapScreen = new MapScreen(this, getIntent().getBooleanExtra("edit", false));
        return mapScreen;
    }

    /**
     * Sets the content of the view. Button are created here
     * @param renderview the class that handles rendering
     * @return
     */
    @Override
    public View getContentView(final FastRenderView renderview) {
        FrameLayout frameLayout = new FrameLayout(this);
        LinearLayout linearLayout = new LinearLayout(this);
        renderView = renderview;

        // adding trash button
        android.graphics.drawable.Drawable trashCan = this.getResources().getDrawable(R.drawable.icon_delete);
        trashButton = new GirafButton(this, trashCan);
        trashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapScreen.Clear();
            }
        });
         addGirafButtonToActionBar(trashButton, GirafActivity.RIGHT);

        // adding help button
        helpGirafButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_help));

        helpGirafButton.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick method for helpGirafButton
             * @param v
             */
            @Override
            public void onClick(View v) {
                MapEditor.this.toggleShowcase();
            }
        });
        addGirafButtonToActionBar(helpGirafButton, GirafActivity.RIGHT);

        // adding save button
        android.graphics.drawable.Drawable saveIcon = this.getResources().getDrawable(R.drawable.icon_save);
        saveButton = new GirafButton(this, saveIcon);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screenshot = renderview.getScreenshot();
                saveDialog = GirafInflatableDialog.newInstance(getResources().getString(R.string.save_dialog_title), getResources().getString(R.string.save_dialog_text), R.layout.activity_save_dialog, SAVE_DIALOG_ID);
                saveDialog.show(getSupportFragmentManager(), SAVE_DIALOG_TAG);
            }
        });

        addGirafButtonToActionBar(saveButton, GirafActivity.RIGHT);

        frameLayout.addView(renderview);
        frameLayout.addView(linearLayout);

        return frameLayout;
    }

    @Override
    public void onBackPressed() {
        //The back press is only to terminate mapEditor, if changes is saved or user does not want to save.
        if (actualBack || !isChanged) {
            super.onBackPressed();
            this.finish();
        } else {
            screenshot = renderView.getScreenshot();
            saveDialog = GirafInflatableDialog.newInstance(getResources().getString(R.string.header_unsaved), getResources().getString(R.string.unsaved_text), R.layout.activity_save_dialog, UNSAVED_DIALOG_ID);
            saveDialog.show(getSupportFragmentManager(), SAVE_DIALOG_TAG);
        }
    }

    /**
     * and override method from implementing GirafInflatableDialog.OnCustomViewCreatedListener
     * Content of the dialog goes in this method
     * @param viewGroup the views inside the dialog, access this when editing views.
     * @param i the id of the dialog
     */
    @Override
    public void editCustomView(final ViewGroup viewGroup, int i) {
        if(i == SAVE_DIALOG_ID || i == UNSAVED_DIALOG_ID) {

            ImageView screenshotImage = (ImageView)viewGroup.findViewById(R.id.saveDialogScreenshot);
            screenshotImage.setImageBitmap(screenshot);

            final GirafButton saveButton = (GirafButton) viewGroup.findViewById(R.id.button_gem);
            final GirafButton backButton = (GirafButton) viewGroup.findViewById(R.id.button_back);
            final GirafButton cancelButton = (GirafButton) viewGroup.findViewById(R.id.button_annuller);

            //changes on buttons, if the popup is created from the save button or the back button.
            if (i == UNSAVED_DIALOG_ID) {
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actualBack = true;
                        onBackPressed();
                    }
                });
                saveButton.setContentDescription("actualBack");
            } else {
                backButton.setVisibility(viewGroup.GONE);
                backButton.setEnabled(false);
            }

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveDialog.dismiss();
                }
            });

            /**
             * Reads the trackOrganizer from file and overrides it, containing the newly created track aswell.
             */
            saveButton.setOnClickListener(new View.OnClickListener() {

                /**
                 * When clicking the save button, the track will be edited, if the "edit" extra is set to true, and call the appropriate method in the trackOrganizer
                 * @param v the view that was clicked
                 */
                @Override
                public void onClick(View v) {
                    // Read the trackorganizer from file
                    TrackOrganizer trackOrganizer = IOService.instance().readTrackOrganizerFromFile();

                    // Add a track to the trackorganizer
                    // If the "edit" bool is flipped, then the edited track is overwritten, else we create a new track
                    if (getIntent().getBooleanExtra("edit", false)) {
                        Track track = (Track)getIntent().getSerializableExtra("track");
                        IOService.instance().overwriteBitmapToFile(screenshot, track.getScreenshotPath(), String.valueOf(track.getID()));
                        trackOrganizer.editTrack(track.getID(), mapScreen.roadItems);

                    } else {
                        if(trackOrganizer.canSaveMoreTracks()) {

                            trackOrganizer.addTrack(screenshot, mapScreen.roadItems, gamesettings.GetGameMode());
                        } else {
                            Toast.makeText(MapEditor.this, "Du kan ikke gemme flere baner", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //Write the trackorganizer to the file.
                    IOService.instance().writeTrackOrganizerToFile(trackOrganizer);
                    gamesettings.setRoadItems(mapScreen.roadItems);

                    //If the dialog is the unsaved changes, the button terminates the MapEditor.
                    if (saveButton.getContentDescription() == "actualBack") {
                        actualBack = true;
                        onBackPressed();
                    } else {
                        //If it is normal save, it only closes the save popup.
                        isChanged = false;
                        saveDialog.dismiss();
                    }
                }
            });
        }
    }

    /**
     * This class controls what obstacle is placed.
     * IS also reads in old obstacles, if a track is being edited.
     */
    private class MapScreen extends SettingsScreen {
        private final int grassSize = 70;
        private final int finishLineScale = 15;
        private final int REMOVE_BUFFER_MANHATTAN = 5;
        private int finishLineX;

        private ArrayList<RoadItem> roadItems;

        private RoadItem dragItem = null;
        private Point dragStart = new Point(0, 0);
        private boolean canRemove = false;
        private GameMode mode;

        public MapScreen(Game game, boolean editMap) {
            super(game);
            setCarX(-getCarWidth());

            if(editMap){
                Track track = (Track)getIntent().getSerializableExtra("track");
                track.initRoadItems();
                roadItems = track.getObstacleArray();
            } else {
                roadItems = new ArrayList<RoadItem>();
            }

            this.finishLineX = game.getWidth() - 80;
            
            if(editMap) {
                Track track = (Track)getIntent().getSerializableExtra("track");
                mode = track.getMode();
            } else {
                mode = gamesettings.GetGameMode();
            }

        }

        @Override
        public void paint(Graphics graphics, float deltaTime) {
            super.paint(graphics, deltaTime);
            drawFinishLine(graphics);
            for (RoadItem roadItem : roadItems)
                roadItem.Draw(graphics, deltaTime);
        }

        private void drawFinishLine(Graphics graphics) {
            int height = game.getHeight() - 2 * grassSize, squareHeight = height / finishLineScale, squareWidth = 40, width = 120;
            graphics.drawRect(finishLineX, grassSize, width, height, Color.WHITE);
            for (int i = squareHeight; i < height; i += 2 * squareHeight)
                graphics.drawRect(finishLineX, grassSize + i, 41, squareHeight + 1, Color.BLACK);
            for (int i = 0; i < height; i += 2 * squareHeight)
                graphics.drawRect(finishLineX + squareWidth, grassSize + i, 41, squareHeight + 1, Color.BLACK);
        }

        @Override
        public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
            for (Input.TouchEvent e : touchEvents) {
                if (isInsideMapBounds(e.x, e.y)) {
                    switch (e.type) {
                        case Input.TouchEvent.TOUCH_DOWN:
                            dragItem = getObstacleAt(e.x, e.y);
                            dragStart.x = e.x;
                            dragStart.y = e.y;
                            canRemove = true;
                            isChanged = true;

                            if (dragItem == null) {
                                dragItem = Add(e.x - gamesettings.OBSTACLE_SIZE / 2, e.y - gamesettings.OBSTACLE_SIZE / 2);
                                canRemove = false;
                            }

                            break;

                        case Input.TouchEvent.TOUCH_DRAGGED:
                            if (dragItem != null) {
                                isChanged = true;
                                updateItem(dragItem, e.x - gamesettings.OBSTACLE_SIZE / 2, e.y - gamesettings.OBSTACLE_SIZE / 2);
                                if (Math.abs(dragStart.x - e.x) + Math.abs(dragStart.y - e.y) > REMOVE_BUFFER_MANHATTAN)
                                    canRemove = false;
                            }
                            break;

                        case Input.TouchEvent.TOUCH_UP:
                            if (canRemove)
                                Remove(dragItem);
                            dragItem = null;
                    }
                }
            }
        }

        private boolean isInsideMapBounds(int x, int y) {
            return y > grassSize && y < game.getHeight() - grassSize;
        }

        private RoadItem getObstacleAt(int x, int y) {
            for (RoadItem roadItem : roadItems)
                if (roadItem.GetBounds().contains(x, y)) {
                    return roadItem;
                }
            return null;
        }

        private void Clear() {
            roadItems = new ArrayList<RoadItem>();
            gamesettings.setRoadItems(roadItems);
        }

        // Gets called when an object is added in the map editor screen
        private RoadItem Add(float x, float y) {
            int index = roadItems.size();

            RoadItem roadItem = new RoadItem(x, y, gamesettings.OBSTACLE_SIZE, gamesettings.OBSTACLE_SIZE, mode);
            roadItems.add(roadItem);
            gamesettings.setRoadItems(roadItems);

            return roadItem;


        }

        // Gets called when an object is removed in the map editor screen
        private void Remove(RoadItem roadItem) {
            int index = roadItems.indexOf(roadItem);
            int size = roadItems.size();

            roadItems.remove(roadItem);

            gamesettings.setRoadItems(roadItems);
        }

        // Gets called when an objects position is updated in the map editor screen
        private void updateItem(RoadItem roadItem, float x, float y) {
            int index = roadItems.indexOf(roadItem);

            roadItem.x = x;
            roadItem.y = y;

            // updates the barometer number of the star (The number indicating how high the sound volume must be in order for the car to reach the star)
            roadItem.setBarometerNumber();
        }

        public void setRoadItems(ArrayList<RoadItem> roadItems) {
            this.roadItems = roadItems;
        }

        @Override
        public void pause() {

        }

        @Override
        public void resume() {
            gamesettings = GameSettings.LoadSettings(getApplicationContext());//Might be a hack
        }

        @Override
        public void dispose() {

        }

        @Override
        public void backButton() {
            onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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

                    synchronized (MapEditor.this) {

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            MapEditor.this.findViewById(android.R.id.content).getViewTreeObserver().removeGlobalOnLayoutListener(globalLayoutListener);
                        } else {
                            MapEditor.this.findViewById(android.R.id.content).getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
                        }

                        globalLayoutListener = null;
                    }
                }
            });
        }
    }

    /**
     * Showcaseview that explain the functionality
     */
    @Override
    public synchronized void showShowcase() {

        // Targets for the Showcase
        final ViewTarget trashButtonTarget = new ViewTarget(trashButton, 1.5f);
        final ViewTarget saveButtonTarget = new ViewTarget(saveButton, 1.5f);

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
                MapEditor.this.toggleShowcase();
            }
        });

        // Calculate position for the help text
        final int textX = getResources().getDisplayMetrics().widthPixels / 2 + margin;
        final int textY = getResources().getDisplayMetrics().heightPixels / 2 + margin;

        showcaseManager = new ShowcaseManager();

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcaseX(getResources().getDisplayMetrics().widthPixels / 2);
                showcaseView.setShowcaseY(getResources().getDisplayMetrics().heightPixels / 2);
                showcaseView.setContentTitle(getString(R.string.place_item_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.place_item_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.addView(stopButton, stopLps);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(saveButtonTarget, true);
                showcaseView.setContentTitle(getString(R.string.save_button_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.save_button_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(trashButtonTarget, true);
                showcaseView.setContentTitle(getString(R.string.trash_button_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.trash_button_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.removeView(stopButton);
                showcaseView.addView(stopButton, lps);
                showcaseView.setTextPostion(textX, textY);
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

