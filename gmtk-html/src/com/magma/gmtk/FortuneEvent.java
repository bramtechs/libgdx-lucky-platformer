package com.magma.gmtk;

import com.badlogic.gdx.utils.Array;
import com.magma.engine.debug.MagmaLogger;

public abstract class FortuneEvent {

    private boolean isDiscovered;
    private String name;

    public FortuneEvent(String name) {
        this.name = name;
    }

    public void apply() {
        discover();
        applyEvent();
    }

    protected abstract void applyEvent();

    public String getName() {
        return name;
    }

    public boolean isDiscovered() {
        return isDiscovered;
    }

    public void discover() {
        isDiscovered = true;
    }

    public static void addDefaults(Array<FortuneEvent> events) {
        events.add(new FortuneEvent("Flipped Gravity") {
            @Override
            public void applyEvent() {
                PlatformerPlayer.gravity = -10;
                MagmaLogger.log(this,"flip grav");
            }
        });
        events.add(new FortuneEvent("Background Palette Swap") {
            @Override
            public void applyEvent() {
                PlatformerStage.setRandomPalette();
            }
        });
        events.add(new FortuneEvent("Explode!") {
            @Override
            public void applyEvent() {
                PlatformerPlayer.explode();
            }
        });
        events.add(new FortuneEvent("Honk!") {
            @Override
            public void applyEvent() {
                PlatformerAudio.honk.play();
            }
        });
        events.add(new FortuneEvent("Upside Down") {
            @Override
            public void applyEvent() {
                PlatformerStage.flipLevel();
            }
        });
        events.add(new FortuneEvent("No Gravity") {
            @Override
            public void applyEvent() {
                PlatformerPlayer.gravity = 0;
            }
        });
        events.add(new FortuneEvent("Melt ice") {
            @Override
            public void applyEvent() {
                PlatformerStage.melt();
            }
        });
        events.add(new FortuneEvent("Support") {
            @Override
            public void applyEvent() {
                PlatformerStage.support();
            }
        });
        events.add(new FortuneEvent("Disarm") {
            @Override
            public void applyEvent() {
                PlatformerStage.disarm();
            }
        });
    }
}
