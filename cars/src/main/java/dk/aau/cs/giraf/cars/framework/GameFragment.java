package dk.aau.cs.giraf.cars.framework;

import android.app.Fragment;
import android.graphics.Point;

public abstract class GameFragment extends Fragment implements Game {
    FastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    Point size;
}
