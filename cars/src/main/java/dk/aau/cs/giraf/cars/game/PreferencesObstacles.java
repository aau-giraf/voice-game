package dk.aau.cs.giraf.cars.game;

import android.content.ContextWrapper;
import android.content.SharedPreferences;
import dk.aau.cs.giraf.cars.DatabaseHelper;

public class PreferencesObstacles implements ObstacleGenerator {
    private GameSettings gs;

    public PreferencesObstacles(GameSettings gs) {
        this.gs = gs;
    }

    @Override
    public Obstacle[] CreateObstacles(int width, int height) {

        return (Obstacle[])gs.LoadObstacles().toArray();

    }
}
