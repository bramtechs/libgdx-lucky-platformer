package com.magma.engine.gfx.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraFollowBehavior extends CameraBehavior {

	private Actor target;
	
	public CameraFollowBehavior(Actor target) {
		this.target = target;
	}
	
	@Override
	public void process(OrthographicCamera camera, float delta) {
		if (target != null) {
			Viewport view = stage.getViewport();
			
			 // why divide by exactly 4, I don't know, it centers the camera
			camera.position.x = target.getX()+target.getWidth()*0.5f* target.getScaleX();
			camera.position.y = target.getY()+target.getHeight()*0.5f* target.getScaleY();
		}
	}
}
