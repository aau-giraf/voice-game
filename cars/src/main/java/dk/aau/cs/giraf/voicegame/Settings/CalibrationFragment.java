package dk.aau.cs.giraf.voicegame.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.aau.cs.giraf.game_framework.Screen;
import dk.aau.cs.giraf.voicegame.CarsGames.CarsFragment;
import dk.aau.cs.giraf.voicegame.game.Controller.VolumeCarControl;
import dk.aau.cs.giraf.voicegame.R;


public class CalibrationFragment extends CarsFragment {
    VolumeCarControl control;
    private CalibrationScreen screen = null;
    float minVolume;
    float maxVolume;


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

    public void SetMinVolume(float minVolume) {
        this.minVolume = minVolume;
        if (screen != null)
            screen.control.setMinAmplitude(minVolume);
    }

    public float GetMinVolume() {
        return screen.control.GetMinAmplitude();
    }

    public void SetMaxVolume(float maxVolume) {
        this.maxVolume = maxVolume;
        if (screen != null)
            screen.control.setMaxAmplitude(maxVolume);
    }

    public float GetMaxVolume() {
        return screen.control.GetMaxAmplitude();
    }

    @Override
    public Screen getFirstScreen() {
        control = new VolumeCarControl(minVolume, maxVolume);
        screen = new CalibrationScreen( getResources().getString(R.string.calibration_button_high), getResources().getString(R.string.calibration_button_low), this, control);
        return screen;
    }
}
