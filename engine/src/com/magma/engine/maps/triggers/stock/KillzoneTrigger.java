package com.magma.engine.maps.triggers.stock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.magma.engine.chars.Character;
import com.magma.engine.entities.Entity;
import com.magma.engine.maps.triggers.MapTrigger;

public class KillzoneTrigger extends MapTrigger {

    private int damage;

	public KillzoneTrigger(Rectangle rect, MapObject original) {
		super(rect, original);
	}

	@Override
	public void onTriggered(Actor a) {
	    if (a instanceof Character){
            // give damage to whoever steps on the trigger
            Character c = (Character)a;
            Entity e = c.getEntity();
            e.damage(damage);
        }
	}

	@Override
	protected void unpack(MapProperties properties) {
        Object damageValue = properties.get("damage");
        if (damageValue != null){
           damage = (int)damageValue; 
        }else{
            Gdx.app.error("KillzoneTrigger", "No damage amount defined for trigger");
        }
    }
}
