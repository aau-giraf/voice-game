package dk.aau.cs.giraf.cars.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dk.aau.cs.giraf.cars.framework.GameFragment;
import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.game.CalibrationScreen;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Stefan on 08-04-2014.
 */
public class CalibrationFragment extends GameFragment{

    public CalibrationFragment(){super();}

    @Override
    public Screen getInitScreen() {

        return new CalibrationScreen(this);
    }


}
