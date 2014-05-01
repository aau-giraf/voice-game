package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class GameSettings implements Parcelable {
    private LinkedList<Integer> colors;
    private int speed;
    private float minVolume;
    private float maxVolume;
    private HashMap<String,Float> map;

    private final int DEFAULT_SPEED = 30;
    private final float DEFAULT_MIN = 0;
    private final float DEFAULT_MAX = 5000;
    private final Integer[] DEFAULT_COLORS = new Integer[]{Color.BLUE, Color.GREEN, Color.RED};
    public final int OBSTACLE_SIZE = 100;

    public GameSettings() {
        this.colors = new LinkedList<Integer>(Arrays.asList(DEFAULT_COLORS));
        this.speed = DEFAULT_SPEED;
        this.minVolume = DEFAULT_MIN;
        this.maxVolume = DEFAULT_MAX;
    }

    public GameSettings(LinkedList<Integer> colors, int speed, float minVolume, float maxVolume) {
        this.colors = colors;
        this.speed = speed;
        this.minVolume = minVolume;
        this.maxVolume = maxVolume;
    }

    public GameSettings(LinkedList<Integer> colors, int speed, float minVolume, float maxVolume, HashMap<String,Float> map) {
    this(colors,speed,minVolume,maxVolume);
    this.map = map;
    }


    private GameSettings(Parcel in) {
        colors = new LinkedList<Integer>();

        for (int i = 0; i < 3; i++)
            colors.add(in.readInt());

        speed = in.readInt();
        minVolume = in.readFloat();
        maxVolume = in.readFloat();
    }

    public LinkedList<Integer> GetColors() {
        return colors;
    }

    public int GetSpeed() {
        return speed;
    }

    public float GetMinVolume(){return minVolume;}

    public float GetMaxVolume(){return maxVolume;}

    public void SetMap(HashMap<String,Float> map)
    {
        this.map = map;
    }

    public HashMap<String,Float> GetMap()
    {
        return map;
    }

    public ArrayList<Obstacle> LoadObstacles() {
        ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();

        int count = map.get("count").intValue();

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
        for (int i : colors)
            dest.writeInt(i);

        dest.writeInt(speed);
        dest.writeFloat(minVolume);
        dest.writeFloat(maxVolume);
    }

    public static final Parcelable.Creator<GameSettings> CREATOR = new Parcelable.Creator<GameSettings>() {
        public GameSettings createFromParcel(Parcel in) {
            return new GameSettings(in);
        }

        public GameSettings[] newArray(int size) {
            return new GameSettings[size];
        }

    };
}
