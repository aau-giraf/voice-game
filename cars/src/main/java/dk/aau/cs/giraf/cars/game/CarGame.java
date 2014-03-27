package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.GameActivity;
import dk.aau.cs.giraf.cars.framework.Screen;

public class CarGame extends GameActivity {
    @Override
    public Screen getInitScreen() {
        return new LoadingScreen(this);
    }
}
