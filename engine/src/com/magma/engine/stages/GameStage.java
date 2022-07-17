package com.magma.engine.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.magma.engine.collision.CollisionStage;
import com.magma.engine.gfx.camera.CameraBehavior;
import com.magma.engine.gfx.camera.CameraPanBehavior;

// This abstract type of stage is linked to another stage containing the UI
public abstract class GameStage extends CollisionStage {
	
	protected final Camera camera;
	protected final Stage ui;
    protected final ViewportContext viewports;

	private CameraBehavior cameraBehavior;
	private CameraBehavior cameraBehaviorUI;
	
	public GameStage(ViewportContext viewports, SpriteBatch batch) {
		super(viewports.get(),batch);
		this.camera = getCamera();
        this.viewports = viewports;
		this.ui = new Stage(viewports.getUI(),batch);
		//setCameraBehaviorUI(new CameraPanBehavior());
		//setDebugUnderMouse(true);
	}
	
	public void setCameraBehavior(CameraBehavior behavior) {
		if (cameraBehavior != null) {
			cameraBehavior.remove();
		}
		cameraBehavior = behavior;
		cameraBehavior.addSource(this);
		addActor(cameraBehavior);
	}
	
	public void setCameraBehaviorUI(CameraBehavior behavior) {
		if (cameraBehaviorUI != null) {
			cameraBehaviorUI.remove();
		}
		cameraBehaviorUI = behavior;
		cameraBehaviorUI.addSource(ui);
		ui.addActor(cameraBehaviorUI);
	}
	
	public OrthographicCamera getOrthoCamera() {
		if (!(camera instanceof OrthographicCamera)) {
			throw new IllegalStateException("Camera is not orthographic!" + camera.getClass().getSimpleName());
		}
		return (OrthographicCamera) camera;
	}
	
	public PerspectiveCamera getPersCamera() {
		if (!(camera instanceof PerspectiveCamera)) {
			throw new IllegalStateException("Camera is not perspective! It is " + camera.getClass().getSimpleName());
		}
		return (PerspectiveCamera) camera;
	}
	
	@Override
	public void act(float delta) {
        if (Gdx.input.isKeyJustPressed(Keys.F7)){
            setCameraBehaviorUI(new CameraPanBehavior());
        }
        if (Gdx.input.isKeyJustPressed(Keys.F8)){
            setCameraBehavior(new CameraPanBehavior());
        }
		super.act(delta);
		ui.act(delta);
	}
	
	@Override
	public void draw() {
		super.draw();
		ui.getViewport().apply();
		ui.draw();
	}
	
	@Override
	public void dispose() {
		ui.dispose();
		super.dispose();
	}

    public ViewportContext getViewports(){
        return viewports;
    }
	
	public Stage getUIStage() {
		return ui;
	}
}
