package com.magma.gmtk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.magma.engine.assets.MagmaLoader;

public class FortuneWheel extends VerticalGroup {

    private static Array<FortuneEvent> events;

    private Array<Label> labels;
    private int tick;
    private float delay;
    private float timer;
    private boolean isAborted;

    public FortuneWheel() {
        if (events == null) {
            events = new Array<FortuneEvent>();
            FortuneEvent.addDefaults(events);
        }
        labels = new Array<Label>();
        setPosition(0, 0);
        setSize(640, 480);
        align(Align.center);

        // roll the dice
        timer = MathUtils.random(0.5f, 0.9f);
        tick = MathUtils.random(0,10);

        // generate labels
        for (FortuneEvent event : events) {
            String text = "????";
            if (event.isDiscovered()) {
                text = event.getName();
            }
            Label lbl = new Label(text, MagmaLoader.getDebugSkin());
            labels.add(lbl);
            addActor(lbl);
        }
    }

    public void reset() {
        for (Label lbl : labels) {
            lbl.setColor(Color.WHITE);
        }
    }

    @Override
    public void act(float delta) {
        if (delay > 0.1f) {
            delay = 0f;
            if (timer > 0f) {
                reset();
                int i = tick % events.size;
                Label label = labels.get(i);
                label.setColor(Color.GREEN);
                tick++;
                PlatformerAudio.click.play(1f, MathUtils.random(0.9f, 1.1f), 0f);
            } else {
                int j = (tick - 1) % events.size;
                Label label = labels.get(j);
                FortuneEvent event = events.get(j);
                event.discover();
                event.apply();
                label.setText(event.getName());
                delay = -100f;
            }
        }
        toFront();
        delay += delta;
        timer -= delta; 
        if (timer < -1f) {
            remove();
        }
        super.act(delta);
    }
}
