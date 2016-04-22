package dk.aau.cs.giraf.voicegame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import dk.aau.cs.giraf.gui.GirafInflatableDialog;
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

public class MapEditor extends CarsActivity implements GirafInflatableDialog.OnCustomViewCreatedListener {
    private GameSettings gamesettings;
    // Holder of screen settings
    private MapScreen mapScreen;
    private Bitmap screenshot;
    private GirafInflatableDialog saveDialog;
    private FastRenderView renderView;
    private boolean actualBack = false;
    private boolean isChanged = false;

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
        android.graphics.drawable.Drawable trashCan = this.getResources().getDrawable(R.drawable.trashcan);
        GirafButton trashButton = new GirafButton(this, trashCan);
        trashButton.setY(5);
        // width is 1280px
        // It is not possible to get the dimensions of the icon, so we're subtracting a percentage of the screens width, in order to accommodate scaling.
        trashButton.setX(this.getWidth() - ((this.getHeight() / 100) * 25));
        trashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapScreen.Clear();
            }
        });

        linearLayout.addView(trashButton);

        // adding save button
        android.graphics.drawable.Drawable saveIcon = this.getResources().getDrawable(R.drawable.icon_save);
        GirafButton saveButton = new GirafButton(this, saveIcon);
        saveButton.setY(5);

        // width is 1280px
        // It is not possible to get the dimensions of the icon, so we're subtracting a percentage of the screens width, in order to accommodate scaling.
        saveButton.setX(this.getWidth() - ((this.getHeight() / 100) * 20 ));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screenshot = renderview.getScreenshot();
                saveDialog = GirafInflatableDialog.newInstance(getResources().getString(R.string.save_dialog_title), getResources().getString(R.string.save_dialog_text), R.layout.activity_save_dialog, SAVE_DIALOG_ID);
                saveDialog.show(getSupportFragmentManager(), SAVE_DIALOG_TAG);
            }
        });

        linearLayout.addView(saveButton);

        //adding back button
        android.graphics.drawable.Drawable iconBack = this.getResources().getDrawable(R.drawable.icon_back);
        GirafButton backButton = new GirafButton(this, iconBack);
        backButton.setY(5);
        backButton.setX(-95);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        linearLayout.addView(backButton);

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
            saveDialog = GirafInflatableDialog.newInstance(getResources().getString(R.string.header_unsaved_map), getResources().getString(R.string.unsaved_map_text), R.layout.activity_save_dialog, UNSAVED_DIALOG_ID);
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
            GirafButton cancelButton = (GirafButton) viewGroup.findViewById(R.id.button_anuller);
            GirafButton backButton = (GirafButton) viewGroup.findViewById(R.id.button_back);

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
                backButton.setVisibility(viewGroup.INVISIBLE);
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

                    // TODO temporary check, some error happens when adding more than 6 tracks. Will make task aswell.
                    } else {
                        if(trackOrganizer.canSaveMoreTracks()) {
                            String bitmapPath = IOService.instance().writeNewBitmapToFile(screenshot, String.valueOf(trackOrganizer.getNextFreeID()));
                            trackOrganizer.addTrack(mapScreen.roadItems, bitmapPath, gamesettings.GetGameMode());
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

        }
    }
}

