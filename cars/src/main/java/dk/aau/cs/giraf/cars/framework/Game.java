package dk.aau.cs.giraf.cars.framework;

import android.content.res.Resources;

public interface Game {

    int getWidth();
    int getHeight();

    Input getInput();
    FileIO getFileIO();
    Graphics getGraphics();
    Audio getAudio() ;
    void setScreen(Screen screen);
    Screen getCurrentScreen();
    Screen getInitScreen();
    Input.TouchEvent[] getTouchEvents();

    Resources getResources();
}