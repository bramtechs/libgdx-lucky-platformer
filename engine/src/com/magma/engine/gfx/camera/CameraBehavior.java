package com.magma.engine.gfx.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class CameraBehavior extends Actor {

	protected Stage stage;
    protected OrthographicCamera cam;
	
	public void addSource(Stage stage) {
		this.stage = stage;
        this.cam = (OrthographicCamera) stage.getCamera();
		if (cam != null) {
			Gdx.app.log("Camera is at ", cam.position + " with zoom " + cam.zoom);
		}
	}
	
	public abstract void process(OrthographicCamera camera, float delta);
	
	@Override
	public void act(float delta) {
		if (cam != null) {
			process(cam,delta);
		}
		super.act(delta);
	}
}
