package com.magma.engine.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.magma.engine.MagmaGame;
import com.magma.engine.maps.MapStage;

public class MapLoader {
	
	private static Array<MapStage> maps;
		
	public static TiledMap loadTilemap(String mapName) {
		// autofill mapPath handle
		mapName = MagmaGame.getAssetPrefix() + "maps/" + mapName + ".tmx";
		Gdx.app.log("MapStage", "Loading map " + mapName);
		
		// check if file exists
        if (!Gdx.files.internal(mapName).exists()) {
			throw new IllegalArgumentException("Map " + mapName + " does not exist!");
		}
		
		try {
			TiledMap map = new TmxMapLoader().load(mapName);
			return map;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static MapStage getRegisteredMap(int id) {
		if (id > maps.size || id < 0) {
			throw new IllegalArgumentException("No registered map with id " + id + " (total maps " + maps.size + ")");
		}
		return maps.get(id-1);
	}
}
