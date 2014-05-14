package dk.aau.cs.giraf.cars.framework;

import android.content.Context;
import android.media.MediaPlayer;

public class Sound {
    int soundId;
    MediaPlayer mediaPlayer;

    public Sound (Context context, int soundId, float volume) {
        this.soundId = soundId;
        this.mediaPlayer = MediaPlayer.create(context, soundId);
        this.mediaPlayer.setVolume(volume, volume);
        this.mediaPlayer.setLooping(false);
    }

    public void Play() {
        mediaPlayer.start();
    }

    public boolean IsPlaying() { return mediaPlayer.isPlaying(); }

    public void Reset() { mediaPlayer.reset(); }

    public void Dispose() {
        mediaPlayer.release();
    }

}