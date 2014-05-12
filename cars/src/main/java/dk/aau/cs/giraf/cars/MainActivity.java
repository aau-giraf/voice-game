package dk.aau.cs.giraf.cars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import dk.aau.cs.giraf.cars.game.CarGame;
import dk.aau.cs.giraf.cars.game.MapEditor;
import dk.aau.cs.giraf.gui.GButtonProfileSelect;
import dk.aau.cs.giraf.gui.GComponent;
import dk.aau.cs.giraf.gui.GTextView;
import dk.aau.cs.giraf.oasis.lib.controllers.ProfileController;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

public class MainActivity extends Activity {

    private static final int SETTINGS_IDENTIFIER = 0;
    private static final int MAPEDITOR_IDENTIFIER = 1;

    int child_id, guardian_id;
    GTextView profile_text;
    GButtonProfileSelect gButtonProfileSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        DatabaseHelper database = new DatabaseHelper(this);

        //Helper h = new Helper(this);
        //h.CreateDummyData();
        child_id = intent.getIntExtra(DatabaseHelper.CHILD_ID, database.GetDefaultChild());
        guardian_id = intent.getIntExtra(DatabaseHelper.GUARDIAN_ID, database.GetChildDefaultGuardian(child_id));

        Profile curChild = database.GetProfileById(child_id);
        Profile curGuardian = database.GetProfileById(guardian_id);

        setContentView(R.layout.activity_main_menu);

        View v = LayoutInflater.from(this).inflate(R.layout.activity_main_menu, null);
        v.setBackgroundDrawable(GComponent.GetBackground(GComponent.Background.GRADIENT));
        setContentView(v);

        profile_text = (GTextView) findViewById(R.id.profile_text);
        gButtonProfileSelect = (GButtonProfileSelect) findViewById(R.id.profile_button);

        profile_text.setText(curChild.getName());
        gButtonProfileSelect.GuardianSelectableInList(false);
        gButtonProfileSelect.setup(curGuardian, curChild, new GButtonProfileSelect.onCloseListener() {
            @Override
            public void onClose(Profile guardianProfile, Profile currentProfile) {
                child_id = currentProfile.getId();
                profile_text.setText(currentProfile.getName());
            }
        });
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, CarGame.class);
        intent.putExtra(DatabaseHelper.CHILD_ID, child_id);
        startActivity(intent);
    }

    public void startMapEditor(View view) {
        Intent intent = new Intent(this, MapEditor.class);
        intent.putExtra(DatabaseHelper.CHILD_ID, child_id);

        startActivityForResult(intent, MAPEDITOR_IDENTIFIER);
    }

    public void showSettings(View view) {
        Intent intent = new Intent(this, Settings.class);

        intent.putExtra(DatabaseHelper.CHILD_ID, child_id);

        startActivityForResult(intent, SETTINGS_IDENTIFIER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("childid", "Childid ved retur: " + child_id);

        switch (requestCode) {
            case (SETTINGS_IDENTIFIER):
                if (resultCode == Activity.RESULT_OK)

                    break;

            case (MAPEDITOR_IDENTIFIER):
                if (resultCode == Activity.RESULT_OK)

                    break;

        }
    }


}
