package dk.aau.cs.giraf.cars.game.Controller;

import android.os.Parcel;
import android.os.Parcelable;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;

/**
 * Created by Stefan on 03-04-2014.
 */
public abstract class Controller implements Parcelable{

    public abstract CarControl GetCarControl();

    public abstract CarCalibration GetCalibrator();
}
