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

import java.util.List;

public abstract class GameActivity extends Activity implements Game {
    FastRenderView renderView;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    WakeLock wakeLock;

    private Input.TouchEvent[] touchEvents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        Bitmap frameBuffer = Bitmap.createBitmap(size.x, size.y, Config.RGB_565);

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

    void setTouchEvents() {
        List<Input.TouchEvent> events = input.getTouchEvents();
        Input.TouchEvent[] array = new Input.TouchEvent[events.size()];
        events.toArray(array);
        this.touchEvents = array;
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
}