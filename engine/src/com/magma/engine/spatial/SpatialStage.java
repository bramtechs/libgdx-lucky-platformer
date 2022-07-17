package com.magma.engine.spatial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.magma.engine.stages.GameStage;
import com.magma.engine.stages.ViewportContext;

public class SpatialStage extends GameStage {
	
	protected final ModelBatch modelBatch;
	protected final Viewport viewport;
	protected final SpriteBatch batch;
	protected final ModelBuilder modelBuilder;
	protected final Camera camera;
	protected final Environment env;
	
	private ModelInstance origin;
	private Model boxModel;
	
	private FirstPersonCameraController fps;
	
	public SpatialStage(ViewportContext viewports, SpriteBatch batch, ModelBatch modelBatch, ModelBuilder builder) {
		super(viewports,batch);
		this.modelBatch = modelBatch;
		this.modelBuilder = builder;
		this.batch = batch;
		this.viewport = viewports.get();
		this.camera = viewports.getCamera();
		if (this.camera == null) {
			throw new NullPointerException("No camera!");
		}else if (this.camera instanceof PerspectiveCamera){
			camera.far = 1000;
			camera.near = 0.01f;
			camera.position.set(15, 15, 15);
			camera.lookAt(0, 0, 0);
		}
		
		// generate world center
		Material mat = new Material(ColorAttribute.createDiffuse(Color.RED));
		boxModel = modelBuilder.createBox(0.5f,0.5f, 0.5f, mat, Usage.Position | Usage.Normal);
		origin = new ModelInstance(boxModel);
		
		// lighting
		env = new Environment();
		env.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		env.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -0.2f, -0.8f, 0.7f));
		
		fps = new FirstPersonCameraController(getPersCamera());
		fps.setVelocity(10f);
		fps.setDegreesPerPixel(2f);
		Timer.schedule(new Task() {
			@Override
			public void run() {
				Gdx.app.log("Camera",""+camera.position);
			}
		}, 0f,2f);
		
		Gdx.input.setInputProcessor(fps);
	}
	
	@Override
	public void act(float delta) {
		fps.update(delta);
		super.act(delta);
	}
	
	@Override
	public void dispose() {
		boxModel.dispose();
		super.dispose();
	}
	
	protected void draw3d() {
	}
	
	@Override
	public void draw() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		modelBatch.begin(camera);
		modelBatch.render(origin,env);
		draw3d();
		modelBatch.end();
		super.draw();
	}
}
