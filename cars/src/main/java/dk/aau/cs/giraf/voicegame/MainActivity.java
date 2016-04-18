package dk.aau.cs.giraf.voicegame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import dk.aau.cs.giraf.activity.GirafActivity;
import dk.aau.cs.giraf.voicegame.Settings.GameSettings;
import dk.aau.cs.giraf.gui.GComponent;
import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends GirafActivity{

    private GameSettings gameSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setGameSettings(GameSettings.LoadSettings(getApplicationContext()));
        setContentView(R.layout.activity_main_menu);

        View v = LayoutInflater.from(this).inflate(R.layout.activity_main_menu, null);
        v.setBackgroundDrawable(GComponent.GetBackground(GComponent.Background.GRADIENT));
        setContentView(v);
        // TODO Remove this before submission
        IOService.instance().deleteTrackOrganizer(getApplicationContext());
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

    /**
     * Upon game start, the TrackPickerActivity is started, which allows the user to pick a track to play
     * The method is set as the onClick method for the start button in the menu.
     * @param view the view that was clicked
     */
    public void startGame(View view) {
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
