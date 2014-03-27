package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;

public class GameSettings implements Parcelable{
    private LinkedList<Integer> colors;
    private int speed;

    public GameSettings(LinkedList<Integer> colors, int speed)
    {
        this.colors = colors;
        this.speed = speed;
    }


    private GameSettings(Parcel in)
    {
        colors = new LinkedList<Integer>();

        for(int i = 0; i< 3; i++)
        colors.add(in.readInt());

        speed = in.readInt();
    }

    public LinkedList<Integer> GetColors()
    {
           return colors;
    }

    public int GetSpeed()
    {
            return speed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        for(int i:colors)
            dest.writeInt(i);

        dest.writeInt(speed);
    }

    public static final Parcelable.Creator<GameSettings> CREATOR = new Parcelable.Creator<GameSettings>(){
        public GameSettings createFromParcel(Parcel in)
        {
            return new GameSettings(in);
        }

        public GameSettings[] newArray(int size)
        {
            return new GameSettings[size];
        }

    };
}
