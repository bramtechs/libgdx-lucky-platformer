package com.magma.engine.gfx.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraPanBehavior extends CameraBehavior {

	private float speed;
	
	public CameraPanBehavior(float speed) {
		this.speed = speed;
	}
	
	public CameraPanBehavior() {
		this.speed = 100;
	}
	
	@Override
	public void process(OrthographicCamera camera, float delta) {
		float s = speed*delta* (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)?5f:1f);
		if (Gdx.input.isKeyPressed(Keys.A)) {
			camera.translate(-s, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			camera.translate(s, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.W)) {
			camera.translate(0, s, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			camera.translate(0, -s, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.PAGE_UP)) {
			camera.zoom -= delta;
		}
		if (Gdx.input.isKeyPressed(Keys.PAGE_DOWN)) {
			camera.zoom += delta;
		}
		if (Gdx.input.isKeyJustPressed(Keys.HOME)) {
			camera.zoom = 1;
		}
	}

}
