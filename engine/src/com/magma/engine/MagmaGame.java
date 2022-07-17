package com.magma.engine;

import java.io.File;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.magma.engine.debug.MagmaLogger;
import com.magma.engine.saving.SaveFile;
import com.magma.engine.stages.StageSwitcher;

public abstract class MagmaGame extends Game {

    protected static final boolean DEBUG = true;

	private static String assetFolder;
	private static Array<Disposable> disposables;
    private static Color bgColor;
	
	protected static SpriteBatch batch;
	protected ModelBatch modelBatch;

	protected StageSwitcher stageSwitcher;

	protected MagmaGame(String assetFolder) {
		if (!assetFolder.endsWith("/")) {
			assetFolder += "/";
		}
		MagmaGame.assetFolder = assetFolder;
	}
	
	protected MagmaGame() {
		MagmaGame.assetFolder = "";
        bgColor = Color.BLACK;
	}

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_INFO);
		
		disposables = new Array<Disposable>();
		batch = new SpriteBatch();
		
		modelBatch = new ModelBatch();

		MagmaLogger.log(this, "Executing at path " + new File("").getAbsolutePath());
		MagmaLogger.log(this, "Save files will be saved in " + SaveFile.getPersistentPath());

		stageSwitcher = StageSwitcher.getInstance();		
		setScreen(stageSwitcher);

		initStages();
	}

	protected abstract void initStages();

	@Override
	public void render() {
        // update all the things
        float delta = Gdx.graphics.getDeltaTime();
		StageSwitcher.act(delta);

        // render all the things
		ScreenUtils.clear(bgColor.r,bgColor.g,bgColor.b,bgColor.a);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		for (Disposable disp : disposables) {
			MagmaLogger.log(this, "Disposing " + disp.getClass().getSimpleName() +" ...");
			disp.dispose();
		}
		batch.dispose();
		modelBatch.dispose();
	}
	
	public static void disposeOnExit(Disposable disp) {
		if (!disposables.contains(disp, false)) {
			disposables.add(disp);
		}else {
			MagmaLogger.log(disp, "Already marked for disposing " + disp.getClass().getSimpleName());
		}
	}
	
	public static String getAssetPrefix() {
		return assetFolder;
	}
	
	public static SpriteBatch getSpriteBatch() {
		return batch;
	}

    public static void setBackgroundColor(Color bgColor) {
        MagmaGame.bgColor = bgColor;
    }

}
