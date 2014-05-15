package dk.aau.cs.giraf.cars.framework;

import android.content.Context;
import android.media.MediaPlayer;

public class Sound {
    private int soundId;
    private MediaPlayer mediaPlayer;
    private boolean isPlayed;

    public Sound (Context context, int soundId, float volume) {
        this.soundId = soundId;
        this.mediaPlayer = MediaPlayer.create(context, soundId);
        this.mediaPlayer.setVolume(volume, volume);
        this.mediaPlayer.setLooping(false);

        this.isPlayed = false;
    }

    public void Play() {
        if (!isPlayed && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPlayed = true;
        }
    }

    public void Reset() {
        isPlayed = false;
    }

    public void PlayAndReset() {
        Play();
        Reset();
    }

    public boolean IsPlaying() { return mediaPlayer.isPlaying(); }

    public void Dispose() {
        mediaPlayer.release();
    }

}