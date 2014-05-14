package dk.aau.cs.giraf.cars.framework;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public class Audio {
    AssetManager assets;
    SoundPool soundPool;
    Context context;

    private final float DEFAULT_VOLUME = 1.0f;

    public Audio(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assets = activity.getAssets();
        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        this.context = activity;
    }

    public Music createMusic(String filename) {
        try {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            return new Music(assetDescriptor);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load music '" + filename + "'");
        }
    }

    public Sound createSound(int soundId) {
        return createSound(soundId, DEFAULT_VOLUME);
    }

    public Sound createSound(int soundId, float volume) {
        return new Sound(context, soundId, volume);
    }
}
