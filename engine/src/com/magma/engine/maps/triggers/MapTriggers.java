package com.magma.engine.maps.triggers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.magma.engine.maps.MapStage;

public class MapTriggers extends Group {

	private final MapStage stage;
	
	public MapTriggers(MapStage stage, MapObjects objects, MapTriggerBuilder builder) {
		this.stage = stage;
		for (MapObject obj : objects) {
			if (obj.getProperties().containsKey("type")) {

				// construct MapTrigger
				MapTrigger trigger = builder.create(obj);
				if (trigger != null) { // ignore invalid triggers
					addActor(trigger);
				}
			}
		}
		Gdx.app.log("MapTriggers", toString());
	}

	@Override
	public void addActor(Actor actor) {
		stage.registerActor(actor);
		super.addActor(actor);
	}
	
    @Override
    public boolean remove(){
        for (Actor a : getChildren()){
                stage.unregisterActor(a);
        }
        return super.remove();
    }

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}

	@Override
	public String toString() {
		String debug = "";
		for (Actor a : getChildren()) {
			if (a instanceof MapTrigger) {
				MapTrigger entry = (MapTrigger)a;
				debug += "/==> trigger of type " + entry.getClass().getSimpleName() + "\n";
				debug += entry.toString();
			}
		}
		return debug;
	}

    public int getCount(){
        return this.getChildren().size;
    }
}
