package dk.aau.cs.giraf.cars.sound;

import android.util.Log;
import java.util.Arrays;
import dk.aau.cs.giraf.cars.gamecode.GameInfo;

public class MicTestThread extends Thread {
    private static final int MAX_FREQ = 3400;
    private static final int MIN_FREQ = 50;
    private static final int FREQ_INTERVAL_SIZE = 50;
    private static final int MAGIC_FREQUENCY_CALIBRATION_NUMBER = 100;

    private boolean isRunning = false;
    private int lowFreq = 0;
    private int highFreq = 0;
    private SetupStates curTestType = SetupStates.Low;
    private boolean restart = false;

    public MicTestThread() { }

    public void SetType(SetupStates testType) { curTestType = testType; }
    public int GetHighFreq() { return highFreq; }
    public int GetLowFreq() { return lowFreq; }

    public void SaveFrequencies() {
        GameInfo.setHighFreq(highFreq);
        GameInfo.setLowFreq(lowFreq);
    }

    public void StopThread() { isRunning = false; }
    public void Restart() { restart = true; }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            switch (curTestType) {
                case High:
                    highFreq = collectFrequency(SetupStates.High) - MAGIC_FREQUENCY_CALIBRATION_NUMBER;
                    Log.v("HIGH", "" + highFreq);
                    break;
                case Low:
                    lowFreq = collectFrequency(SetupStates.Low) + MAGIC_FREQUENCY_CALIBRATION_NUMBER;
                    Log.v("LOW", "" + lowFreq);
                    break;
            }
        }
    }

    private int collectFrequency(SetupStates freqType) {
        int freqIntervals = (MAX_FREQ - MIN_FREQ) / FREQ_INTERVAL_SIZE + 1;
        int[] frequencyRangeArray = new int[freqIntervals];
        int tmpCurrFreq;
        int frequencyIndex;

        while (curTestType == freqType) {
            if (restart) {
                Arrays.fill(frequencyRangeArray, 0);
                restart = false;
            }

            tmpCurrFreq = GameInfo.getCurrFreq();

            if (tmpCurrFreq > 50) {
                frequencyIndex = tmpCurrFreq / FREQ_INTERVAL_SIZE;

                if (frequencyIndex != 0)
                    frequencyRangeArray[frequencyIndex]++;

                try {
                    Thread.sleep(1);
                }
                catch (InterruptedException e) { }
            }
        }

        int highestValueIndex = 0;
        for (int i = 1; i < freqIntervals; i++)
            if (frequencyRangeArray[i] > frequencyRangeArray[highestValueIndex])
                highestValueIndex = i;

        int freq = (highestValueIndex + 1) * FREQ_INTERVAL_SIZE;
        Log.d("FRQ", "Frequency: " + freq);
        return freq;
    }
}
