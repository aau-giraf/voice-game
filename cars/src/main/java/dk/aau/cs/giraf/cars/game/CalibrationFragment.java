package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.Screen;
import dk.aau.cs.giraf.cars.game.CarsGames.CarsFragment;


public class CalibrationFragment extends CarsFragment {

    public CalibrationFragment(){super();}

    @Override
    public Screen getFirstScreen() {
        return new CalibrationScreen(this);
    }
}
