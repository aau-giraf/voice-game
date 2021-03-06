package dk.aau.cs.giraf.voicegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.util.Log;
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
    private ImageView lastClickImage = null;
    private TrackOrganizer trackOrganizer;
    private boolean isEmpty = false;

    // The integer and bitmap array are the same size
    public TrackListAdapter(Context context, ArrayList<Integer> numberOfRows, ArrayList<Integer> trackIDs, ArrayList<Bitmap> bitmapResource,
                            TrackOrganizer trackOrganizer, TrackPickerActivity parentActivity) {
        super(context, R.layout.track_picker_row, numberOfRows);

        this.trackIDs = trackIDs;
        trackScreenshots = bitmapResource;
        this.parentActivity = parentActivity;
        this.trackOrganizer = trackOrganizer;

        // -1 is the standard value for creating an empty track.
        if(trackIDs.isEmpty()) {
            isEmpty = trackIDs.isEmpty();
            trackIDs.add(-1);
        }
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

        if(isEmpty) {
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.track_picker_row_empty, parent, false);
            }

            return convertView;
        }

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.track_picker_row, parent, false);
        }
            View rowView = convertView;

        final ImageView imageTrack1 = (ImageView) rowView.findViewById(R.id.image_track1);
        final ImageView imageTrack2 = (ImageView) rowView.findViewById(R.id.image_track2);

        final int calculatedPosition = (position + 1) * 2;

        if((calculatedPosition - 2) < trackIDs.size()) {
            Log.v("TackListAdapter", (calculatedPosition - 2) + " < " + trackIDs.size());
            imageTrack1.setImageBitmap(trackScreenshots.get(calculatedPosition - 2));
        } else {
            imageTrack2.setImageDrawable(null);
        }

        if((calculatedPosition - 1) < trackIDs.size()) {
            Log.v("TackListAdapter", (calculatedPosition - 1) + " < " + trackIDs.size());
            imageTrack2.setImageBitmap(trackScreenshots.get(calculatedPosition - 1));
        } else {
            imageTrack2.setImageDrawable(null);
        }

        /**
         * This method will be called when pressing images of the left side.
         * It changed the background color to mark it and tell the trackOrganizer to change it's current track.
         */
        imageTrack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((calculatedPosition - 2) < trackIDs.size()) {
                    parentActivity.setTrack(trackIDs.get(calculatedPosition - 2));
                    trackOrganizer.setCurrentTrackID(trackIDs.get(calculatedPosition - 2));


                    if(lastClickImage != null) {
                        lastClickImage.setBackgroundColor(getContext().getResources().getColor(R.color.listBackground));
                    }

                    imageTrack1.setBackgroundColor(getContext().getResources().getColor(R.color.listRowFocusedAlt));
                    lastClickImage = imageTrack1;
                }
            }
        });

        /**
         * This method will be called when pressing images of the right side.
         * It changed the background color to mark it and tell the trackOrganizer to change it's current track.
         */
        imageTrack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((calculatedPosition - 1) < trackIDs.size()) {
                    parentActivity.setTrack(trackIDs.get(calculatedPosition - 1));
                    trackOrganizer.setCurrentTrackID(trackIDs.get(calculatedPosition - 1));


                    if (lastClickImage != null) {
                        lastClickImage.setBackgroundColor(getContext().getResources().getColor(R.color.listBackground));
                    }

                    imageTrack2.setBackgroundColor(getContext().getResources().getColor(R.color.listRowFocusedAlt));
                    lastClickImage = imageTrack2;
                }
            }
        });


        return rowView;
    }

    public ImageView getLastClickImage() {
        return lastClickImage;
    }
}
