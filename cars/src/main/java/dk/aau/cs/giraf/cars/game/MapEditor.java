package dk.aau.cs.giraf.cars.game;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

import dk.aau.cs.giraf.cars.DatabaseHelper;
import dk.aau.cs.giraf.cars.framework.FastRenderView;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.game.CarsGames.CarsActivity;
import dk.aau.cs.giraf.gui.GButtonTrash;

public class MapEditor extends CarsActivity {
    private GameSettings gamesettings;
    private int currentId;
    private MapScreen mapScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if (intent.hasExtra(DatabaseHelper.CHILD_ID))
            currentId = intent.getIntExtra(DatabaseHelper.CHILD_ID, 0);
        else throw new IllegalArgumentException("no child id");

        DatabaseHelper database = new DatabaseHelper(this);
        database.Initialize(currentId);

        gamesettings = database.GetGameSettings();
    }

    @Override
    public Screen getFirstScreen() {
        mapScreen = new MapScreen(this);
        return mapScreen;
    }

    @Override
    public View getContentView(FastRenderView renderview) {
        FrameLayout frameLayout = new FrameLayout(this);
        LinearLayout linearLayout = new LinearLayout(this);

        GButtonTrash trashButton = new GButtonTrash(this);
        trashButton.setY(5);
        trashButton.setX(5);
        trashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapScreen.Clear();
            }
        });

        linearLayout.addView(trashButton);

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

        private RoadItem Add(float x, float y) {
            int index = roadItems.size();
            RoadItem roadItem = new RoadItem(x, y, gamesettings.OBSTACLE_SIZE, gamesettings.OBSTACLE_SIZE, gamesettings.GetGameMode());
            roadItems.add(roadItem);
            AddObstacle(map, x, y, index);
            gamesettings.SetMap(map);
            return roadItem;
        }

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

        private void updateItem(RoadItem roadItem, float x, float y) {
            int index = roadItems.indexOf(roadItem);
            map.put("x" + index, x);
            map.put("y" + index, y);
            roadItem.x = x;
            roadItem.y = y;
        }

        @Override
        public void pause() {

        }

        @Override
        public void resume() {

        }

        @Override
        public void dispose() {

        }

        @Override
        public void backButton() {

        }
    }
}

