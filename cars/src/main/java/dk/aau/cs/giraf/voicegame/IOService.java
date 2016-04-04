package dk.aau.cs.giraf.voicegame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * A singleton class that handles read/write actions to/from internal storage
 */
public class IOService {
    public static final String TRACK_FILE_PATH = "/sdcard/TracksFile";
    private static IOService ioService;


    static public IOService instance(){
        if(ioService == null){
            ioService = new IOService();
        }
        return ioService;
    }

    /**
     * Method will try to read the trackOrganizer, it does not already exist (is null), then it will instantiate a new TrackOrganizer instead.
     * @return either the track that was read or a new track organizer.
     */
    public TrackOrganizer readTrackOrganizerFromFile(){
        TrackOrganizer trackOrganizer = null;
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TRACK_FILE_PATH));
            trackOrganizer = (TrackOrganizer) ois.readObject();
        }catch (FileNotFoundException e){
            System.out.println("File not found - input");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("IO exception happened while reading");
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            System.out.println("Wrong cast");
            e.printStackTrace();
        }

        if(trackOrganizer == null){
            trackOrganizer = new TrackOrganizer();
        }

        return trackOrganizer;
    }


    /**
     * Method will write the content of the input to the file
     * @param trackOrganizer the track that should be written to the file
     */
    public void writeTrackOrganizerToFile(TrackOrganizer trackOrganizer){
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TRACK_FILE_PATH));
            oos.writeObject(trackOrganizer);
            oos.close();
        } catch (FileNotFoundException e){
            System.out.println("File not found - output");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("IO exception happened while writing");
            e.printStackTrace();
        }
    }

    /**
     * Deletes the trackOrganizer
     */
    public void deleteTrackOrganizer(){
        File file = new File(TRACK_FILE_PATH);
        if (file != null) {
            file.delete();
        }

    }

}
