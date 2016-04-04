package dk.aau.cs.giraf.voicegame;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

import dk.aau.cs.giraf.voicegame.game.RoadItem;

/**
 * Holds the information about a track. I.e. stars, obstacles, name, id etc for the purpose of saving tracks.
 */
public class Track implements Serializable {
    private int id;
    private ArrayList<RoadItem> obstacleArray;
    private String screenshotPath;
    private transient Bitmap screenshot;

    public Track(int id, ArrayList<RoadItem> obstacleArray){
        this.id = id;
        this.obstacleArray = obstacleArray;
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

}
