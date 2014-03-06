package dk.aau.cs.giraf.cars.sound;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;

import dk.aau.cs.giraf.cars.gamecode.GameInfo.GameInfo;


public class PitchRecorder extends InputThread {
    public boolean recording;  //variable to start or stop recording
    public final int highestHumanPitch = 3400; //Determine the highest frequency a human can make to get rid of false data
    public final int voiceSensitivity = 10000;  //Determine the "volume" that that has to be recorded before the input data is valid

    private int bufferSize;
    private final int sampleRate = 44100;
    private short[] audioData;

    public PitchRecorder() {
    }

    private AudioRecord initialize() {
        System.out.println("Initializing RecorderThread");

        bufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT) * 2;
        AudioRecord recorder = new AudioRecord(AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

        recording = true;
        audioData = new short[bufferSize];

        return recorder;
    }

    @Override
    public void run() {
        AudioRecord recorder = initialize();

        while (recording) {
            if (recorder.getRecordingState() == android.media.AudioRecord.RECORDSTATE_STOPPED) {
                recorder.startRecording();
            } else {
                //read the PCM audio data into the audioData array
                recorder.read(audioData, 0, bufferSize);
                GameInfo.setCurrFreq(calculateCurrentFrequency());
            }
        }

        if (recorder.getState() == android.media.AudioRecord.RECORDSTATE_RECORDING)
            recorder.stop(); //stop the recorder before ending the thread
        recorder.release(); //release the recorders resources
        recorder = null; //set the recorder to be garbage collected.
    }

    private int calculateCurrentFrequency() {
        short[] imgAudioData = new short[bufferSize];
        //Decode the PCM data using the Zero Crossings Method
        double[] endAudioData = FFT.fft(audioData, imgAudioData, true);

        double[] magnitude = new double[bufferSize - 1];
        int highestMagnitude = 0;
        for (int p = 2; p < (bufferSize - 1) * 2; p += 2) {
            magnitude[p / 2 - 1] = Math.sqrt(endAudioData[p] * endAudioData[p] + endAudioData[p + 1] * endAudioData[p + 1]);
            if (magnitude[p / 2 - 1] > magnitude[highestMagnitude]) {
                highestMagnitude = p / 2 - 1;
            }
        }
        double[] frequency = new double[bufferSize - 1];
        for (int i = 0; i < (bufferSize - 1); i++) {
            frequency[i] = ((i + 1) * sampleRate / 2) / (bufferSize / 2);
        }

        double total = 0;
        double magnitudeTotal = 0;

        int start = highestMagnitude > 2 ? -2 : 0;
        int end = highestMagnitude + 2 < frequency.length ? 2 : 0;

        for (int i = start; i < end; i++) {
            total += frequency[highestMagnitude + i] * magnitude[highestMagnitude + i];
            magnitudeTotal += magnitude[highestMagnitude + i];
        }

        double averageFreq = total / magnitudeTotal;
        averageFreq = (averageFreq <= highestHumanPitch && magnitudeTotal > voiceSensitivity) ? averageFreq : 0;

        return (int) averageFreq;
    }
}
