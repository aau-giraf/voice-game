package dk.aau.cs.giraf.cars;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import dk.aau.cs.giraf.cars.Game.CarGame;
import dk.aau.cs.giraf.gui.GButtonProfileSelect;
import dk.aau.cs.giraf.gui.GComponent;
import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

public class MainActivity extends Activity {

    private static final int SETTINGS_IDENTIFIER = 0;
    private static final int MAPEDITOR_IDENTIFIER = 1;

    int currentId, guardianId;
    GButtonProfileSelect gButtonProfileSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        DatabaseHelper database = new DatabaseHelper(this);

        //dk.aau.cs.giraf.oasis.lib.Helper h = new dk.aau.cs.giraf.oasis.lib.Helper(this);
        //h.CreateDummyData();
        // Skip loading screen if monkey test
        if (ActivityManager.isUserAMonkey()) {
            Helper h = new Helper(this);
            h.CreateDummyData();

            currentId = -1;
            guardianId = h.profilesHelper.getGuardians().get(0).getId();
        }
        else {
            currentId = intent.getIntExtra(DatabaseHelper.CHILD_ID, database.GetDefaultChild());
            Log.d("id", Integer.toString(currentId));
            guardianId = intent.getIntExtra(DatabaseHelper.GUARDIAN_ID, database.GetChildDefaultGuardian());
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

        gButtonProfileSelect = (GButtonProfileSelect) findViewById(R.id.profile_button);

        gButtonProfileSelect.setup(curGuardian, curProfile, new GButtonProfileSelect.onCloseListener() {
            @Override
            public void onClose(Profile guardianProfile, Profile currentProfile) {
                if (currentProfile == null)
                    currentId = guardianProfile.getId();
                else
                    currentId = currentProfile.getId();
            }
        });
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
}
