package dk.aau.cs.giraf.cars.Game;

import dk.aau.cs.giraf.cars.Settings.GameSettings;

import java.util.ArrayList;

public class PreferencesObstacles implements RoadItemGenerator {
    private GameSettings gs;

    public PreferencesObstacles(GameSettings gs) {
        this.gs = gs;
    }

    @Override
    public RoadItem[] CreateRoadItems(int width, int height) {

        ArrayList<RoadItem> roadItemArrayList =  gs.LoadObstacles();
        RoadItem[] roadItems = new RoadItem[roadItemArrayList.size()];

        return roadItemArrayList.toArray(roadItems);

    }
}
