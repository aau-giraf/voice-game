package dk.aau.cs.giraf.cars.framework;

public interface Game {

    int getWidth();
    int getHeight();

    Input getInput();
    FileIO getFileIO();
    Graphics getGraphics();
    Audio getAudio() ;
    void setScreen(Screen screen);
    Screen getCurrentScreen();
    abstract Screen getInitScreen();
}