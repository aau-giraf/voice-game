package dk.aau.cs.giraf.voicegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * A singleton class that handles read/write actions to/from internal storage
 */
public class IOService {
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
    public TrackOrganizer readTrackOrganizerFromFile(Context ctx){
        TrackOrganizer trackOrganizer = null;

        try{
            FileInputStream inputStream = ctx.openFileInput(ctx.getResources().getString(R.string.track_file_name));
            BufferedInputStream buffer = new BufferedInputStream(inputStream);
            ObjectInputStream ois = new ObjectInputStream(buffer);
            trackOrganizer = (TrackOrganizer) ois.readObject();
            ois.close();
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
    public void writeTrackOrganizerToFile(TrackOrganizer trackOrganizer, Context ctx){
        try{
            FileOutputStream outputStream = ctx.openFileOutput(ctx.getResources().getString(R.string.track_file_name), Context.MODE_PRIVATE);
            BufferedOutputStream buffer = new BufferedOutputStream(outputStream);
            ObjectOutputStream oos = new ObjectOutputStream(buffer);
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
    public void deleteTrackOrganizer(Context ctx){
        File file = new File(ctx.getFilesDir() + "/" + ctx.getResources().getString(R.string.track_file_name));
        if (file != null) {
            file.delete();
        }
    }

    /**
     * Writes a bitmap to a file.
     * @param bitmap The bitmap to be saved.
     * @param identifier The name the bitmap will be saved as. Must be unique.
     * @param ctx The context of the application.
     * @return The path the saved bitmap
     */
    public String writeNewBitmapToFile(Bitmap bitmap, String identifier, Context ctx) {
        File directory = ctx.getDir(ctx.getResources().getString(R.string.screenshots_dir), Context.MODE_PRIVATE);
        File bitmapPath = new File(directory, identifier + ".jpg");

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(bitmapPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            outputStream.close();
        } catch (FileNotFoundException e){
            System.out.println("Bitmap file not found - output");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("IO exception happened while writing bitmap");
            e.printStackTrace();
        }

        return directory.getAbsolutePath();
    }

    /**
     * Overwrites a saved bitmap with a new one.
     * @param bitmap The new bitmap
     * @param path The path to the old bitmap.
     * @param identifier The identifier of the old bitmap.
     */
    public void overwriteBitmapToFile(Bitmap bitmap, String path, String identifier) {
        try {
            File bitmapFile = new File(path, identifier + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
            outputStream.close();
        } catch (FileNotFoundException e){
            System.out.println("Bitmap file not found - output");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO exception happened while writing bitmap");
            e.printStackTrace();
        }
    }

    /**
     * Reads a bitmap from a file.
     * @param path The path to the bitmap. This is supplied when calling writeNewBitmapToFile().
     * @param identifier The identifier the bitmap was saved as.
     * @return The read bitmap.
     */
    public Bitmap readBitmapFromFile(String path, String identifier) {

        Bitmap bitmap = null;
        try {
            File bitmapFile = new File(path, identifier + ".jpg");
            FileInputStream inputStream = new FileInputStream(bitmapFile);
            BitmapFactory.Options options = new BitmapFactory.Options();
            // load in an image that is 1/4 of the original size, to save memory.
            options.inSampleSize = 4;
            bitmap = BitmapFactory.decodeStream(inputStream, new Rect(0, 0, 0, 0), options);
            inputStream.close();
        } catch (FileNotFoundException e){
            System.out.println("Bitmap file not found - output");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("IO exception happened while writing bitmap");
            e.printStackTrace();
        }

        return bitmap;
    }

    public void deleteBitmap(String path, String identifier) {
        File bitmapFile = new File(path, identifier + ".jpg");
        if(bitmapFile != null) {
            bitmapFile.delete();
        }
    }

}
