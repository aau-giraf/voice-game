package dk.aau.cs.giraf.voicegame;

import java.io.Serializable;
import java.util.ArrayList;

import dk.aau.cs.giraf.voicegame.game.GameMode;
import dk.aau.cs.giraf.voicegame.game.RoadItem;

/**
 * Holds the information about a track. I.e. stars, obstacles, name, id etc for the purpose of saving tracks.
 */
public class Track implements Serializable {
    private int id;
    private ArrayList<RoadItem> obstacleArray;
    private String screenshotPath;
    private GameMode mode;

    public Track(int id, ArrayList<RoadItem> obstacleArray, String screenshotPath, GameMode type){
        this.id = id;
        this.obstacleArray = obstacleArray;
        this.mode = type;
        this.screenshotPath = screenshotPath;
    }

    public int getID(){
        return id;
    }


    public ArrayList<RoadItem> getObstacleArray() {
        return obstacleArray;
    }

    /**
     * Needed method for other components that reads road items from game settings.
     * @param obstacleArray
     */
    public void setObstacleArray(ArrayList<RoadItem> obstacleArray) {
        this.obstacleArray = obstacleArray;
    }

    /**
     * This method prepares the roaditems for rendering. Always call this method when loading a track from file or as an extra, else the text might look weird
     */
    public void initRoadItems() {
        if (!obstacleArray.isEmpty()) {
            for (RoadItem item: obstacleArray) {
                item.initPaint();
            }
        }
    }

    public GameMode getMode() {
        return mode;
    }
    public String getScreenshotPath() {
        return screenshotPath;
    }
}
