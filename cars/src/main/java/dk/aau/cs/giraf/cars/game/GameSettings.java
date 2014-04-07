package dk.aau.cs.giraf.cars.game;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import dk.aau.cs.giraf.cars.game.Controller.Controller;
import dk.aau.cs.giraf.cars.game.Controller.TouchCarControl;
import dk.aau.cs.giraf.cars.game.Controller.TouchController;
import dk.aau.cs.giraf.cars.game.Controller.VolumeController;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;

import java.util.Arrays;
import java.util.LinkedList;

public class GameSettings implements Parcelable {
    private LinkedList<Integer> colors;
    private int speed;
    private Controller carcontrol;

    private final int DEFAULT_SPEED = 300;
    private final Integer[] DEFAULT_COLORS = new Integer[]{Color.BLUE, Color.GREEN, Color.RED};
    private final Controller DEFAULT_CONTROL = new VolumeController();

    public GameSettings() {
        this.colors = new LinkedList<Integer>(Arrays.asList(DEFAULT_COLORS));
        this.speed = DEFAULT_SPEED;
        this.carcontrol = DEFAULT_CONTROL;
    }

    public GameSettings(LinkedList<Integer> colors, int speed, Controller carcontrol) {
        this.colors = colors;
        this.speed = speed;
        this.carcontrol = carcontrol;
    }


    private GameSettings(Parcel in) {
        colors = new LinkedList<Integer>();

        for (int i = 0; i < 3; i++)
            colors.add(in.readInt());

        speed = in.readInt();
        carcontrol = in.readParcelable(Controller.class.getClassLoader());
    }

    public LinkedList<Integer> GetColors() {
        return colors;
    }

    public int GetSpeed() {
        return speed;
    }

    public Controller GetController() {return carcontrol;}
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        for (int i : colors)
            dest.writeInt(i);

        dest.writeInt(speed);

        dest.writeParcelable(carcontrol,flags);
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
