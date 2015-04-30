package dk.aau.cs.giraf.cars.Settings;

import android.graphics.Color;
import android.util.Log;
import dk.aau.cs.giraf.cars.game.GameMode;
import dk.aau.cs.giraf.cars.game.RoadItem;

import java.util.*;

public class GameSettings {
    public final int OBSTACLE_SIZE = 100;
    private final float DEFAULT_SPEED = 2.0f;
    private final float DEFAULT_MIN = 0;
    private final float DEFAULT_MAX = 5000;
    private final int DEFAULT_COLOR = Color.BLUE;
    private final GameMode DEFAULT_GAME_MODE = GameMode.pickup;
    private int color;
    private float speed;
    private float minVolume;
    private float maxVolume;
    private HashMap<String, Float> map;
    private GameMode gameMode;

    public GameSettings() {
        this.color = DEFAULT_COLOR;
        this.speed = DEFAULT_SPEED;
        this.minVolume = DEFAULT_MIN;
        this.maxVolume = DEFAULT_MAX;
        this.map = new HashMap<String, Float>();
        this.gameMode = DEFAULT_GAME_MODE;
    }

    public GameSettings(int color, float speed, float minVolume, float maxVolume, HashMap<String, Float> map, GameMode gameMode) {
        this.color = color;
        this.speed = speed;
        this.minVolume = minVolume;
        this.maxVolume = maxVolume;
        this.map = map;
        this.gameMode = gameMode;
    }

    public int GetColor() {
        return color;
    }

    public float GetSpeed() {
        return speed;
    }

    public float GetMinVolume() {
        return minVolume;
    }

    public float GetMaxVolume() {
        return maxVolume;
    }

    public void SetMap(HashMap<String, Float> map) {
        this.map = map;
    }

    public HashMap<String, Float> GetMap() {
        return map;
    }

    public ArrayList<RoadItem> LoadObstacles() {
        ArrayList<RoadItem> roadItems = new ArrayList<RoadItem>();

        if (map.get("count") == null) {
            Log.d("database", "returning empty obstacles");
            return roadItems;
        }
        Log.d("database", map.toString());

        int count = map.get("count").intValue();
        Log.d("database", "count" + count);
        for (int i = 0; i < count; i++) {
            float x = map.get("x" + i);
            float y = map.get("y" + i);
            roadItems.add(new RoadItem(x, y, OBSTACLE_SIZE, OBSTACLE_SIZE, gameMode));
        }
        return roadItems;
    }

    public GameMode GetGameMode() {
        return gameMode;
    }
}
