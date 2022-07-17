package com.magma.engine.chars;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.magma.engine.entities.Entity;

public class CharacterMover extends Action {

	private GridPoint2 target;
    private boolean doEnd;

	private final Entity entity;
	
	public CharacterMover(Entity entity, GridPoint2 target) {
		this.target = target;
		this.entity = entity;
	}
	
	public CharacterMover(Entity entity, int x, int y) {
		this.target = new GridPoint2(x, y);
		this.entity = entity;
	}
	
	public CharacterMover(Entity entity) {
		this.entity = entity;
	}
	
	public void target(GridPoint2 target) {
		this.target = target;
	}
	
	public void target(int x, int y) {
		target.set(x,y);
	}
	
	@Override
	public boolean act(float delta) {
        if (doEnd){
            return true;
        }
		if (target == null) {
			return false;
		}

		float sp = entity.getStats().speed * delta;

		int x = (int) (getActor().getX());
		int y = (int) (getActor().getY());
		
		// has arrived?
		if (target.x == x && target.y == y) {
			return false;
		}
		
		if (x < target.x) {
			getActor().moveBy(sp, 0);
		}
		if (x > target.x) {
			getActor().moveBy(-sp, 0);
		}
		if (y < target.y) {
			getActor().moveBy(0, sp);
		}
		if (y > target.y) {
			getActor().moveBy(0, -sp);
		}
		
		return false;
	}

	@Override
	public String toString() {
		String text = "CharacterMover: ";
		if (target == null) {
			text += "(no target)";
		}else {
			text += target.toString();
		}
		return text;
	}
	
    public void stop(){
       doEnd = true; 
    }
}
