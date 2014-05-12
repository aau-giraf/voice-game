package dk.aau.cs.giraf.cars.game;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

import dk.aau.cs.giraf.cars.DatabaseHelper;
import dk.aau.cs.giraf.cars.MainActivity;
import dk.aau.cs.giraf.cars.framework.FastRenderView;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.game.CarsGames.CarsActivity;
import dk.aau.cs.giraf.gui.GButtonTrash;

public class MapEditor extends CarsActivity implements View.OnClickListener {
    private boolean delete = false;
    private GameSettings gamesettings;
    private int child_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if(intent.hasExtra(DatabaseHelper.CHILD_ID))
            child_id = intent.getIntExtra(DatabaseHelper.CHILD_ID, 0);
        else throw new IllegalArgumentException("no child id");

        Log.d("childid","Childid ved Map create: "+ child_id);

        DatabaseHelper database = new DatabaseHelper(this);
        database.Initialize(child_id);

        gamesettings = database.GetGameSettings();
    }

    @Override
    public Screen getFirstScreen() {
        return new MapScreen(this);
    }

    @Override
    public View getContentView(FastRenderView renderview) {
        FrameLayout frameLayout = new FrameLayout(this);
        LinearLayout linearLayout = new LinearLayout(this);

        GButtonTrash trashButton = new GButtonTrash(this);
        trashButton.setY(5);
        trashButton.setX(5);
        trashButton.setOnClickListener(this);

        linearLayout.addView(trashButton);

        frameLayout.addView(renderview);
        frameLayout.addView(linearLayout);

        return frameLayout;
    }

    public void onClick(View v) {
        delete = true;
    }

    private class MapScreen extends SettingsScreen {
        private final int grassSize = 70;
        private final float garageSize = 250;
        private final int amountOfGarages = 3;
        private final float animationZoneSize = 100;


        private float animationZoneX;

        private ArrayList<Obstacle> obstacles;
        private HashMap<String, Float> map;

        public MapScreen(Game game) {
            super(game);
            setCarX(-getCarWidth());

            obstacles = new ArrayList<Obstacle>();
            map = new HashMap<String, Float>();

            DatabaseHelper databaseHelper = new DatabaseHelper(getApplication());
            databaseHelper.Initialize(child_id);

            obstacles = gamesettings.LoadObstacles();
            map = gamesettings.GetMap();
        }


        @Override
        public void paint(Graphics graphics, float deltaTime) {
            super.paint(graphics, deltaTime);
            for (Obstacle o : obstacles)
                o.Draw(graphics, deltaTime);
        }

        @Override
        public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
            if (delete) {
                Clear();
                delete = false;
            }
            for (Input.TouchEvent e : touchEvents) {
                if (e.type == Input.TouchEvent.TOUCH_DOWN && e.x < animationZoneX && e.y > grassSize && e.y < game.getHeight() - grassSize) {
                    Obstacle rem = null;
                    for (Obstacle o : obstacles)
                        if (o.GetBounds().contains(e.x, e.y)) {
                            rem = o;
                            break;
                        }

                    if (rem == null)
                        Add(e.x - gamesettings.OBSTACLE_SIZE / 2, e.y - gamesettings.OBSTACLE_SIZE / 2);
                    else
                        Remove(rem);
                }
            }
        }

        private void Clear() {
            obstacles.clear();
            map = new HashMap<String, Float>();
            gamesettings.SetMap(map);
        }

        public void AddObstacle(HashMap<String, Float> map, float x, float y, int index) {
            map.put("x" + index, x);
            map.put("y" + index, y);
            map.put("count", (float) index + 1);

        }

        private void Add(float x, float y) {
            int index = obstacles.size();
            obstacles.add(new Obstacle(x, y, gamesettings.OBSTACLE_SIZE, gamesettings.OBSTACLE_SIZE));
            AddObstacle(map, x, y, index);
            Log.d("database",map.toString());
            gamesettings.SetMap(map);
        }

        private void Remove(Obstacle obstacle) {
            int index = obstacles.indexOf(obstacle);
            int size = obstacles.size();

           HashMap<String,Float> map = gamesettings.GetMap();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplication());
        databaseHelper.Initialize(child_id);

        databaseHelper.SaveSettings(gamesettings);

        this.finish();
    }
}
