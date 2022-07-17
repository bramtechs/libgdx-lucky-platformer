package com.magma.engine.input;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.utils.Json;
import com.magma.engine.saving.SaveFile;

public class GameInput {

    private static final String FILE_NAME = "keybindings.json";
	private static GameInput instance;
	private final HashMap<String, Integer> keys;
	
	public GameInput() {
		this.keys = new HashMap<String,Integer>(); 

		// get rid of the cursor, kinda complicated
		Pixmap nothing = new Pixmap(4,4,Format.RGBA8888);
		nothing.setColor(Color.WHITE);
		nothing.drawRectangle(0, 0, 4, 4);
		Cursor cursor = Gdx.graphics.newCursor(nothing, 0, 0);
		Gdx.graphics.setCursor(cursor);
		nothing.dispose();
	}

    // TODO: not cross platform, write a GameInputDesktop class
    @SuppressWarnings("unchecked")
	private HashMap<String,Integer> loadKeys(){
		FileHandle jsonFile = requestFileHandle(); 
		HashMap<String, Integer> keys = null;
		// upload keys from config into hashmap
		if (jsonFile.exists()) {
			try {
				Json json = new Json();
				keys = (HashMap<String,Integer>) json.fromJson(HashMap.class, jsonFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (keys == null) {
			Gdx.app.log("GameInput", "No keybindings saved yet...");
			keys = new HashMap<String, Integer>();
		}
		return keys;
	}

    private static FileHandle requestFileHandle(){
		return SaveFile.getPersistentPath().child(FILE_NAME);
    }
	
    // TODO: not cross platform, write a GameInputDesktop class
	public static void flush() {
        if (true) return;
		GameInput ins = getInstance();
		String json = new Json().prettyPrint(ins.keys);
        FileHandle jsonFile = requestFileHandle();
		jsonFile.writeString(json, false);
		Gdx.app.log("GameInput", "Keyboard bindings updated");
	}

	public static int registerKey(String name, int defaultKey) {
		GameInput ins = getInstance();
		if (!ins.keys.containsKey(name)) {
			Gdx.app.log("GameInput", "Registered key " + defaultKey + " with name " + name);
			ins.keys.put(name, defaultKey);
			flush();
		}
		return ins.keys.get(name);
	}
	
    // TODO: not cross platform, write a GameInputDesktop class
	public static void reset() {
        if (true) return;
		Gdx.app.log("GameInput", "Wiped keybindings");
        FileHandle handle = requestFileHandle();
		handle.delete();
		instance = null;
	}
	
	public static GameInput getInstance() {
		if (instance == null) {
			return new GameInput();
		}
		return instance; 
	}
}
