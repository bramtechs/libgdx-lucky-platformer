package com.magma.gmtk;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.magma.engine.debug.MagmaLogger;

public class PlatformerAudio {

    private static PlatformerAudio instance;

    public static Sound explosion;
    public static Sound jump;
    public static Sound laserShoot;
    public static Sound pickup;
    public static Sound click;
    public static Sound synth;
    public static Sound laserShoot2;
    public static Sound powerup;
    public static Sound honk;

    private static Music curMusic;
    private static boolean isMuted;

    public PlatformerAudio() {
        explosion = Gdx.audio.newSound(Gdx.files.internal("gmtk/explosion.wav"));
        jump = Gdx.audio.newSound(Gdx.files.internal("gmtk/jump.wav"));
        pickup = Gdx.audio.newSound(Gdx.files.internal("gmtk/pickupCoin.wav"));
        click = Gdx.audio.newSound(Gdx.files.internal("gmtk/click.wav"));
        honk = Gdx.audio.newSound(Gdx.files.internal("gmtk/honk.wav"));
        powerup = Gdx.audio.newSound(Gdx.files.internal("gmtk/powerUp.wav"));
    }

    public static void stopMusic() {
        if (curMusic != null) {
            curMusic.stop();
            curMusic.dispose();
            curMusic = null;
        }
    }

    public static void playLevelMusic() {
        stopMusic();
        if (!isMuted) {
            curMusic = Gdx.audio.newMusic(Gdx.files.internal("gmtk/mus/nightshade.ogg"));
            curMusic.play();
            curMusic.setLooping(true);
        }
    }

    public static void playBossMusic() {
        stopMusic();
        if (!isMuted) {
            curMusic = Gdx.audio.newMusic(Gdx.files.internal("gmtk/mus/breakbeat.ogg"));
            curMusic.setPosition(110);
            curMusic.play();
            curMusic.setLooping(true);
        }
    }

    public static void mute() {
        isMuted = true;
        stopMusic();
    }

    public static void load() {
        instance = new PlatformerAudio();
    }
}
