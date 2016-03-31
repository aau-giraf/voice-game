package dk.aau.cs.giraf.voicegame;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import dk.aau.cs.giraf.activity.GirafActivity;
import dk.aau.cs.giraf.game_framework.GameActivity;
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

    long currentId, guardianId;
    GirafButton GirafButtonProfileSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        DatabaseHelper database = new DatabaseHelper(this);

        //dk.aau.cs.giraf.dblib.Helper h = new dk.aau.cs.giraf.dblib.Helper(this);
        //h.CreateDummyData();
        // Skip loading screen if monkey test
        if (ActivityManager.isUserAMonkey()) {
            Helper h = new Helper(this);

            currentId = -1;
            guardianId = h.profilesHelper.getGuardians().get(0).getId();
        }
        else {
            currentId = intent.getLongExtra(DatabaseHelper.CHILD_ID, database.GetDefaultChild());
            Log.d("id", Long.toString(currentId));
            guardianId = intent.getLongExtra(DatabaseHelper.GUARDIAN_ID, database.GetChildDefaultGuardian());
        }

        Profile curGuardian = database.GetProfileById(guardianId);
        Profile curProfile;
        if (currentId == -1) {
            curProfile = null;
            currentId = guardianId;
        } else {
            curProfile = database.GetProfileById(currentId);
        }

        setContentView(R.layout.activity_main_menu);

        View v = LayoutInflater.from(this).inflate(R.layout.activity_main_menu, null);
        v.setBackgroundDrawable(GComponent.GetBackground(GComponent.Background.GRADIENT));
        setContentView(v);

        GirafButtonProfileSelect = (GirafButton) findViewById(R.id.profile_button);

        // Set the change user button to open the change user dialog
        GirafButtonProfileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GirafProfileSelectorDialog changeUser = GirafProfileSelectorDialog.newInstance(MainActivity.this, guardianId, false, false, "Vælg den borger du vil skifte til.", CHANGE_USER_SELECTOR_DIALOG);
                changeUser.show(getSupportFragmentManager(), "" + CHANGE_USER_SELECTOR_DIALOG);

            }
        });
        loadWidgets();
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

        Intent intent = new Intent(this, CarGame.class);
        intent.putExtra(DatabaseHelper.CHILD_ID, currentId);
        startActivity(intent);
    }

    public void startMapEditor(View view) {
        Intent intent = new Intent(this, MapEditor.class);
        intent.putExtra(DatabaseHelper.CHILD_ID, currentId);

        startActivity(intent);
    }

    public void showSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);

        intent.putExtra(DatabaseHelper.CHILD_ID, currentId);

        startActivity(intent);
    }

    private void loadWidgets() {
        final GWidgetProfileSelection widgetProfileSelection = (GWidgetProfileSelection) findViewById(R.id.profile_widget);
        Helper h = new Helper(this);
        // Fetch the profile picture
        Bitmap profilePicture = h.profilesHelper.getById(currentId).getImage();
        // If there were no profile picture use the default template
        if (profilePicture == null) {
            // Fetch the default template
            profilePicture = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.no_profile_pic)).getBitmap();
        }

        // Set the profile picture
        widgetProfileSelection.setImageBitmap(profilePicture);
        Log.d("asdf","asdf");
    }

    @Override
    public void onProfileSelected(final int i, final Profile profile) {

        if (i == CHANGE_USER_SELECTOR_DIALOG) {

            // Update the profile
            currentId = profile.getId();
            loadWidgets();
        }

    }
}
