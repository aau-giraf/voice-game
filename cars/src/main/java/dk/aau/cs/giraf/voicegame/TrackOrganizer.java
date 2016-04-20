package dk.aau.cs.giraf.voicegame;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

import dk.aau.cs.giraf.voicegame.game.GameMode;
import dk.aau.cs.giraf.voicegame.game.RoadItem;

/**
 * Keeps track of all the tracks; their name and position in the array and places it within the array.
 */
public class TrackOrganizer implements Serializable{

    //ID that is connected to the file so that it is known what file it is when it is read.
    static final long serialVersionUID = 1L;

    private ArrayList<Track> trackArrayList;
    private int currentTrackID;

    public  TrackOrganizer(){
        trackArrayList = new ArrayList<Track>();
    }

    /**
     *  Adds track to the trackarraylist which is handled by the trackorganizer
     * @param roadItemArrayList the array containing the road items that belongs to the given track
     */
    public void addTrack(ArrayList<RoadItem> roadItemArrayList, GameMode type){
        Boolean isAdded = false;


        for (int i = 0; i<trackArrayList.size(); i++){
            if(trackArrayList.get(i) == null && !isAdded){
                trackArrayList.add(i, new Track(i,roadItemArrayList, type));
                isAdded = true;
            }
        }
        if(!isAdded){
            trackArrayList.add(new Track(trackArrayList.size(),roadItemArrayList, type));
        }
    }

    /**
     * "Deletes" a track by replacing it with null to ensure that the place in the trackarraylist are identical to the id.
     * @param trackID the track id
     */
    public void deleteTrack(int trackID){
        trackArrayList.set(trackID, null);
    }

    /**
     * Returns the trackArrayList
     * @return trackArrayList
     */
    public ArrayList<Track> getArray(){
        return trackArrayList;
    }

    /**
     * Get a specific track using id
     * @param trackID the id of the wanted track
     * @return the track corresponding to the id
     */
    public Track getTrack(int trackID) {
        return trackArrayList.get(trackID);
    }

    /**
     * Overrides the track belonging to the track with the trackID
     * @param trackID id of the track
     * @param roadItems array of roaditems in the track
     */
    public void editTrack(int trackID, ArrayList<RoadItem> roadItems) {
        trackArrayList.get(trackID).setObstacleArray(roadItems);
    }

    public void setCurrentTrackID(int currentTrackID) {
        this.currentTrackID = currentTrackID;
    }

    public int getCurrentTrackID() {
        return currentTrackID;
    }
}
