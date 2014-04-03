package dk.aau.cs.giraf.cars.framework;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class GameFragment extends Fragment implements Game {
    FastRenderView renderView;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    Point size;

    private Input.TouchEvent[] touchEvents;

    public int getWidth() {
        return size.x;
    }

    public int getHeight() {
        return size.y;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Activity activity = getActivity();
        if (activity == null)
            throw new NullPointerException("No activity associated with getActivity()");

        renderView = new FastRenderView(activity, this, null);
        fileIO = new FileIO(activity);
        audio = new Audio(activity);
        input = new Input(activity, renderView, 1, 1);
        screen = getInitScreen();

        this.touchEvents = new Input.TouchEvent[0];

        return renderView;
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

        if (isRemoving())
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
