package com.magma.engine.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.magma.engine.MagmaGame;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Shapes extends ShapeDrawer implements Disposable {

	private static ShapeDrawer instance;
	private SpriteBatch batch;
	
	private Shapes(SpriteBatch batch,TextureRegion pixel) {
		super(batch,pixel);
		MagmaGame.disposeOnExit(this);
	}
	
	@Override
	public void dispose() {
        instance = null;
		getRegion().getTexture().dispose();
	}
	
	public static ShapeDrawer getInstance() {
		if (MagmaGame.getSpriteBatch() == null) {
			throw new IllegalArgumentException("Spritebatch not yet initialized!");
		}
		if (instance == null) {
			TextureRegion pixel = new TextureRegion(new Texture("pixel.png"));
			SpriteBatch batch = MagmaGame.getSpriteBatch();
			instance = new Shapes(batch,pixel);
		}
		return instance;
	}
}
