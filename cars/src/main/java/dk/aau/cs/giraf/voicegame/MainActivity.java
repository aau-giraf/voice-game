package dk.aau.cs.giraf.voicegame;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import dk.aau.cs.giraf.activity.GirafActivity;
import dk.aau.cs.giraf.voicegame.Settings.GameSettings;
import dk.aau.cs.giraf.voicegame.game.CarGame;
import dk.aau.cs.giraf.gui.GComponent;
import dk.aau.cs.giraf.dblib.Helper;
import dk.aau.cs.giraf.dblib.models.Profile;
import dk.aau.cs.giraf.gui.GWidgetProfileSelection;
import dk.aau.cs.giraf.gui.GirafButton;
import dk.aau.cs.giraf.gui.GirafProfileSelectorDialog;
import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends GirafActivity implements GirafProfileSelectorDialog.OnSingleProfileSelectedListener {

    private static final int SETTINGS_IDENTIFIER = 0;
    private static final int MAPEDITOR_IDENTIFIER = 1;
    private static final int CHANGE_USER_SELECTOR_DIALOG = 100;
    private GameSettings gameSettings;

    GirafButton GirafButtonProfileSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setGameSettings(GameSettings.LoadSettings(getApplicationContext()));
        setContentView(R.layout.activity_main_menu);

        View v = LayoutInflater.from(this).inflate(R.layout.activity_main_menu, null);
        v.setBackgroundDrawable(GComponent.GetBackground(GComponent.Background.GRADIENT));
        setContentView(v);

        GirafButtonProfileSelect = (GirafButton) findViewById(R.id.profile_button);

        // Set the change user button to open the change user dialog
        GirafButtonProfileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GirafProfileSelectorDialog changeUser = GirafProfileSelectorDialog.newInstance(MainActivity.this, 1, false, false, getResources().getString(R.string.Profile_selector), CHANGE_USER_SELECTOR_DIALOG);
                changeUser.show(getSupportFragmentManager(), "" + CHANGE_USER_SELECTOR_DIALOG);

            }
        });
        loadWidgets();

        // TODO This will delete the trackOrganizer, remove before push
        IOService.instance().deleteTrackOrganizer();
    }


    //Google analytics - start logging
    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);  // Start logging
    }
    //Google analytics - Stop logging
    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);  // stop logging
    }

    public void startGame(View view) {

        /*
        Intent intent = new Intent(this, CarGame.class);
        intent.putExtra("settings", getGameSettings());
        startActivity(intent);
        */

        Intent intent = new Intent(this, TrackPickerActivity.class);
        intent.putExtra("settings", getGameSettings());
        startActivity(intent);

    }

    public void startMapEditor(View view) {
        Intent intent = new Intent(this, MapEditor.class);
        intent.putExtra("settings", getGameSettings());
        startActivity(intent);
    }

    public void showSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);

        intent.putExtra("settings", getGameSettings());

        startActivityForResult(intent, 0);
    }

    private void loadWidgets() {
        final GWidgetProfileSelection widgetProfileSelection = (GWidgetProfileSelection) findViewById(R.id.profile_widget);

        // Fetch the default template
        Bitmap profilePicture = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.no_profile_pic)).getBitmap();

        // Set the profile picture
        widgetProfileSelection.setImageBitmap(profilePicture);
        Log.d("asdf","asdf");
    }

    @Override
    public void onProfileSelected(final int i, final Profile profile) {

        if (i == CHANGE_USER_SELECTOR_DIALOG) {

            // Update the profile
            loadWidgets();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    setGameSettings((GameSettings) data.getSerializableExtra("settings"));
                }
                break;
        }
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }
}
