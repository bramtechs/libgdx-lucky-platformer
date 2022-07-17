package com.magma.engine.maps;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.magma.engine.debug.MagmaLogger;
import com.magma.engine.debug.modules.MapModuleListener;
import com.magma.engine.maps.triggers.MapTrigger;
import com.magma.engine.maps.triggers.MapTriggerBuilder;
import com.magma.engine.stages.GameStage;
import com.magma.engine.stages.StageSwitchListener;
import com.magma.engine.stages.ViewportContext;
import com.magma.engine.ui.dialog.Dialog;

public class MapStage extends GameStage implements StageSwitchListener, MapModuleListener {

    private static MapStage instance;

    private final MapTriggerBuilder triggerBuilder;

    private MapSession session;

    public MapStage(ViewportContext viewports, SpriteBatch batch, MapTriggerBuilder triggerBuilder) {
        super(viewports, batch);
        MapStage.instance = this;
        this.triggerBuilder = triggerBuilder;

        // TODO: add dialog Stack
        Dialog dialog = new Dialog(viewports.getUI(), 300, 120);
        ui.addActor(dialog);
    }

    public MapStage(ViewportContext viewports, SpriteBatch batch) {
        super(viewports, batch);
        this.triggerBuilder = new MapTriggerBuilder() {
            @Override
            public MapTrigger createCustom(Rectangle bounds, String type, MapObject object) {
                return null;
            }
        };
    }

    public void openSession(MapSession session) {
        // close session if already open
        if (this.session != null) {
            closeSession();
        }

        MagmaLogger.log(this, "Starting session...");
        this.session = session;
        this.session.spawnTriggers(triggerBuilder);
        this.session.toFront();
        addActor(this.session);
    }

    public void closeSession() {
        if (session != null) {
            MagmaLogger.log(this, "Closing session...");
            session.toBack();
            session.remove();
            session.dispose();
            session = null;
        }
    }

    @Override
    public void dispose() {
        if (session != null) {
            session.dispose();
            session = null;
        }
        super.dispose();
    }

    @Override
    public void stageOpened(GameStage stage) {
    }

    @Override
    public void stageClosed(GameStage stage) {
        if (stage == this) {
            closeSession();
        }
    }

    @Override
    public void unloadMap() {
        if (session == null) {
            MagmaLogger.log("Map unloading not necessary");
            return;
        }
        session.dispose();
        session = null;
        MagmaLogger.log(this, "Unloading map...");
    }

    @Override
    public void setTriggersVisible(boolean on) {
        session.setDebug(on, true);
    }

    @Override
    public void requestMap(String name) {
        // You should override this
    }


    @Override
    public String toString() {
        if (session != null) {
            String text = "tmx map: " + session.getTmxName() + "\n";
            text += "tileSize: " + MapSession.getTileSize() + "\nspawn: ";
            return text;
        }
        return " dormant";
    }

    public MapSession getSession() {
        return session;
    }

    public static MapStage getInstance() {
        if (instance == null) {
            throw new NullPointerException("MapStage not yet created!");
        }
        return instance;
    }
}
