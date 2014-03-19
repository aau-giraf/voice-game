package dk.aau.cs.giraf.cars.game;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Screen;

public class CarGame extends Game {
    @Override
    public Screen getInitScreen() {
        return new LoadingScreen(this);
    }
}
