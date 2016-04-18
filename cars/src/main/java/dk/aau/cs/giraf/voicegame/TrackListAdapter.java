package dk.aau.cs.giraf.voicegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import dk.aau.cs.giraf.voicegame.Interfaces.Drawable;

/**
 * Custom adapter to make images in the GList, presented in the TrackPickerActivity
 */
public class TrackListAdapter extends ArrayAdapter<Integer> {
    // the commented section is stuff that will be implemented in other tasks, which we are working on.

    private ArrayList<Bitmap> bitmaps;
    private int currentBitmap = 0;

    public TrackListAdapter(Context context, ArrayList<Integer> intResource, ArrayList<Bitmap> bitmapResource) {
        super(context, R.layout.track_picker_row, intResource);
        bitmaps = bitmapResource;
    }

    /**
     * Override method from ArrayAdapter which displays the view for each row in the list in which the adapter is assigned
     * @param position is the position in the list
     * @param convertView the view that makes up a row
     * @param parent the parent view
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.track_picker_row, parent, false);
        }
            View rowView = convertView;

        ImageView imageTrack1 = (ImageView) rowView.findViewById(R.id.image_track1);

        if( currentBitmap < bitmaps.size()) {
            imageTrack1.setImageBitmap(bitmaps.get(currentBitmap));
            currentBitmap++;
        }

        ImageView imageTrack2 = (ImageView) rowView.findViewById(R.id.image_track2);

        if( currentBitmap < bitmaps.size()) {
            imageTrack2.setImageBitmap(bitmaps.get(currentBitmap));
            currentBitmap++;
        }



        return rowView;
    }
}
