package com.magma.engine.stages;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.magma.engine.MagmaGame;
import com.magma.engine.debug.modules.StageModuleListener;

//TODO: Add stage transitions
public class StageSwitcher implements Screen, Disposable, StageModuleListener {

    private static Array<StageSwitchListener> listeners;
	private static StageSwitcher instance;
    private static GameStage active;

    private StageSwitcher() {
        listeners = new Array<StageSwitchListener>();
        MagmaGame.disposeOnExit(this);
    }

    public static StageSwitcher getInstance() {
        if (instance == null){
            instance = new StageSwitcher();
        }
        return instance;
    }

    public static void open(GameStage stage) {
        StageSwitcher.active = stage;

        // make the stage a listener if it has the correct interface
        if (stage instanceof StageSwitchListener) {
            listeners.add((StageSwitchListener) stage);
        }

        // notify all the listeners
        for (StageSwitchListener s : listeners) {
            s.stageOpened(stage);
        }
    }

    public static void addListener(StageSwitchListener listener) {
        listeners.add(listener);
    }

    public static void act(float delta) {
        active.act(delta);
    }

    @Override
    public void render(float delta) {
        if (active != null) {
            active.getViewport().apply();
            active.draw();
        }
    }

    @Override
    public void dispose() {
        if (active != null) {
            active.dispose();
        }
        listeners = null;
        instance = null;
    }

    @Override
    public void resize(int width, int height) {
        if (active != null) {
            active.getViewports().resize(width, height);
            Stage ui = active.getUIStage();
            ui.getViewport().update(width, height);
            ui.getCamera().update(true);
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void unloadStage() {

    }
}
