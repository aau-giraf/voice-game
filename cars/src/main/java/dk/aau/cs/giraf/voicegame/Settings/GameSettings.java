package dk.aau.cs.giraf.voicegame.Settings;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import org.apache.commons.lang.enums.*;

import dk.aau.cs.giraf.voicegame.SettingsActivity;
import dk.aau.cs.giraf.voicegame.game.Enums.MoveState;
import dk.aau.cs.giraf.voicegame.game.GameMode;
import dk.aau.cs.giraf.voicegame.game.RoadItem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;

/**
 * Class that stores the settings for the game.
 */
public class GameSettings implements Serializable {
    public final int OBSTACLE_SIZE = 100;
    private final float DEFAULT_SPEED = 2.0f;
    private final float DEFAULT_MIN = 0;
    private final float DEFAULT_MAX = 5000;
    private final int DEFAULT_COLOR = Color.BLUE;
    private final GameMode DEFAULT_GAME_MODE = GameMode.pickup;
    private int color;
    private float speed;
    private float minVolume;
    private float maxVolume;
    private ArrayList<RoadItem> roadItems;
    private GameMode gameMode;
    // when set to silence, the car moves when the microphone is not picking up sound.
    // TODO implement way to set this property in the settings menu
    private MoveState moveState = MoveState.silence;


    public GameSettings() {
        this.color = DEFAULT_COLOR;
        this.speed = DEFAULT_SPEED;
        this.minVolume = DEFAULT_MIN;
        this.maxVolume = DEFAULT_MAX;
        this.gameMode = DEFAULT_GAME_MODE;
    }

    public GameSettings(int color, float speed, float minVolume, float maxVolume, GameMode gameMode) {
        this.color = color;
        this.speed = speed;
        this.minVolume = minVolume;
        this.maxVolume = maxVolume;
        this.gameMode = gameMode;
    }

    public int GetColor() {
        return color;
    }

    public float GetSpeed() {
        return speed;
    }

    public float GetMinVolume() {
        return minVolume;
    }

    public float GetMaxVolume() {
        return maxVolume;
    }

    public void setRoadItem(ArrayList<RoadItem> roadItems) {
        this.roadItems = roadItems;
    }

    public MoveState getMoveState() { return moveState; }

    public ArrayList<RoadItem> LoadObstacles() {

        return roadItems;
    }

    public GameMode GetGameMode() {
        return gameMode;
    }

    /**
     * Reads GameSettings from local file
     * @param context
     * @return GameSettings
     */
    public static GameSettings LoadSettings(Context context) {
        FileInputStream fis = null;
        GameSettings gs = null;
        try {
            fis = context.openFileInput("vg_settings");
        } catch (FileNotFoundException e) {
            SettingsActivity.SaveSettings(null, context);//Creates new save file with default settings
            try {
                fis = context.openFileInput("vg_settings");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(fis);
            gs = (GameSettings)is.readObject();
            is.close();
            fis.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(gs != null){
            return gs;
        } else {
            return new GameSettings();
        }
    }
}
