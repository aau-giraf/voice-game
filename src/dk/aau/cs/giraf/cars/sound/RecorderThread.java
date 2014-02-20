package dk.aau.cs.giraf.cars.sound;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import dk.aau.cs.giraf.cars.gamecode.GameInfo;


public class RecorderThread extends Thread {
    public boolean recording;  //variable to start or stop recording
    public int highestHumanPitch = 3400; //Determine the highest frequency a human can make to get rid of false data
    public int voiceSensitivity = 10000;  //Determine the "volume" that that has to be recorded before the input data is valid
    private int currentFrequency = -1;

    public RecorderThread() {

    }

    @Override
    public void run() {
        System.out.println("recorderThread started");
        AudioRecord recorder;
        short[] audioData;
        int bufferSize;
        int sampleRate = 44100;

        bufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT) * 2; //get the buffer size to use with this audio record

        recorder = new AudioRecord(AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize); //instantiate the AudioRecorder


        recording = true; //variable to use start or stop recording
        audioData = new short[bufferSize]; //short array that pcm data is put into.

        while (recording) {  //loop while recording is needed
            if (recorder.getRecordingState() == android.media.AudioRecord.RECORDSTATE_STOPPED) {
                recorder.startRecording();  //check to see if the Recorder has stopped or is not recording, and make it record.
            } else {
                recorder.read(audioData, 0, bufferSize); //read the PCM audio data into the audioData array
                double[] endAudioData = new double[bufferSize * 2];
                //Now we need to decode the PCM data using the Zero Crossings Method
                //FFT fft1 = new FFT();
                short[] imgAudioData = new short[bufferSize];
                endAudioData = FFT.fft(audioData, imgAudioData, true);

                double[] magnitude = new double[bufferSize - 1];
                int highestMagnitude = 0;
                for (int p = 2; p < (bufferSize - 1) * 2; p += 2) {
                    magnitude[p / 2 - 1] = Math.sqrt(endAudioData[p] * endAudioData[p] + endAudioData[p + 1] * endAudioData[p + 1]);
                    //System.out.println("magnitude = " + magnitude[p/2-1]);
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
                if (averageFreq <= highestHumanPitch && magnitudeTotal > voiceSensitivity) {
                    currentFrequency = (int) averageFreq;
                    //System.out.println("average frequency = " + (int)averageFreq + " magnitude total = " + (int)magnitudeTotal);
                } else {
                    currentFrequency = 0;
                }


                GameInfo.setCurrFreq(currentFrequency);

            }//else recorder started

        } //while recording

        if (recorder.getState() == android.media.AudioRecord.RECORDSTATE_RECORDING)
            recorder.stop(); //stop the recorder before ending the thread
        recorder.release(); //release the recorders resources
        recorder = null; //set the recorder to be garbage collected.

    }//run

}//recorderThread
