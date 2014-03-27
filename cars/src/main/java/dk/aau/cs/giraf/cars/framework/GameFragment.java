package dk.aau.cs.giraf.cars.framework;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class GameFragment extends Fragment implements Game {
    FastRenderView renderView;
    Graphics graphics;
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

    public GameFragment(int width, int height) {
        this.size = new Point(width, height);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bitmap frameBuffer = Bitmap.createBitmap(this.size.x, this.size.y, Bitmap.Config.RGB_565);

        Activity activity = getActivity();
        if (activity == null)
            throw new NullPointerException("No activity associated with getActivity()");

        renderView = new FastRenderView(activity, this, frameBuffer);
        graphics = new Graphics(activity.getAssets(), frameBuffer);
        fileIO = new FileIO(activity);
        audio = new Audio(activity);
        input = new Input(activity, renderView, 1, 1);
        screen = getInitScreen();

        this.touchEvents = new Input.TouchEvent[0];

        return renderView;
    }
}
