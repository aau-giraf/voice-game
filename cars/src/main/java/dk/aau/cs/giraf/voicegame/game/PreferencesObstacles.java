package dk.aau.cs.giraf.voicegame.game;

import android.content.Context;

import dk.aau.cs.giraf.voicegame.IOService;
import dk.aau.cs.giraf.voicegame.Settings.GameSettings;
import dk.aau.cs.giraf.voicegame.Track;
import dk.aau.cs.giraf.voicegame.TrackOrganizer;

import java.util.ArrayList;

public class PreferencesObstacles implements RoadItemGenerator {
    private GameSettings gs;
    private TrackOrganizer trackOrganizer;

    public PreferencesObstacles(GameSettings gs) {
        this.gs = gs;
    }

    /**
     * Creates the roaditems to be displayed from the current track set in the trackOrganizer.
     * @param width
     * @param height
     * @return
     */
    @Override
    public RoadItem[] CreateRoadItems(int width, int height) {
        trackOrganizer = IOService.instance().readTrackOrganizerFromFile();
        Track track;
        if(trackOrganizer.getCurrentTrackID() != -1) {
            track = trackOrganizer.getTrack(trackOrganizer.getCurrentTrackID());
        } else {
            track = new Track(-1, new ArrayList<RoadItem>(), "", gs.GetGameMode());
        }


        ArrayList<RoadItem> roadItemArrayList =  track.getObstacleArray();
        // TODO fix this by adding settings to IOService
        if(roadItemArrayList == null) {
            roadItemArrayList = new ArrayList<RoadItem>();
        }
        RoadItem[] roadItems = new RoadItem[roadItemArrayList.size()];

        return roadItemArrayList.toArray(roadItems);

    }
}
