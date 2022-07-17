package com.magma.engine.assets;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.magma.engine.MagmaGame;

public class MagmaLoader extends AssetManager {

	private static MagmaLoader instance;
	private Skin gameSkin;
	
	private MagmaLoader() {
		gameSkin = new Skin();
		MagmaGame.disposeOnExit(this);
	}
	
	public static <T> T request(String fileName, Class<T> type, boolean engine) {
		if (!engine) {
			fileName = MagmaGame.getAssetPrefix() + fileName;
		}
		
		MagmaLoader assets = getInstance();
		T loaded = null;
		if (!assets.isLoaded(fileName)) {
			assets.load(fileName,type);
			assets.finishLoadingAsset(fileName); // hang game until loaded
		}
		loaded = assets.get(fileName); 
		return loaded;
	}
	
	public static <T> T request(String fileName, Class<T> type) {
		return request(fileName,type,false);
	}

	public static void loadAtlas(String... atlasNames) {
		for (String name : atlasNames) {
			if (!name.endsWith(".atlas")) {
				name += ".atlas";
			}
			TextureAtlas atlas = request(name, TextureAtlas.class);
			getInstance().gameSkin.addRegions(atlas);
		}
	}
	
	@Override
	public void dispose() {
		instance = null;
		super.dispose();
	}

	public static NinePatch getNinePatch(String name) {
		return getInstance().gameSkin.getPatch(name);
	}

	public static AtlasRegion getAtlasRegion(String name) {
		Array<AtlasRegion> regions = getAtlasRegions(name);
		if (regions == null || regions.isEmpty()) {
			Gdx.app.error("AssetLoader", "Atlas region of name " + name + " not found! (Have you loaded the atlas?)");
			return null;
		}
		return regions.first();
	}

	// TODO might be slow, implement caching
	public static Array<AtlasRegion> getAtlasRegions(String name){
		Array<TextureAtlas> atlasses = new Array<TextureAtlas>();
		getInstance().getAll(TextureAtlas.class, atlasses);
		for (TextureAtlas atlas : atlasses.iterator()) {
			Array<AtlasRegion> regions = atlas.findRegions(name);
			if (regions != null) {
				if (regions.isEmpty()) {
					throw new IllegalArgumentException("No atlas region of name " + name + " found!");
				}
				return regions;
			}
		}
		String message = "Atlas regions of name " + name + " not found! (Have you loaded the atlas?)";
		message += "\nChoose between: ";
		for (AtlasRegion region : getAtlasRegions()) {
			message += region.name + " ";
		}
		Gdx.app.error("AssetLoader", message);
		return null;
	}

	public static Array<AtlasRegion> getAtlasRegions(){
		Array<TextureAtlas> atlasses = new Array<TextureAtlas>();
		Array<AtlasRegion> regions = new Array<AtlasRegion>();
		getInstance().getAll(TextureAtlas.class, atlasses);
		for (TextureAtlas atlas : atlasses.iterator()) {
			regions.addAll(atlas.getRegions());
		}
		return regions;
	}

    public static ArrayList<String> getAllMapNames(){
        ArrayList<String> list = new ArrayList<String>();

        FileHandle mapFolder = Gdx.files.internal(MagmaGame.getAssetPrefix()+"/maps");
        if (!mapFolder.exists()){
                Gdx.app.log("AssetLoader", "Could not find the map folder.");
                return list;
        }

        FileHandle[] items = mapFolder.list();
        for (FileHandle item : items) {
                if (item.extension().equalsIgnoreCase("tmx")){
                        list.add(item.nameWithoutExtension());
                }
        }

        if (items.length == 0){
                Gdx.app.log("AssetLoader", "No maps found in map folder.");
        }

        return list;
    }
    
	public static Texture getPlaceholderTexture() {
		return request("404.png", Texture.class, true);
	}

	public static Skin getSkin() {
		return getInstance().gameSkin;
	}

	public static Skin getDebugSkin() {
		return request("skins/default/skin/uiskin.json",Skin.class,true);
	}

	public static MagmaLoader getInstance() {
		if (instance == null) {
			instance = new MagmaLoader();
		}
		return instance;
	}

}
