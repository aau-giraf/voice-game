package dk.aau.cs.giraf.cars.sound;

import android.util.Log;
import java.util.Arrays;
import dk.aau.cs.giraf.cars.gamecode.GameInfo;

public class MicTestThread extends Thread {
    private static final int MAX_FREQUENCY = 3400;
    private static final int MIN_FREQUENCY = 50;
    private static final int FREQ_INTERVAL_SIZE = 50;
    private static final int MAGIC_FREQUENCY_CALIBRATION_NUMBER = 100;

    private boolean isRunning = false;
    private int lowFreq = 0;
    private int highFreq = 0;
    private SetupStates curTestType = SetupStates.Low;
    private boolean restartHigh = false;
    private boolean restartLow = false;

    public MicTestThread() { }

    public void setType(SetupStates testType) { curTestType = testType; }
    public int getHighFreq() { return highFreq; }
    public int getLowFreq() { return lowFreq; }

    public void saveFrequencies() {
        GameInfo.setHighFreq(highFreq);
        GameInfo.setLowFreq(lowFreq);
    }

    public void stopThread() { isRunning = false; }
    public void restartLowFreq() { restartLow = true; }
    public void restartHighFreq() { restartHigh = true; }

    @Override
    public void run() {
        isRunning = true;
        Log.d("WAI", "CollectingFrequencies");
        while (isRunning) {
            switch (curTestType) {
                case High:
                    collectHighFreq();
                    Log.d("WAI", "CollectedHighFrequency");
                    break;
                case Low:
                    collectLowFreq();
                    Log.d("WAI", "CollectedLowFrequency");
                    break;
            }
        }
    }

    private void collectHighFreq() {
        int freqIntervals = (MAX_FREQUENCY - MIN_FREQUENCY) / FREQ_INTERVAL_SIZE + 1;
        int[] frequencyRangeArray = new int[freqIntervals];
        int tmpCurrFreq;
        int frequencyIndex;

        while (curTestType == SetupStates.High) {
            if (restartHigh) {
                Arrays.fill(frequencyRangeArray, 0);
                restartHigh = false;
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

        highFreq = (highestValueIndex + 1) * FREQ_INTERVAL_SIZE - MAGIC_FREQUENCY_CALIBRATION_NUMBER;
        Log.d("FREQ", "High: " + highFreq);
    }

    private void collectLowFreq() {
        int freqIntervals = (MAX_FREQUENCY - MIN_FREQUENCY) / FREQ_INTERVAL_SIZE + 1;
        int[] frequencyRangeArray = new int[freqIntervals];
        int tmpCurrFreq;
        int frequencyIndex;

        while (curTestType == SetupStates.Low) {
            if (restartLow) {
                Arrays.fill(frequencyRangeArray, 0);
                restartLow = false;
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

        lowFreq = (highestValueIndex + 1) * FREQ_INTERVAL_SIZE + MAGIC_FREQUENCY_CALIBRATION_NUMBER;
        Log.d("FREQ", "Low: " + lowFreq);

        System.out.println(lowFreq);
    }
}
