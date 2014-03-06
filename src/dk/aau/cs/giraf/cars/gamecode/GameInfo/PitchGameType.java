package dk.aau.cs.giraf.cars.gamecode.GameInfo;

import dk.aau.cs.giraf.cars.MicSetupDialogFragment;
import dk.aau.cs.giraf.cars.sound.InputThread;

public class PitchGameType extends GameType{

    private float minPitch;
    private float maxPitch;

    private static final float defaultMinPitch = 10;
    private static final float defaultMaxPitch = 100;


    public PitchGameType(float minPitch, float maxPitch){

        //Change to Volume specific implementations
        super.InputThread = new InputThread();//
        super.ControlSetup = new MicSetupDialogFragment();

        this.minPitch = minPitch;
        this.maxPitch = maxPitch;
    }

    public PitchGameType()
    {
        this(defaultMinPitch, defaultMaxPitch);
    }

}
