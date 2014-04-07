package dk.aau.cs.giraf.cars.framework;

import android.content.res.Resources;

public interface Game {

    int getWidth();
    int getHeight();

    FileIO getFileIO();
    Audio getAudio() ;
    void setScreen(Screen screen);
    Screen getCurrentScreen();
    Screen getInitScreen();
    GameMessenger getMessenger();

    Resources getResources();
}