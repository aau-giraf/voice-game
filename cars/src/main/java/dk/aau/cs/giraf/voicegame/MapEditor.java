package dk.aau.cs.giraf.voicegame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import dk.aau.cs.giraf.gui.GirafInflatableDialog;
import dk.aau.cs.giraf.voicegame.CarsGames.CarsActivity;
import dk.aau.cs.giraf.voicegame.Interfaces.Drawable;
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
    private long currentId;
    // Holder of screen settings
    private MapScreen mapScreen;
    private Bitmap screenshot;
    private GirafInflatableDialog saveDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if (intent.hasExtra(DatabaseHelper.CHILD_ID))
            currentId = intent.getLongExtra(DatabaseHelper.CHILD_ID, 0);
        else throw new IllegalArgumentException("no child id");

        DatabaseHelper database = new DatabaseHelper(this);
        database.Initialize(currentId);

        if(intent.hasExtra("settings")){
            gamesettings = (GameSettings)intent.getSerializableExtra("settings");
        } else {
            gamesettings = new GameSettings(); //Default settings
        }

    }

    @Override
    public Screen getFirstScreen() {

        mapScreen = new MapScreen(this);
        return mapScreen;
    }

    @Override
    public View getContentView(final FastRenderView renderview) {
        FrameLayout frameLayout = new FrameLayout(this);
        LinearLayout linearLayout = new LinearLayout(this);

        // adding trash button
        android.graphics.drawable.Drawable trashCan = this.getResources().getDrawable(R.drawable.trashcan);
        GirafButton trashButton = new GirafButton(this, trashCan);
        trashButton.setY(5);
        trashButton.setX(5);
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
        saveButton.setX(this.getWidth() - ((this.getHeight() / 100) * 17 ));

        // currently does the same as the trash button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screenshot = renderview.getScreenshot();
                saveDialog = GirafInflatableDialog.newInstance("Gem bane", "Her kan du se et billede af din bane", R.layout.activity_save_dialog, SAVE_DIALOG_ID);
                saveDialog.show(getSupportFragmentManager(), SAVE_DIALOG_TAG);
            }
        });
        
        linearLayout.addView(saveButton);

        frameLayout.addView(renderview);
        frameLayout.addView(linearLayout);

        return frameLayout;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplication());
        databaseHelper.Initialize(currentId);

        databaseHelper.SaveSettings(gamesettings);

        this.finish();
    }

    @Override
    public void editCustomView(final ViewGroup viewGroup, int i) {
        if(i == SAVE_DIALOG_ID) {

            ImageView screenshotImage = (ImageView)viewGroup.findViewById(R.id.saveDialogScreenshot);
            screenshotImage.setImageBitmap(screenshot);

            GirafButton saveButton = (GirafButton) viewGroup.findViewById(R.id.button_gem);
            GirafButton cancelButton = (GirafButton) viewGroup.findViewById(R.id.button_anuller);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Track trackToSave = new Track(1, "Bane 1", mapScreen.roadItems);
                    String fileName = "/sdcard/TracksFile";
                    try{
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
                        oos.writeObject(trackToSave);
                        oos.close();
                    } catch (FileNotFoundException e){
                        System.out.println("File not found - output");
                        e.printStackTrace();
                    } catch (IOException e){
                        System.out.println("IO exception happened while writing");
                        e.printStackTrace();
                    }

                    // TODO make the dialog close after succesful save
                    saveDialog.dismiss();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO make the dialog close
                    saveDialog.dismiss();
                }
            });


        }
    }

    private class MapScreen extends SettingsScreen {
        private final int grassSize = 70;
        private final int finishLineScale = 15;
        private final int REMOVE_BUFFER_MANHATTAN = 5;
        private int finishLineX;

        private ArrayList<RoadItem> roadItems;
        private HashMap<String, Float> map;

        private RoadItem dragItem = null;
        private Point dragStart = new Point(0, 0);
        private boolean canRemove = false;

        public MapScreen(Game game) {
            super(game);
            setCarX(-getCarWidth());

            roadItems = new ArrayList<RoadItem>();
            map = new HashMap<String, Float>();

            DatabaseHelper databaseHelper = new DatabaseHelper(getApplication());
            databaseHelper.Initialize(currentId);

            roadItems = gamesettings.LoadObstacles();
            map = gamesettings.GetMap();

            this.finishLineX = game.getWidth() - 80;
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

                            if (dragItem == null) {
                                dragItem = Add(e.x - gamesettings.OBSTACLE_SIZE / 2, e.y - gamesettings.OBSTACLE_SIZE / 2);
                                canRemove = false;
                            }
                            break;

                        case Input.TouchEvent.TOUCH_DRAGGED:
                            if (dragItem != null) {
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
            roadItems.clear();
            map = new HashMap<String, Float>();
            gamesettings.SetMap(map);
        }

        public void AddObstacle(HashMap<String, Float> map, float x, float y, int index) {
            map.put("x" + index, x);
            map.put("y" + index, y);
            map.put("count", (float) index + 1);
        }

        // Gets called when an object is added in the map editor screen
        private RoadItem Add(float x, float y) {
            int index = roadItems.size();

            RoadItem roadItem = new RoadItem(x, y, gamesettings.OBSTACLE_SIZE, gamesettings.OBSTACLE_SIZE, gamesettings.GetGameMode());
            roadItems.add(roadItem);
            AddObstacle(map, x, y, index);
            gamesettings.SetMap(map);

            return roadItem;


        }

        // Gets called when an object is removed in the map editor screen
        private void Remove(RoadItem roadItem) {
            int index = roadItems.indexOf(roadItem);
            int size = roadItems.size();

            HashMap<String, Float> map = gamesettings.GetMap();

            for (int i = index; i < size - 1; i++) {
                RoadItem r = roadItems.get(i + 1);
                map.put("x" + i, r.x);
                map.put("y" + i, r.y);
            }
            map.remove("x" + (size - 1));
            map.remove("y" + (size - 1));

            map.put("count", (float) size - 1);
            roadItems.remove(roadItem);

            gamesettings.SetMap(map);
        }

        // Gets called when an objects position is updated in the map editor screen
        private void updateItem(RoadItem roadItem, float x, float y) {
            int index = roadItems.indexOf(roadItem);
            map.put("x" + index, x);
            map.put("y" + index, y);
            roadItem.x = x;
            roadItem.y = y;

            // updates the barometer number of the star (The number indicating how high the sound volume must be in order for the car to reach the star)
            roadItem.setBarometerNumber();
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

