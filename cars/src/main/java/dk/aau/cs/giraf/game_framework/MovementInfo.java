package dk.aau.cs.giraf.game_framework;

public class MovementInfo {
    private float pointStart, pointEnd, speedStart;

    public MovementInfo(float pointStart, float pointEnd, float speedStart) {
        this.pointStart = pointStart;
        this.pointEnd = pointEnd;
        this.speedStart = speedStart;
    }

    public float getPointEnd() {
        return pointEnd;
    }

    public float getPointStart() {
        return pointStart;
    }

    public float getSpeedStart() {
        return speedStart;
    }
}
