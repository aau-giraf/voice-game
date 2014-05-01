package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.LinkedList;

public class GameSettings implements Parcelable {
    private LinkedList<Integer> colors;
    private float speed;
    private float minVolume;
    private float maxVolume;

    private final float DEFAULT_SPEED = 2.0f;
    private final float DEFAULT_MIN = 0;
    private final float DEFAULT_MAX = 5000;
    private final Integer[] DEFAULT_COLORS = new Integer[]{Color.BLUE, Color.GREEN, Color.RED};

    public GameSettings() {
        this.colors = new LinkedList<Integer>(Arrays.asList(DEFAULT_COLORS));
        this.speed = DEFAULT_SPEED;
        this.minVolume = DEFAULT_MIN;
        this.maxVolume = DEFAULT_MAX;
    }

    public GameSettings(LinkedList<Integer> colors, float speed, float minVolume, float maxVolume) {
        this.colors = colors;
        this.speed = speed;
        this.minVolume = minVolume;
        this.maxVolume = maxVolume;
    }


    private GameSettings(Parcel in) {
        colors = new LinkedList<Integer>();

        for (int i = 0; i < 3; i++)
            colors.add(in.readInt());

        speed = in.readFloat();
        minVolume = in.readFloat();
        maxVolume = in.readFloat();
    }

    public LinkedList<Integer> GetColors() {
        return colors;
    }

    public float GetSpeed() {
        return speed;
    }

    public float GetMinVolume(){return minVolume;}

    public float GetMaxVolume(){return maxVolume;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        for (int i : colors)
            dest.writeInt(i);

        dest.writeFloat(speed);
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
