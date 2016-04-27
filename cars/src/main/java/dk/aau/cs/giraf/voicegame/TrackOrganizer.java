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

    private int maxTracks = 14;

    private ArrayList<Track> trackArrayList;
    private ArrayList<Integer> deletedIDs;
    private int currentTrackID;

    public  TrackOrganizer(){
        trackArrayList = new ArrayList<Track>();
        deletedIDs = new ArrayList<Integer>();
    }

    /**
     * Checks the deletedIDs array for any ID's to use from there, if there is none, then a new one is created.
     * @return the next free id;
     */
    public int getNextFreeID() {

        if(deletedIDs.isEmpty()) {
            // if there are no deleted ID's to pick from, then use the size of the trackArrayList, as this value is the number of elements in the list  + 1
            return trackArrayList.size();
        } else {
            // get the last ID and delete it from the list, as that ID is now in use again.
            int result = deletedIDs.get(deletedIDs.size() - 1);
            deletedIDs.remove(deletedIDs.size() - 1);
            return result;
        }
    }

    /**
     *  Adds track to the trackarraylist which is handled by the trackorganizer.
     * @param roadItemArrayList the array containing the road items that belongs to the given track
     */
    public void addTrack(Bitmap screenshot, ArrayList<RoadItem> roadItemArrayList, GameMode type){
        int id = getNextFreeID();
        String bitmapPath = IOService.instance().writeNewBitmapToFile(screenshot, String.valueOf(id));
        trackArrayList.add(new Track(id, roadItemArrayList, bitmapPath, type));
    }

    /**
     * "Deletes" a track by replacing it with null to ensure that the place in the trackarraylist are identical to the id.
     * @param trackID the track id
     */
    public void deleteTrack(int trackID){
        IOService.instance().deleteBitmap(getTrack(trackID).getScreenshotPath(), String.valueOf(trackID));
        trackArrayList.remove(getTrack(trackID));
        deletedIDs.add(trackID);
    }

    /**
     * Returns the trackArrayList
     * @return trackArrayList
     */
    public ArrayList<Track> getTrackArray(){
        return trackArrayList;
    }

    /**
     * Get the array of screenshots that is stored for all the tracks.
     * @return
     */
    public ArrayList<Bitmap> getScreenshotArray() {
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

        for (Track track: trackArrayList) {
            bitmaps.add(IOService.instance().readBitmapFromFile(track.getScreenshotPath(), String.valueOf(track.getID())));
        }

        return bitmaps;
    }

    /**
     * Get a specific track using id
     * @param trackID the id of the wanted track
     * @return the track corresponding to the id
     */
    public Track getTrack(int trackID) {

        Track result = null;

        for (Track track: trackArrayList) {
            if(track.getID() == trackID) {
                result = track;
            }
        }

        return result;
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
        getTrack(trackID).setObstacleArray(roadItems);
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
