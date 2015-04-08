package dk.aau.cs.giraf.game_framework;

public class mFloat {
    private long started;
    private boolean running = false;
    private boolean updatedonlast = false;

    private float value;
    private MoveMethod method;
    private MovementInfo info;

    public mFloat(float value, MoveMethod method) {
        this.value = value;
        this.info = new MovementInfo(value, value, 0);
        this.method = method.Clone();
        this.method.setInfo(this.info);
        this.started = java.lang.System.currentTimeMillis();
    }

    public void Update() {
        updatedonlast = running;
        if (!running)
            return;

        float oldval = this.value;
        float time = (java.lang.System.currentTimeMillis() - started) / 1000f;
        if (time >= method.getTime()) {
            time = method.getTime();
            this.value = info.getPointEnd();
            running = false;
        } else
            this.value = method.Position(time);

        if (this.value == Float.NaN || this.value == Float.POSITIVE_INFINITY || this.value == Float.NEGATIVE_INFINITY)
            this.value = oldval;
    }

    public boolean isUpdated() {
        return updatedonlast;
    }

    public float getTargetValue() {
        return info.getPointEnd();
    }

    public void setTargetValue(float value) {
        if (info.getPointEnd() == value)
            return;

        long now = java.lang.System.currentTimeMillis();

        float time = (now - started) / 1000f;
        if (time >= method.getTime())
            time = method.getTime();

        MovementInfo m;
        if (running)
            m = new MovementInfo(method.Position(time), value, method.Speed(time));
        else
            m = new MovementInfo(this.value, value, 0);
        method.setInfo(m);
        this.info = m;
        running = true;

        started = now;
    }

    public float getCurrentValue(){
        return this.value;
    }

    public void setCurrentValue(float value){
        if (!running && this.value == value)
            return;

        running = false;
        this.value = value;
        this.info = new MovementInfo(value, value, 0);
    }

    public float getCurrentTime(){
        return method == null ? 0 : method.getTime();
    }

    public void setMethod(MoveMethod method)
    {
        setCurrentValue(getTargetValue());
        this.method = method;
    }
}
