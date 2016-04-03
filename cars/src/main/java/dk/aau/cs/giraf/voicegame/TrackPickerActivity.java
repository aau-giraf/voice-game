package dk.aau.cs.giraf.voicegame;

import android.app.ActionBar;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import dk.aau.cs.giraf.activity.GirafActivity;
import dk.aau.cs.giraf.gui.GComponent;
import dk.aau.cs.giraf.gui.GList;
import dk.aau.cs.giraf.gui.GirafButton;
import dk.aau.cs.giraf.voicegame.Interfaces.Drawable;
import dk.aau.cs.giraf.voicegame.game.GameItem;

/**
 * The activity from where the user can see and choose the tracks that have been saved
 */
public class TrackPickerActivity extends GirafActivity {

    private static final int PLAY_BUTTON_ID = 1;

    /**
     * Called when the activity is started.
     * Instantiates the TrackListAdapter, which makes the content of list shown in this activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = LayoutInflater.from(this).inflate(R.layout.activity_track_picker, null);
        v.setBackgroundDrawable(GComponent.GetBackground(GComponent.Background.GRADIENT));

        createDeleteButton();
        createEditButton();
        createPlayButton();

        setContentView(v);

        ArrayList<Integer> array = new ArrayList<>();
        array.add(0);
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(4);
        array.add(5);

        ListAdapter adapter = new TrackListAdapter(this, array);
        GList trackList = (GList) findViewById(R.id.list_tracks);

        trackList.setAdapter(adapter);

        trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String string = String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(TrackPickerActivity.this, string, Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Creates the play button, shown in the toolbar
     */
    private void createPlayButton() {
        GirafButton playButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_play));

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        addGirafButtonToActionBar(playButton, GirafActivity.RIGHT);
    }

    /**
     * Creates the delete button, shown in the toolbar
     */
    private void createDeleteButton() {
        GirafButton deleteButton = new GirafButton(this, getResources().getDrawable(R.drawable.delete));

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        addGirafButtonToActionBar(deleteButton, GirafActivity.RIGHT);
    }

    /**
     * Creates the edit track button, shown in the toolbar
     */
    private void createEditButton() {
        GirafButton editButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_edit));

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        addGirafButtonToActionBar(editButton, GirafActivity.RIGHT);
    }

}
