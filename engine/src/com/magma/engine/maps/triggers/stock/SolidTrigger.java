package com.magma.engine.maps.triggers.stock;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.magma.engine.chars.Character;
import com.magma.engine.maps.triggers.MapTrigger;

public class SolidTrigger extends MapTrigger {

	public SolidTrigger(Rectangle rect, MapObject original) {
		super(rect, original);
	}

	@Override
	public void onTriggered(Actor a) {
		if (a instanceof Character) {
			Character c = (Character) a;
			c.collisionPush(rect);
		}
	}

	@Override
	protected void unpack(MapProperties properties) {

	}
}
