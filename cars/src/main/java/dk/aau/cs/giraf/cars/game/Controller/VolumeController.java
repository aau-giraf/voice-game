package dk.aau.cs.giraf.cars.game.Controller;

import android.os.Parcel;
import android.os.Parcelable;
import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;

public class VolumeController extends Controller{
    VolumeCarControl volumeControl;

    public VolumeController()
    {
        volumeControl= new VolumeCarControl(500,2000,5000);
    }

    public VolumeController(Parcel in)
    {
        volumeControl = new VolumeCarControl(500,2000,5000);
    }
    
    @Override
    public CarControl GetCarControl() {
        return null;
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
        //TODO insert parceling of settings
    }

    public static final Parcelable.Creator<VolumeController> CREATOR = new Parcelable.Creator<VolumeController>() {
        public VolumeController createFromParcel(Parcel in) {
            return new VolumeController(in);
        }

        public VolumeController[] newArray(int size) {
            return new VolumeController[size];
        }

    };
}
