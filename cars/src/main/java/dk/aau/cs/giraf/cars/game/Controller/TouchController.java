package dk.aau.cs.giraf.cars.game.Controller;

import android.os.Parcel;
import android.os.Parcelable;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;

public class TouchController extends Controller{
    TouchCarControl touchcontrol;

    public TouchController()
    {
        touchcontrol = new TouchCarControl();
    }

    public TouchController(Parcel in)
    {
        touchcontrol = new TouchCarControl();
    }

    @Override
    public CarControl GetCarControl() {
        return touchcontrol;
    }

    @Override
    public CarCalibration GetCalibrator() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<TouchController> CREATOR = new Parcelable.Creator<TouchController>() {
        public TouchController createFromParcel(Parcel in) {
            return new TouchController(in);
        }

        public TouchController[] newArray(int size) {
            return new TouchController[size];
        }

    };
}
