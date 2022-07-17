package com.magma.engine.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;
import com.magma.engine.assets.MapLoader;
import com.magma.engine.collision.MapCollisions;
import com.magma.engine.maps.triggers.MapTriggerBuilder;
import com.magma.engine.maps.triggers.MapTriggers;

public class MapSession extends Group implements Disposable {

	private static MapSession session;

	private final OrthogonalTiledMapRenderer renderer;
	private final TiledMap map;

	private MapCollisions collisions;
	private MapTriggers triggers;
	private final String tmxName;
	private final MapStage stage;

	public MapSession(MapStage stage, String tmxName) {
		this.stage = stage;
		this.tmxName = tmxName;
		MapSession.session = this;

		Gdx.app.log("MapStage", "Opening map...");
		map = MapLoader.loadTilemap(tmxName);

		renderer = new OrthogonalTiledMapRenderer(map, 1f / getTileSize().x);
	}

	public void spawnTriggers(MapTriggerBuilder triggerBuilder) {
		// extract layers
		MapLayers layers = map.getLayers();

		Object layer = layers.get("Triggers");
		if (layer != null) {
			MapLayer triggerLayer = (MapLayer) layer;

			collisions = new MapCollisions(triggerLayer);

			MapObjects objects = triggerLayer.getObjects();
			triggers = new MapTriggers(stage, objects, triggerBuilder);
			addActor(triggers);

		} else {
			Gdx.app.log("MapStage", "No _triggers layer found!");
			collisions = new MapCollisions();
			triggers = null;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (renderer != null) {
			renderer.setView(stage.getOrthoCamera());
			renderer.render();
		}
		super.draw(batch, parentAlpha);
	}

	public static GridPoint2 getMapSize() {
		GridPoint2 mapSize = new GridPoint2();
		if (getSession() == null) {
			throw new NullPointerException("No map session active!");
		}

		MapProperties props = session.map.getProperties();
		if (props.containsKey("width")) {
			mapSize.x = (int) props.get("width");
		} else {
			Gdx.app.log("MapStage", "Could not detect tile width from map, defaulting to 16");
			mapSize.x = 16;
		}
		if (props.containsKey("height")) {
			mapSize.y = (int) props.get("height");
		} else {
			Gdx.app.log("MapStage", "Could not detect tile width from map, defaulting to 16");
			mapSize.y = 16;
		}

		return mapSize;
	}

	@Override
	public void addActor(Actor actor) {
		stage.registerActor(actor);
		super.addActor(actor);
	}
	
	@Override
	public void dispose() {
        triggers.remove();
		renderer.dispose();
	}

	public static MapSession getSession() {
		return session;
	}

	public static GridPoint2 getTileSize() {
		GridPoint2 tileSize = new GridPoint2(16, 16);
		if (getSession() == null) {
			return tileSize;
		}
		MapProperties props = getSession().map.getProperties();
		if (props.containsKey("tilewidth")) {
			tileSize.x = (int) props.get("tilewidth");
		} else {
			Gdx.app.log("MapStage", "Could not detect tile width from map, defaulting to 16");
			tileSize.x = 16;
		}
		if (props.containsKey("tileheight")) {
			tileSize.y = (int) props.get("tileheight");
		} else {
			Gdx.app.log("MapStage", "Could not detect tile height from map, defaulting to 16");
			tileSize.y = 16;
		}
		return tileSize;
	}

    public OrthogonalTiledMapRenderer getRenderer() {
        return renderer;
    }

    public TiledMap getMap() {
        return map;
    }

    public MapCollisions getCollisions() {
        return collisions;
    }

    public MapTriggers getTriggers() {
        return triggers;
    }

    public String getTmxName() {
        return tmxName;
    }

    public MapStage getStage() {
        return stage;
    }
}
