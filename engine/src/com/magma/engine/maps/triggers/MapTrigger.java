package com.magma.engine.maps.triggers;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.magma.engine.collision.TriggerListener;
import com.magma.engine.gfx.Drawable;
import com.magma.engine.maps.MapSession;
import com.magma.engine.maps.MapStage;
import com.magma.engine.utils.MagmaMath;

public abstract class MapTrigger extends Actor implements Drawable, TriggerListener {
	
	protected final Rectangle rect;
	
	public MapTrigger(Rectangle rect, MapObject original) {
		this.rect = rect;
		
		// import the originals properties
		MagmaMath.scaleRectangle(rect, 1f/MapSession.getTileSize().x);
		setBounds(rect.x,rect.y,rect.width,rect.height);
		setColor(original.getColor());
		setVisible(original.isVisible());
		setName(original.getName());
		unpack(original.getProperties());
	}

    public MapStage getMapStage(){
        return (MapStage) getStage();
    }
	
	protected abstract void unpack(MapProperties properties);
}
