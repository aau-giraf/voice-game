package dk.aau.cs.giraf.voicegame.game.GameScreens;

import android.graphics.Rect;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import dk.aau.cs.giraf.voicegame.Assets;
import dk.aau.cs.giraf.voicegame.Track;
import dk.aau.cs.giraf.voicegame.game.Car;
import dk.aau.cs.giraf.voicegame.game.CarGame;
import dk.aau.cs.giraf.voicegame.game.Enums.SoundMode;
import dk.aau.cs.giraf.voicegame.game.GameItemCollection;
import dk.aau.cs.giraf.voicegame.game.GameMode;
import dk.aau.cs.giraf.voicegame.game.GameScreen;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.voicegame.Interfaces.CarControl;
import dk.aau.cs.giraf.voicegame.game.RoadItem;

/**
 * Class shared by both avoid and pickup game modes
 */
public class RunningScreen extends GameScreen {
    private Rect pauseButtonRec = new Rect(20, 20, 100, 100);
    private Rect pauseButtonImageRec = new Rect(0, 0, Assets.GetPlayButton().getWidth(), Assets.GetPlayButton().getHeight());

    private CarControl carControl;
    private float carSpeed;
    // storing whether the car moves on noise or silence;
    private SoundMode soundMode;
    private Track currentTrack;

    public RunningScreen(CarGame game, Car car, GameItemCollection obstacles, CarControl carControl, float carSpeed, Track track, SoundMode soundMode) {
        super(game, car, obstacles);
        this.carControl = carControl;
        this.carSpeed = carSpeed;
        this.soundMode = soundMode;

        currentTrack = track;

        currentTrack.initRoadItems();

    }

    // Method that is called when the game is running
    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents, deltaTime);

        float moveTo = 1f - carControl.getMove(touchEvents, soundMode);
        moveTo = Math.max(0, Math.min(1, moveTo));
        moveCarTo(moveTo);

        checkWinCondition();

        // listening for touch on the pause button
        for (Input.TouchEvent e : touchEvents)
            if (e.type == Input.TouchEvent.TOUCH_DOWN && e.inBounds(pauseButtonRec))
                showPauseScreen();

        RoadItem roadItem = getCollisionRoadItem();
        if (roadItem != null) {
            if(currentTrack.getMode() == GameMode.pickup) {
                Assets.GetPickup().PlayAndReset();
                removeObstacle(roadItem);
            } else {
                showCrashScreen(roadItem);
            }
        }
    }

    /**
     * When the car hit the finishline, check which game mode is being played and act accordinglys
     */
    public void checkWinCondition() {

        if(getCarLocationX() + getCarWidth() > game.getWidth() - getFinishLineWidth()) {
            if(currentTrack.getMode() == GameMode.pickup) {
                if(!isRoadItemsEmpty()) {
                    showFailureScreen();
                } else {
                    Assets.GetWellDone().Play();
                    showWinningScreen();
                }
            } else {
                Assets.GetWellDone().Play();
                showWinningScreen();
            }
        }
    }

    /**
     * Draws the pause button.
     * Also calls the super class, GameScreen, which draws the obstacles in the track.
     * @param graphics
     * @param deltaTime
     */
    @Override
    public void paint(Graphics graphics, float deltaTime) {
        super.paint(graphics, deltaTime);

        graphics.drawScaledImage(Assets.GetPauseButton(), pauseButtonRec, pauseButtonImageRec);
    }

    @Override
    public void showScreen() {
        setCarSpeed(carSpeed);
    }
}
