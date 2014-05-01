package dk.aau.cs.giraf.cars;

import android.content.Context;
import android.util.Log;
import dk.aau.cs.giraf.cars.game.GameSettings;
import dk.aau.cs.giraf.oasis.lib.controllers.ApplicationController;
import dk.aau.cs.giraf.oasis.lib.controllers.ProfileApplicationController;
import dk.aau.cs.giraf.oasis.lib.controllers.ProfileController;
import dk.aau.cs.giraf.oasis.lib.models.Application;
import dk.aau.cs.giraf.oasis.lib.models.Profile;
import dk.aau.cs.giraf.oasis.lib.models.ProfileApplication;
import dk.aau.cs.giraf.oasis.lib.models.Setting;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Stefan on 29-04-2014.
 */
public class DatabaseHelper {
    public static final String CHILD_ID = "currentChildID";
    public static final String SETTINGS = "settings";
    Context context;
    int child_id;

    Application application;
    ProfileApplication profileApplication;
    ProfileController profileController;
    ProfileApplicationController profileApplicationController;

    public DatabaseHelper(Context context) {
        this.context = context;

        ApplicationController applicationController = new ApplicationController(context);
        application = applicationController.getApplicationByPackageName();
        profileApplicationController = new ProfileApplicationController(context);

        profileController = new ProfileController(context);
    }

    public void Initialize(int child_id)
    {
        this.child_id = child_id;
        profileApplication = LoadProfileApplication(child_id);
    }

    private GameSettings ParseSettings(Setting<String, String, String> settings) {

        if(settings.isEmpty())
            return new GameSettings();

        HashMap<String, String> colors = settings.get("colors");

        LinkedList<Integer> colorlist = new LinkedList<Integer>();
        for (Map.Entry<String, String> entry : colors.entrySet())
            colorlist.add(Integer.parseInt(entry.getValue()));

        float speed = Float.parseFloat(settings.get("speed").get("default"));

        float min = Float.parseFloat(settings.get("calibration").get("min"));
        float max = Float.parseFloat(settings.get("calibration").get("max"));

        return new GameSettings(colorlist, speed, min, max);
    }

    public int GetDefaultChild()
    {
        return profileController.getChildren().get(0).getId();
    }

    public GameSettings GetGameSettings() {
        Setting<String, String, String> setting = profileApplication.getSettings();
        return ParseSettings(setting);
    }

    public void SaveSettings(GameSettings gs) {
        Setting<String, String, String> s = new Setting<String, String, String>();
        s.addValue("speed", "default", Float.toString(gs.GetSpeed()));
        s.addValue("colors", "1", Integer.toString(gs.GetColors().get(0)));
        s.addValue("colors", "2", Integer.toString(gs.GetColors().get(0)));
        s.addValue("colors", "3", Integer.toString(gs.GetColors().get(0)));
        s.addValue("calibration", "min", Float.toString(gs.GetMinVolume()));
        s.addValue("calibration", "max", Float.toString(gs.GetMinVolume()));

        profileApplication.setSettings(s);

        profileApplicationController.modifyProfileApplication(profileApplication);
    }

    private ProfileApplication LoadProfileApplication(int child_id) {
        Profile profile = profileController.getProfileById(child_id);

        ProfileApplication profileApplication = profileApplicationController.getProfileApplicationByProfileIdAndApplicationId(application, profile);

        Log.d("database", Boolean.toString(application==null) + " "  + Boolean.toString(profile==null) + " " + Boolean.toString(profileApplication==null));

        return profileApplication;
    }
}
