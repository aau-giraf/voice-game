package dk.aau.cs.giraf.voicegame.Interfaces;

import dk.aau.cs.giraf.game_framework.Input;
import dk.aau.cs.giraf.voicegame.Settings.GameSettings;
import dk.aau.cs.giraf.voicegame.game.Enums.MoveState;

public interface CarControl {
    public float getMove(Input.TouchEvent[] touchEvents, MoveState moveState);
    public int getBarometerNumber(float y, float height);
    public void Reset();
}
