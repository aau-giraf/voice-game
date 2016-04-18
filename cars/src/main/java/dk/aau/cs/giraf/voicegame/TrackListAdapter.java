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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import dk.aau.cs.giraf.voicegame.Interfaces.Drawable;

/**
 * Custom adapter to make images in the GList, presented in the TrackPickerActivity
 */
public class TrackListAdapter extends ArrayAdapter<Integer> {
    // the commented section is stuff that will be implemented in other tasks, which we are working on.
    private ArrayList<Bitmap> trackScreenshots;
    private ArrayList<Integer> trackIDs;
    private TrackPickerActivity parentActivity;

    // The integer and bitmap array are the same size
    public TrackListAdapter(Context context, ArrayList<Integer> intResource, ArrayList<Bitmap> bitmapResource, TrackPickerActivity parentActivity) {
        super(context, R.layout.track_picker_row, intResource);

        trackIDs = intResource;
        trackScreenshots = bitmapResource;
        this.parentActivity = parentActivity;
    }

    /**
     * Override method from ArrayAdapter which displays the view for each row in the list in which the adapter is assigned
     * @param position is the position in the list
     * @param convertView the view that makes up a row
     * @param parent the parent view
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.track_picker_row, parent, false);
        }
            View rowView = convertView;

        ImageView imageTrack1 = (ImageView) rowView.findViewById(R.id.image_track1);
        ImageView imageTrack2 = (ImageView) rowView.findViewById(R.id.image_track2);

        if(position < trackIDs.size()) {
            imageTrack1.setImageBitmap(trackScreenshots.get(position));
        }

        if(position + 1 < trackIDs.size()) {
            imageTrack2.setImageBitmap(trackScreenshots.get(position + 1));
        }

        imageTrack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position < trackIDs.size()) {
                    parentActivity.setTrack(trackIDs.get(position));
                    Toast.makeText(parentActivity, String.valueOf(trackIDs.get(position)), Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageTrack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position + 1 < trackIDs.size()) {
                    parentActivity.setTrack(trackIDs.get(position + 1));
                    Toast.makeText(parentActivity, String.valueOf(trackIDs.get(position + 1)), Toast.LENGTH_SHORT).show();
                }
            }
        });


        return rowView;
    }

    public int test() { return  1;}
}
