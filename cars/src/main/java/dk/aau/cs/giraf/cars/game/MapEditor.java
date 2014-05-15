package dk.aau.cs.giraf.cars.game;

import android.content.Intent;
import android.graphics.Color;
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
    private Obstacle dragging = null;
    private Obstacle startDrag = null;
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
        private int finishLineX;

        private ArrayList<Obstacle> obstacles;
        private HashMap<String, Float> map;

        public MapScreen(Game game) {
            super(game);
            setCarX(-getCarWidth());

            obstacles = new ArrayList<Obstacle>();
            map = new HashMap<String, Float>();

            DatabaseHelper databaseHelper = new DatabaseHelper(getApplication());
            databaseHelper.Initialize(currentId);

            obstacles = gamesettings.LoadObstacles();
            map = gamesettings.GetMap();

            this.finishLineX = game.getWidth()-80;
        }


        @Override
        public void paint(Graphics graphics, float deltaTime) {
            super.paint(graphics, deltaTime);
            drawFinishLine(graphics);
            for (Obstacle o : obstacles)
                o.Draw(graphics, deltaTime);
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
                if (e.y > grassSize && e.y < game.getHeight() - grassSize) {
                    if (e.type == Input.TouchEvent.TOUCH_DRAGGED)
                        if (dragging != null) {
                            Remove(dragging);
                            dragging = Add(e.x - gamesettings.OBSTACLE_SIZE / 2, e.y - gamesettings.OBSTACLE_SIZE / 2);
                        }
                    if (e.type == Input.TouchEvent.TOUCH_DOWN) {
                        Obstacle rem = getObstacleAt(e.x, e.y);
                        if (rem == null) {
                            dragging = Add(e.x - gamesettings.OBSTACLE_SIZE / 2, e.y - gamesettings.OBSTACLE_SIZE / 2);
                        } else {
                            dragging = rem;
                            startDrag = rem;
                        }
                    } else if (e.type == Input.TouchEvent.TOUCH_UP) {
                        if (startDrag == dragging)
                            Remove(startDrag);
                        dragging = null;

                    }
                }
            }
        }

        private Obstacle getObstacleAt(int x, int y) {
            for (Obstacle o : obstacles)
                if (o.GetBounds().contains(x, y)) {
                    return o;
                }
            return null;
        }

        public void AddObstacle(HashMap<String, Float> map, float x, float y, int index) {
            map.put("x" + index, x);
            map.put("y" + index, y);
            map.put("count", (float) index + 1);

        }

        private Obstacle Add(float x, float y) {
            int index = obstacles.size();
            Obstacle o = new Obstacle(x, y, gamesettings.OBSTACLE_SIZE, gamesettings.OBSTACLE_SIZE);
            obstacles.add(o);
            AddObstacle(map, x, y, index);
            gamesettings.SetMap(map);
            return o;
        }

        private void Remove(Obstacle obstacle) {
            int index = obstacles.indexOf(obstacle);
            int size = obstacles.size();

            HashMap<String, Float> map = gamesettings.GetMap();

            for (int i = index; i < size - 1; i++) {
                Obstacle o = obstacles.get(i + 1);
                map.put("x" + i, o.x);
                map.put("y" + i, o.y);
            }
            map.remove("x" + (size - 1));
            map.remove("y" + (size - 1));

            map.put("count", (float) size - 1);
            obstacles.remove(obstacle);

            gamesettings.SetMap(map);
        }

        public void Clear() {
            obstacles.clear();
            map = new HashMap<String, Float>();
            gamesettings.SetMap(map);
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

