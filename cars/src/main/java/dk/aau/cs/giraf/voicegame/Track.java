package dk.aau.cs.giraf.voicegame;

import java.io.Serializable;
import java.util.ArrayList;

import dk.aau.cs.giraf.voicegame.game.RoadItem;

/**
 * Holds the information about a track. I.e. stars, obstacles, name, id etc for the purpose of saving tracks.
 */
public class Track implements Serializable {
    private int id;
    private ArrayList<RoadItem> obstacleArray;

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
}
