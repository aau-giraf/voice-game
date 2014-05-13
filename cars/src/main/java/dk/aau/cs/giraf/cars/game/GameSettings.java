package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.*;

public class GameSettings implements Parcelable {
    public static final Parcelable.Creator<GameSettings> CREATOR = new Parcelable.Creator<GameSettings>() {
        public GameSettings createFromParcel(Parcel in) {
            return new GameSettings(in);
        }

        public GameSettings[] newArray(int size) {
            return new GameSettings[size];
        }
    };
    public final int OBSTACLE_SIZE = 100;
    private final float DEFAULT_SPEED = 2.0f;
    private final float DEFAULT_MIN = 0;
    private final float DEFAULT_MAX = 5000;
    private final int DEFAULT_COLOR = Color.BLUE;
    private int color;
    private float speed;
    private float minVolume;
    private float maxVolume;
    private HashMap<String, Float> map;

    public GameSettings() {
        this.color = DEFAULT_COLOR;
        this.speed = DEFAULT_SPEED;
        this.minVolume = DEFAULT_MIN;
        this.maxVolume = DEFAULT_MAX;
        this.map = new HashMap<String, Float>();
    }

    public GameSettings(int color, float speed, float minVolume, float maxVolume, HashMap<String, Float> map) {
        this.color = color;
        this.speed = speed;
        this.minVolume = minVolume;
        this.maxVolume = maxVolume;
        this.map = map;
    }

    private GameSettings(Parcel in) {
        color = in.readInt();
        speed = in.readFloat();
        minVolume = in.readFloat();
        maxVolume = in.readFloat();

        Log.d("database", Integer.toString(in.readInt()));

        map = readHashMap(in);
        Log.d("database","map read  " + map.toString());

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

    public ArrayList<Obstacle> LoadObstacles() {
        ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();

        if (map.get("count") == null) {
            Log.d("database", "returning empty obstacles");
            return obstacles;
        }
        Log.d("database", map.toString());

        int count = map.get("count").intValue();
        Log.d("database","count" + count);
        for (int i = 0; i < count; i++) {
            float x = map.get("x" + i);
            float y = map.get("y" + i);
            obstacles.add(new Obstacle(x, y, OBSTACLE_SIZE, OBSTACLE_SIZE));
        }
        return obstacles;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(color);
        dest.writeFloat(speed);
        dest.writeFloat(minVolume);
        dest.writeFloat(maxVolume);

        dest.writeInt(5);
        writeHashMap(dest,map);
    }

    public void writeHashMap(Parcel dest, HashMap<String,Float> map)
    {
        dest.writeInt(map.size());
        Log.d("database","skriv map size" + map.size());

        for(Map.Entry<String,Float> entry: map.entrySet())
        {
            dest.writeString(entry.getKey());
            dest.writeFloat(entry.getValue());
            Log.d("database","skriv værdi");
        }
    }

    public HashMap<String,Float> readHashMap(Parcel in)
    {
        HashMap<String,Float> map = new HashMap<String, Float>();
        int n = in.readInt();
        Log.d("database","n: "+Integer.toString(n));
        for(int i = 0; i < n; i++)
        {
            String key = in.readString();
            Float val = in.readFloat();
            map.put(key,val);
            Log.d("database","Key: " + key + " Val: " + val);
        }
        return  map;
    }
}
