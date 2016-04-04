package dk.aau.cs.giraf.voicegame;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

import dk.aau.cs.giraf.voicegame.game.RoadItem;

/**
 * Keeps track of all the tracks; their name and position in the array and places it within the array.
 */
public class TrackOrganizer implements Serializable{

    static final long serialVersionUID = 1L;

    private ArrayList<Track> trackArrayList;

    public  TrackOrganizer(){
        trackArrayList = new ArrayList<Track>();
    }


    public void addTrack(ArrayList<RoadItem> roadItemArrayList){
        Boolean isAdded = false;


        for (int i = 0; i<trackArrayList.size(); i++){
            if(trackArrayList.get(i) == null && !isAdded){
                trackArrayList.add(i, new Track(i,roadItemArrayList));
                isAdded = true;
            }
        }
        if(!isAdded){
            trackArrayList.add(new Track(trackArrayList.size(),roadItemArrayList));
        }
    }

    public void deleteTrack(int trackID){
        trackArrayList.set(trackID, null);
        for (Track track: trackArrayList) {
            if(track != null){
                System.out.print(track.getID());
            }else{
                System.out.print("null");
            }
        }
    }

    public ArrayList<Track> getArray(){
        return trackArrayList;
    }

    public Track getTrack(int trackID) {
        return trackArrayList.get(trackID);
    }

    public void editTrack(int trackID, ArrayList<RoadItem> roadItems) {
        trackArrayList.get(trackID).setObstacleArray(roadItems);
    }
}
