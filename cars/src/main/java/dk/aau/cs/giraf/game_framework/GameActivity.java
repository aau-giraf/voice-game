package dk.aau.cs.giraf.game_framework;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import dk.aau.cs.giraf.activity.GirafActivity;

public abstract class GameActivity extends GirafActivity implements Game {
    protected FastRenderView renderView;
    Audio audio;
    FileIO fileIO;
    Screen screen;
    Point size;
    GameMessenger messenger;

    protected static final String SAVE_DIALOG_TAG = "SAVE_DIALOG";
    protected static final Integer SAVE_DIALOG_ID = 1;

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

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                        & WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int frameBufferWidth = 1280;
        int frameBufferHeight = 800;
        this.size = new Point(frameBufferWidth, frameBufferHeight);

        Point actual_size = new Point();
        getWindowManager().getDefaultDisplay().getSize(actual_size);
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);

        float scaleX = (float) frameBufferWidth / actual_size.x;
        float scaleY = (float) frameBufferHeight / actual_size.y;

        renderView = new FastRenderView(this, this, frameBuffer, scaleX, scaleY);
        fileIO = new FileIO(this);
        audio = new Audio(this);
        screen = getInitScreen();

        setContentView(getContentView(renderView));
    }

    public View getContentView(FastRenderView renderview){
        return renderview;
    }

    @Override
    public void onResume() {
        super.onResume();
        screen.resume();
        renderView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
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

        this.screen.hideScreen();

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(new Input.TouchEvent[0], 0);
        this.screen = screen;

        this.screen.showScreen();
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
        public void setSize(int width, int height) {
            game.size = new Point(width, height);
        }
    }
}