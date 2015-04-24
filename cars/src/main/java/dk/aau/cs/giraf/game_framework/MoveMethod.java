package dk.aau.cs.giraf.game_framework;

/**
 * Created by Mikkel on 30-04-2014.
 */
public interface MoveMethod {
    MoveMethod Clone();

    MovementInfo getInfo();
    void setInfo(MovementInfo value);

    float Position(float time);
    float Speed(float time);

    float getTime();
}
