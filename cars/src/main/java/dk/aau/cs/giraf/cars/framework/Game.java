package dk.aau.cs.giraf.cars.framework;

import android.content.res.Resources;

public interface Game {

    int getWidth();
    int getHeight();

    Input getInput();
    FileIO getFileIO();
    Audio getAudio() ;
    void setScreen(Screen screen);
    Screen getCurrentScreen();
    Screen getInitScreen();
    Input.TouchEvent[] getTouchEvents();
    GameMessenger getMessenger();

    Resources getResources();
}