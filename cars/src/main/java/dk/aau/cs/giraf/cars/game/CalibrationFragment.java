package dk.aau.cs.giraf.cars.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.game.CarsGames.CarsFragment;
import dk.aau.cs.giraf.cars.game.Controller.VolumeCarControl;


public class CalibrationFragment extends CarsFragment {
    VolumeCarControl control;

    public CalibrationFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        control.Stop();
        super.onDestroyView();
    }

    @Override
    public Screen getFirstScreen() {
        control = new VolumeCarControl(0, 500, 5000, this.getHeight());
        return new CalibrationScreen(this, control);
    }
}
