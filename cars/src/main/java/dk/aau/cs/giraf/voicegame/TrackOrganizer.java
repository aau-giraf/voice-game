package dk.aau.cs.giraf.voicegame;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

import dk.aau.cs.giraf.voicegame.game.GameMode;
import dk.aau.cs.giraf.voicegame.game.RoadItem;

/**
 * Keeps track of all the tracks; their name and position in the array and places it within the array.
 */
public class TrackOrganizer implements Serializable{

    //ID that is connected to the file so that it is known what file it is when it is read.
    static final long serialVersionUID = 1L;

    // TODO temporary max value, some error happens when adding more than 6 tracks. Will make task aswell.
    private int maxTracks = 5;

    private ArrayList<Track> trackArrayList;
    private int currentTrackID;

    public  TrackOrganizer(){
        trackArrayList = new ArrayList<Track>();
    }

    public int getNextFreeID() {
        for (int i = 0; i < trackArrayList.size(); i++) {
            if(trackArrayList.get(i) == null) {
                return i;
            }
        }

        return trackArrayList.size();
    }

    /**
     *  Adds track to the trackarraylist which is handled by the trackorganizer
     * @param roadItemArrayList the array containing the road items that belongs to the given track
     */
    public void addTrack(ArrayList<RoadItem> roadItemArrayList, String bitmapPath, GameMode type){
        Boolean isAdded = false;


        for (int i = 0; i<trackArrayList.size(); i++){
            if(trackArrayList.get(i) == null && !isAdded){
                trackArrayList.add(i, new Track(i,roadItemArrayList, bitmapPath, type));
                isAdded = true;
            }
        }
        if(!isAdded){
            trackArrayList.add(new Track(trackArrayList.size(),roadItemArrayList, bitmapPath, type));
        }
    }

    /**
     * "Deletes" a track by replacing it with null to ensure that the place in the trackarraylist are identical to the id.
     * @param trackID the track id
     */
    public void deleteTrack(int trackID){
        IOService.instance().deleteBitmap(getTrack(trackID).getScreenshotPath(), String.valueOf(trackID));
        trackArrayList.set(trackID, null);
    }

    /**
     * Returns the trackArrayList
     * @return trackArrayList
     */
    public ArrayList<Track> getTrackArray(){
        return trackArrayList;
    }

    public ArrayList<Bitmap> getScreenshotArray(Context ctx) {
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        for(int i = 0; i < trackArrayList.size(); i++) {
            if(trackArrayList.get(i) != null) {
                Track track = trackArrayList.get(i);
                bitmaps.add(IOService.instance().readBitmapFromFile(track.getScreenshotPath(), String.valueOf(track.getID())));
            }
        }

        return bitmaps;
    }

    /**
     * Get a specific track using id
     * @param trackID the id of the wanted track
     * @return the track corresponding to the id
     */
    public Track getTrack(int trackID) {
        return trackArrayList.get(trackID);
    }
    
    public Bitmap getTrackScreenshot(int trackID, Context ctx) {
        return IOService.instance().readBitmapFromFile(getTrack(trackID).getScreenshotPath(), String.valueOf(trackID));
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

    // TODO temporary check for the amount of tracks saved, some error happens when adding more than 6 tracks. Will make task aswell.
    public boolean canSaveMoreTracks() {
        int tracksInArray = 0;

        for (Track track: trackArrayList) {
            if(track != null) {
                tracksInArray++;
            }
        }

        if(tracksInArray < maxTracks) {
            return true;
        } else {
            return false;
        }
    }

}
