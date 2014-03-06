package dk.aau.cs.giraf.cars.gamecode.GameInfo;


import dk.aau.cs.giraf.cars.MicSetupDialogFragment;
import dk.aau.cs.giraf.cars.sound.RecorderThread;

public abstract class GameType {
protected RecorderThread InputThread; //change to generalization of recorderthread
protected MicSetupDialogFragment ControlSetup; //change to generalization of MicSetup..

    public GameType()
    {

    }

}
