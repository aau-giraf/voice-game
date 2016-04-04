package dk.aau.cs.giraf.voicegame.game;

import dk.aau.cs.giraf.voicegame.Settings.GameSettings;

import java.util.ArrayList;

public class PreferencesObstacles implements RoadItemGenerator {
    private GameSettings gs;

    public PreferencesObstacles(GameSettings gs) {
        this.gs = gs;
    }

    @Override
    public RoadItem[] CreateRoadItems(int width, int height) {

        ArrayList<RoadItem> roadItemArrayList =  gs.LoadObstacles();
        // TODO fix this by adding settings to IOService
        if(roadItemArrayList == null) {
            roadItemArrayList = new ArrayList<RoadItem>();
        }
        RoadItem[] roadItems = new RoadItem[roadItemArrayList.size()];

        return roadItemArrayList.toArray(roadItems);

    }
}
