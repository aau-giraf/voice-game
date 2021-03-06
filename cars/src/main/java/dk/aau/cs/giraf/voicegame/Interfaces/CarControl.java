package dk.aau.cs.giraf.voicegame.Interfaces;

import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.voicegame.game.Enums.SoundMode;

public interface CarControl {
    public float getMove(Input.TouchEvent[] touchEvents, SoundMode soundMode);
    public int getBarometerNumber(float y, float height);
    public void Reset();
}
