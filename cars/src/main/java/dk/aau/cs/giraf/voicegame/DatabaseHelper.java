package dk.aau.cs.giraf.voicegame;

import android.content.Context;
import android.util.Log;

import dk.aau.cs.giraf.voicegame.game.GameMode;
import dk.aau.cs.giraf.voicegame.Settings.GameSettings;
import dk.aau.cs.giraf.dblib.controllers.ApplicationController;
import dk.aau.cs.giraf.dblib.controllers.ProfileApplicationController;
import dk.aau.cs.giraf.dblib.controllers.ProfileController;
import dk.aau.cs.giraf.dblib.models.Application;
import dk.aau.cs.giraf.dblib.models.Profile;
import dk.aau.cs.giraf.dblib.models.ProfileApplication;
import dk.aau.cs.giraf.dblib.models.Settings;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stefan on 29-04-2014.
 */
public class DatabaseHelper {
    public static final String CHILD_ID = "currentChildID";
    public static final String GUARDIAN_ID = "currentGuardianID";

    Context context;
    long child_id;

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

    public void Initialize(long child_id) {
        this.child_id = child_id;
        profileApplication = LoadProfileApplication(child_id);
    }

    private GameSettings ParseSettings(Settings settings) {

        Log.d("database","parse");

        //Hack to check if settings is empty, there is no method for this.
        if(settings.toJSON().equals("{}")){
            Log.d("database", "opret ny gamesettings");
            return new GameSettings();
        }

        int color = Integer.parseInt(settings.getSetting(this.context, "color"));
        float speed = Float.parseFloat(settings.getSetting(this.context, "speed"));
        float min = Float.parseFloat(settings.getSetting(this.context, "minvolume"));
        float max = Float.parseFloat(settings.getSetting(this.context, "maxvolume"));

        String mapSettings = settings.getSetting(this.context, "map");

        HashMap<String, Float> map = new HashMap<String, Float>();
        if(mapSettings.length() > 0){
        String[] keyValuePair = mapSettings.split(";");
            for (String e: keyValuePair){
                String[] keyAndValue = e.split(":");
                map.put(keyAndValue[0],Float.valueOf(keyAndValue[1]));
            }
        }

        GameMode gameMode = GameMode.valueOf(settings.getSetting(this.context, "game_mode"));

        return new GameSettings(color, speed, min, max, gameMode);
    }

    public long GetDefaultChild() {
        return profileController.getChildren().get(0).getId();
    }

    public long GetChildDefaultGuardian() {
        return profileController.getGuardians().get(0).getId();
    }

    public Profile GetProfileById(long id) {
        return profileController.getProfileById(id);
    }

    public GameSettings GetGameSettings() {
        Settings setting;
        Profile childProfile = profileController.getProfileById(this.child_id);
        if(childProfile == null){
            setting = new Settings();
        } else {
            setting = childProfile.getNewSettings();
        }
        return ParseSettings(setting);
    }

    public void SaveSettings(GameSettings gs) {

        Settings s = new Settings();
        s.createSetting(this.context, "speed", Float.toString(gs.GetSpeed()));
        s.createSetting(this.context, "color", Integer.toString(gs.GetColor()));
        s.createSetting(this.context, "minvolume", Float.toString(gs.GetMinVolume()));
        s.createSetting(this.context, "maxvolume", Float.toString(gs.GetMaxVolume()));
        s.createSetting(this.context, "game_mode", gs.GetGameMode().name());

        Profile childProfile = profileController.getProfileById(this.child_id);
        childProfile.setNewSettings(s);
        profileController.modify(childProfile);

    }

    private void SaveMapToSettings(HashMap<String,Float> map, Settings s)
    {
        String stringMap = "";
        for (Map.Entry<String,Float> e: map.entrySet())
                stringMap += e.getKey() + ":" + Float.toString(e.getValue()) + ";";
        s.createSetting(this.context, "map", stringMap);

    }

    private ProfileApplication LoadProfileApplication(long id) {
        Profile profile = profileController.getProfileById(id);

        ProfileApplication profileApplication = profileApplicationController.getProfileApplicationByProfileIdAndApplicationId(application, profile);

        Log.d("database", Boolean.toString(application == null) + " " + Boolean.toString(profile == null) + " " + Boolean.toString(profileApplication == null));

        return profileApplication;
    }




}
