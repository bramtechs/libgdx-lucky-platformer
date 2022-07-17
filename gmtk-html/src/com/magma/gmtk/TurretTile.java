package com.magma.gmtk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.magma.engine.chars.CharacterController.Direction;

public class TurretTile extends PlatformerTile {

    private final float INTERVAL = 0.67f;
    private float timer;
    private boolean isDisarmed;
    private Direction dir;

    public TurretTile(int x, int y, Direction dir) {
        super(x, y);
        this.dir = dir;
        timer = MathUtils.random(-0.5f, 0.5f);
    }

    @Override
    public void setRectColor() {
        shapes.setColor(Color.DARK_GRAY.sub(0.1f, 0.1f, 0.1f, 0f));
    }

    public void dismantle() {
        isDisarmed = true;
    }

    @Override
    public void act(float delta) {
        if (!isDisarmed) {
            timer += delta;
            if (timer > INTERVAL) {
                timer = 0f;
                getStage().addActor(new TurretProjectile(getX(), getY(), dir));
            }
        }
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
