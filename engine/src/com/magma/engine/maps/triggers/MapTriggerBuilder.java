package com.magma.engine.maps.triggers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.magma.engine.maps.triggers.stock.KillzoneTrigger;
import com.magma.engine.maps.triggers.stock.SolidTrigger;

public abstract class MapTriggerBuilder {
	
	public MapTrigger create(MapObject object) {
		// construct MapTrigger with correct shape
		Rectangle rect = null;
		if (object instanceof RectangleMapObject) {
			rect = ((RectangleMapObject) object).getRectangle();
		} else {
			Gdx.app.error("MapTriggerBuilder","MapLoader only supports RectangleMapObjects! This is a " + object.getClass().getSimpleName());
			return null;
		}
		
		// when exception: no Type method found of object?
		String type = object.getProperties().get("type").toString();
		
		// stock triggers
		switch (type) {
		case "Solid":
			return new SolidTrigger(rect, object);
        case "Killzone":
             return new KillzoneTrigger(rect,object);
		default:
			return createCustom(rect,type,object);
		}
	}
	
	/**
	 * Define custom MapTrigger types here!
	 */
	public abstract MapTrigger createCustom(Rectangle bounds, String type, MapObject object);
}
