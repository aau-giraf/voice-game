package dk.aau.cs.giraf.cars.framework;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

public abstract class GameActivity extends Activity implements Game {
    FastRenderView renderView;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    WakeLock wakeLock;
    Point size;
    GameMessenger messenger;

    private Input.TouchEvent[] touchEvents;

    public GameActivity() {
        this.messenger = new Messenger(this);
    }

    public int getWidth() {
        return size.x;
    }

    public int getHeight() {
        return size.y;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.size = new Point();
        getWindowManager().getDefaultDisplay().getSize(this.size);
        Bitmap frameBuffer = Bitmap.createBitmap(this.size.x, this.size.y, Config.RGB_565);

        renderView = new FastRenderView(this, this, frameBuffer);
        fileIO = new FileIO(this);
        audio = new Audio(this);
        input = new Input(this, renderView, 1, 1);
        screen = getInitScreen();
        setContentView(renderView);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyGame");

        this.touchEvents = new Input.TouchEvent[0];
    }

    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    public Input.TouchEvent[] getTouchEvents() {
        return touchEvents;
    }

    public Input getInput() {
        return input;
    }

    public FileIO getFileIO() {
        return fileIO;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    public Screen getCurrentScreen() {

        return screen;
    }

    public abstract Screen getInitScreen();

    public GameMessenger getMessenger() {
        return messenger;
    }

    private class Messenger extends GameMessenger {
        private GameActivity game;

        public Messenger(GameActivity game) {
            this.game = game;
        }

        @Override
        public void setTouchEvents(Input.TouchEvent[] touchEvents) {
            game.touchEvents = touchEvents;
        }

        @Override
        public void setSize(int width, int height) {
            game.size = new Point(width, height);
        }
    }
}