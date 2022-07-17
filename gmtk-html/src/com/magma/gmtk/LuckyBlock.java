package com.magma.gmtk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.magma.engine.debug.MagmaLogger;

public class LuckyBlock extends PlatformerTile {

    private float r;
    private float g;
    private float b;

    public LuckyBlock(int x, int y) {
        super(x, y);
    }

    @Override
    public void setRectColor() {
        float speed = 10f;
        r += Gdx.graphics.getDeltaTime() * MathUtils.random();
        b -= Gdx.graphics.getDeltaTime() * MathUtils.random();
        g += Gdx.graphics.getDeltaTime() * MathUtils.random();

        float rr = MathUtils.sin(r * speed) * 0.5f + 0.5f;
        float gg = MathUtils.sin(g * speed) * 0.5f + 0.5f;
        float bb = MathUtils.sin(b * speed) * 0.5f + 0.5f;
        Color c = new Color(rr, gg, bb, 1);
        shapes.setColor(c);
    }

    @Override
    public void onHitByPlayer(PlatformerPlayer player) {
        remove();
        stage.deleteTile(this);
        stage.spawnFortuneWheel();
    }

}
