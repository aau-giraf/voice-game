package dk.aau.cs.giraf.cars.gamecode.GameInfo;


import dk.aau.cs.giraf.cars.MicSetupDialogFragment;
import dk.aau.cs.giraf.cars.sound.InputThread;

public abstract class GameType {
protected InputThread InputThread;
protected MicSetupDialogFragment ControlSetup; //change to generalization of MicSetup..

    public GameType()
    {

    }

}
