package dk.aau.cs.giraf.cars.game;

public class AverageList {
    private float[] values;
    private int maxSamples;
    private int sampleCount;
    private int nextIndex;

    public AverageList(int samples){
        this.values = new float[samples];
        this.maxSamples = samples;
        this.sampleCount = 0;
        this.nextIndex = 0;
    }

    public void Add(float sample){
        values[nextIndex] = sample;
        if(sampleCount < maxSamples)
            sampleCount++;
        nextIndex = (nextIndex + 1) % maxSamples;
    }

    public float GetAverage(){
        if(sampleCount == 0)
            return 0;
        float val = 0;
        for(float s : values)
            val += s;
        return  val / (float) sampleCount;
    }
}
