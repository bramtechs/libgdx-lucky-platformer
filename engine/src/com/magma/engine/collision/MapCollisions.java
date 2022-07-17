package com.magma.engine.collision;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MapCollisions extends Actor {
    private final MapLayer layer;

    public MapCollisions() {
    	layer = null;
    }
    
    public MapCollisions(MapLayer triggerLayer) {
    	this.layer = triggerLayer;
    	
    	// extract all the polygon objects from the layer
    }
}
