package dk.aau.cs.giraf.voicegame.game.GameScreens;

import android.graphics.Rect;
import android.util.Log;
import dk.aau.cs.giraf.voicegame.Assets;
import dk.aau.cs.giraf.voicegame.Settings.GameSettings;
import dk.aau.cs.giraf.voicegame.game.Car;
import dk.aau.cs.giraf.voicegame.game.CarGame;
import dk.aau.cs.giraf.voicegame.game.GameItemCollection;
import dk.aau.cs.giraf.voicegame.game.GameScreen;
import dk.aau.cs.giraf.game_framework.Graphics;
import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.voicegame.Interfaces.CarControl;

public abstract class RunningScreen extends GameScreen {
    private Rect pauseButtonRec = new Rect(20, 20, 100, 100);
    private Rect pauseButtonImageRec = new Rect(0, 0, Assets.GetPlayButton().getWidth(), Assets.GetPlayButton().getHeight());

    private CarControl carControl;
    private float carSpeed;
    // storing wether the car moves on noise or silence;
    private GameSettings.MoveState moveState;

    public RunningScreen(CarGame game, Car car, GameItemCollection obstacles, CarControl carControl, float carSpeed, GameSettings.MoveState moveState) {
        super(game, car, obstacles);
        this.carControl = carControl;
        this.carSpeed = carSpeed;
        this.moveState = moveState;
    }

    // Method that is called when the game is running
    @Override
    public void update(Input.TouchEvent[] touchEvents, float deltaTime) {
        super.update(touchEvents, deltaTime);

        float moveTo = 1f - carControl.getMove(touchEvents, moveState);
        moveTo = Math.max(0, Math.min(1, moveTo));
        moveCarTo(moveTo);

        // listening for touch on the pause button
        for (Input.TouchEvent e : touchEvents)
            if (e.type == Input.TouchEvent.TOUCH_DOWN && e.inBounds(pauseButtonRec))
                showPauseScreen();
    }

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
