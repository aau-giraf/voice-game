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
import dk.aau.cs.giraf.voicegame.game.GameScreen;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.voicegame.Interfaces.CarControl;
import dk.aau.cs.giraf.voicegame.game.RoadItem;

public abstract class RunningScreen extends GameScreen {
    private Rect pauseButtonRec = new Rect(20, 20, 100, 100);
    private Rect pauseButtonImageRec = new Rect(0, 0, Assets.GetPlayButton().getWidth(), Assets.GetPlayButton().getHeight());

    private CarControl carControl;
    private float carSpeed;
    // storing whether the car moves on noise or silence;
    private SoundMode soundMode;
    private Track currentTrack;

    // hard coded filename
    // TODO This will be changed in later, already assigned tasks.
    String fileName = "/sdcard/TracksFile";

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

        if (getCarLocationX() + getCarWidth() > game.getWidth() - getFinishLineWidth()) {
            Assets.GetWellDone().Play();
            showWinningScreen();
        }

        // listening for touch on the pause button
        for (Input.TouchEvent e : touchEvents)
            if (e.type == Input.TouchEvent.TOUCH_DOWN && e.inBounds(pauseButtonRec))
                showPauseScreen();
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

    private Track loadTrack() {
        Track trackFromFile = null;
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            trackFromFile = (Track) ois.readObject();
        }catch (FileNotFoundException e){
            System.out.println("File not found - input");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("IO exception happened while reading");
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            System.out.println("Wrong cast");
            e.printStackTrace();
        }

        return trackFromFile;
    }
}
