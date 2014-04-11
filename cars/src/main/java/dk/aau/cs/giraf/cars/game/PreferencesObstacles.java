package dk.aau.cs.giraf.cars.game;

import android.content.ContextWrapper;
import android.content.SharedPreferences;

public class PreferencesObstacles implements ObstacleGenerator {
    private ContextWrapper contextWrapper;

    public PreferencesObstacles(ContextWrapper contextWrapper) {
        this.contextWrapper = contextWrapper;
    }

    @Override
    public Obstacle[] CreateObstacles(int width, int height) {
        SharedPreferences mapPreferences = contextWrapper.getSharedPreferences("map", 0);
        int count = mapPreferences.getInt("count", 0);
        Obstacle[] obstacles = new Obstacle[count];

        for (int i = 0; i < count; i++) {
            float x = mapPreferences.getFloat("x" + i, 0);
            float y = mapPreferences.getFloat("y" + i, 0);
            obstacles[i] = new Obstacle(x, y, 100, 100);
        }

        return obstacles;
    }
}
