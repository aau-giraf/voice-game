package dk.aau.cs.giraf.cars.game;

import android.content.ContextWrapper;
import android.content.SharedPreferences;
import dk.aau.cs.giraf.cars.DatabaseHelper;

import java.util.ArrayList;

public class PreferencesObstacles implements ObstacleGenerator {
    private GameSettings gs;

    public PreferencesObstacles(GameSettings gs) {
        this.gs = gs;
    }

    @Override
    public Obstacle[] CreateObstacles(int width, int height) {

        ArrayList<Obstacle> obstacleList =  gs.LoadObstacles();
        Obstacle[] obstacles = new Obstacle[obstacleList.size()];

        return obstacleList.toArray(obstacles);

    }
}
