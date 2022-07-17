package com.magma.engine.stages;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ViewportContext {
    private Viewport view;
    private Viewport uiView;

    private ViewportContext() {
    }

    /**
     * Creates two viewports of game and ui where the game consists of tiles on a
     * grid and the ui has a fixed resolution, aka a 'retro' game.
     */
    public static ViewportContext createRetro(int gameWidth, int gameHeight, int uiWidth, int uiHeight) {
        ViewportContext c = new ViewportContext();
        c.view = new FitViewport(gameWidth, gameHeight);
        c.uiView = new FitViewport(uiWidth, uiHeight);
        return c;
    }

    /**
     * Creates two viewports of the game and ui that match the window size,
     * defintion of a 'modern' game.
     */
    public static ViewportContext createModern(Camera camera) {
        ViewportContext c = new ViewportContext();
        c.view = new ScreenViewport(camera);
        c.uiView = new ScreenViewport();
        c.view.setCamera(camera);
        return c;
    }

    /**
     * Creates an context storing two viewports of the game and ui.
     */
    public static ViewportContext createCustom(Viewport view, Viewport uiView) {
        ViewportContext c = new ViewportContext();
        c.view = view;
        c.uiView = uiView;
        return c;
    }

    public void resize(int width, int height) {
        view.update(width, height);
        uiView.update(width, height);
    }

    public Viewport get() {
        return view;
    }

    public Viewport getUI() {
        return uiView;
    }

    public float getWidth() {
        return view.getWorldWidth();
    }

    public float getHeight() {
        return view.getWorldHeight();
    }

    public float getUIWidth() {
        return uiView.getWorldHeight();
    }

    public float getUIHeight() {
        return uiView.getWorldHeight();
    }

    public Vector2 getCenter() {
        return new Vector2(getWidth() * 0.5f, getHeight() * 0.5f);
    }

    public Vector2 getUICenter() {
        return new Vector2(getUIWidth() * 0.5f, getUIHeight() * 0.5f);
    }

    public Camera getCamera() {
        return view.getCamera();
    }

    public Camera getUICamera() {
        return uiView.getCamera();
    }

    public OrthographicCamera getOrthoCamera() {
        return (OrthographicCamera) view.getCamera();
    }

    public OrthographicCamera getUIOrthoCamera() {
        return (OrthographicCamera) uiView.getCamera();
    }
}
