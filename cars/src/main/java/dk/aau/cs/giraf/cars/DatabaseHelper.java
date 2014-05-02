package dk.aau.cs.giraf.cars;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.util.Log;
import dk.aau.cs.giraf.cars.game.GameSettings;
import dk.aau.cs.giraf.cars.game.Obstacle;
import dk.aau.cs.giraf.oasis.lib.controllers.ApplicationController;
import dk.aau.cs.giraf.oasis.lib.controllers.ProfileApplicationController;
import dk.aau.cs.giraf.oasis.lib.controllers.ProfileController;
import dk.aau.cs.giraf.oasis.lib.models.Application;
import dk.aau.cs.giraf.oasis.lib.models.Profile;
import dk.aau.cs.giraf.oasis.lib.models.ProfileApplication;
import dk.aau.cs.giraf.oasis.lib.models.Setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Stefan on 29-04-2014.
 */
public class DatabaseHelper {
    public static final String CHILD_ID = "currentChildID";
    public static final String SETTINGS = "settings";

    private static final int OBSTACLE_SIZE = 100;

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

    public void Initialize(int child_id) {
        this.child_id = child_id;
        profileApplication = LoadProfileApplication(child_id);
    }

    private GameSettings ParseSettings(Setting<String, String, String> settings) {

        Log.d("database","parse");
        if (settings.isEmpty()) {
            Log.d("database", "opret ny gamesettings");
            return new GameSettings();
        }

        HashMap<String, String> colors = settings.get("colors");

        LinkedList<Integer> colorlist = new LinkedList<Integer>();
        for (Map.Entry<String, String> entry : colors.entrySet())
            colorlist.add(Integer.parseInt(entry.getValue()));

        float speed = Float.parseFloat(settings.get("speed").get("default"));

        float min = Float.parseFloat(settings.get("calibration").get("min"));
        float max = Float.parseFloat(settings.get("calibration").get("max"));

        HashMap<String,String> mapsettings = settings.get("map");
        Log.d("database","w " + Boolean.toString(mapsettings==null));

        HashMap<String, Float> map = new HashMap<String, Float>();
        if(mapsettings!= null) {
            for (Map.Entry<String, String> entry : mapsettings.entrySet())
                map.put(entry.getKey(), Float.valueOf(entry.getValue()));
        }


        return new GameSettings(colorlist, speed, min, max,map);
    }

    public int GetDefaultChild() {
        return profileController.getChildren().get(0).getId();
    }

    public GameSettings GetGameSettings() {
        Setting<String, String, String> setting = profileApplication.getSettings();
        return ParseSettings(setting);
    }

    public void SaveSettings(GameSettings gs) {
        Setting<String, String, String> s = new Setting<String, String, String>();
        s.addValue("speed", "default", Float.toString(gs.GetSpeed()));
        s.addValue("colors", "0", Integer.toString(gs.GetColors().get(0)));
        s.addValue("colors", "1", Integer.toString(gs.GetColors().get(1)));
        s.addValue("colors", "2", Integer.toString(gs.GetColors().get(2)));
        s.addValue("calibration", "min", Float.toString(gs.GetMinVolume()));
        s.addValue("calibration", "max", Float.toString(gs.GetMaxVolume()));


        Log.d("database","jeg gemmer map der er " + gs.GetMap().size());
        SaveMapToSettings(gs.GetMap(),s);

        profileApplication.setSettings(s);

        profileApplicationController.modifyProfileApplication(profileApplication);
    }

    private void SaveMapToSettings(HashMap<String,Float> map, Setting<String,String,String> s)
    {

        for (Map.Entry<String,Float> e: map.entrySet())
            s.addValue("map",e.getKey(),Float.toString(e.getValue()));
        if(s.get("map")!=null)
        Log.d("database","mapsize ved save " + Integer.toString(s.get("map").size()));

    }

    private ProfileApplication LoadProfileApplication(int child_id) {
        Profile profile = profileController.getProfileById(child_id);

        ProfileApplication profileApplication = profileApplicationController.getProfileApplicationByProfileIdAndApplicationId(application, profile);

        Log.d("database", Boolean.toString(application == null) + " " + Boolean.toString(profile == null) + " " + Boolean.toString(profileApplication == null));

        return profileApplication;
    }




}
