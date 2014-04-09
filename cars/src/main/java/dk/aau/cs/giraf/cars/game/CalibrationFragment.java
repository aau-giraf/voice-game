package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.Screen;


public class CalibrationFragment extends CarsFragment{

    public CalibrationFragment(){super();}

    @Override
    public Screen getFirstScreen() {
        return new CalibrationScreen(this);
    }
}
