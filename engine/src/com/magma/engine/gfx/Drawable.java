package com.magma.engine.gfx;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface Drawable {
	public abstract void act(float delta);
	public abstract void draw(Batch batch, float parentAlpha);
}
