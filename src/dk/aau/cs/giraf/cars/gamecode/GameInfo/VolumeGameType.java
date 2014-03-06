package dk.aau.cs.giraf.cars.gamecode.GameInfo;


import dk.aau.cs.giraf.cars.MicSetupDialogFragment;
import dk.aau.cs.giraf.cars.sound.InputThread;

public class VolumeGameType  extends GameType {
    private float minVolume;
    private float maxVolume;

    private static final float defaultMinVolume = 10;
    private static final float defaultMaxVolume = 100;


    public VolumeGameType(float minVolume, float maxVolume){
        throw new UnsupportedOperationException();

        //Change to Volume specific implementations
        super.InputThread = new InputThread();//
        super.ControlSetup = new MicSetupDialogFragment();

//        this.minVolume = minVolume;
       //      this.maxVolume= maxVolume;
    }

    public VolumeGameType()
    {
            this(defaultMinVolume,defaultMaxVolume);
    }

}
