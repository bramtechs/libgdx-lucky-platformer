package com.magma.engine.chars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.magma.engine.entities.Entity;
import com.magma.engine.input.GameInput;
import com.magma.engine.ui.dialog.Dialog;
import com.magma.engine.utils.Time;

public class CharacterController extends Action {

	public static final int key_up = GameInput.registerKey("move_up", Keys.W); // 0
	public static final int key_down = GameInput.registerKey("move_down", Keys.S); // 1
	public static final int key_left = GameInput.registerKey("move_left", Keys.A); // 2
	public static final int key_right = GameInput.registerKey("move_right", Keys.D); // 3

	protected Entity entity;
	protected boolean moveDiagonal;
	
	private boolean isMoving;
	private long[] pressTimes = new long[4];

	public enum Direction {
		Up, Down, Left, Right, None
	}

	/**
	 * Makes an actor movable with the keyboard. Default keys are WASD but can be
	 * changed in keybindings.json Designed for attaching to Characters
	 * 
	 * @param entity   Entity possessing the character
	 * @param diagonal Can the entity move in 8 directions (diagonal) or just 4.
	 */
	public CharacterController(Entity entity, boolean diagonal) {
		this.entity = entity;
		this.moveDiagonal = diagonal;
	}

	/**
	 * Makes an actor movable with the keyboard. Default keys are WASD but can be
	 * changed in keybindings.json Designed for attaching to Characters
	 * 
	 * @param entity Entity possessing the character
	 */
	public CharacterController(Entity entity) {
		this.entity = entity;
		this.moveDiagonal = false;
	}

	/**
	 * Makes an actor movable with the keyboard. Default keys are WASD but can be
	 * changed in keybindings.json Designed for attaching to Characters
	 * 
	 * @param speed Movement speed in pixels per second
	 */
	public CharacterController(float speed, boolean diagonal) {
		this.moveDiagonal = diagonal;
	}

	@Override
	public boolean act(float delta) {

        // stop the player from moving when dialog is playing
        if (Dialog.isSpeaking()){
            return false;
        }

		float sp = entity.getStats().speed * delta;

		// The character will move in the direction of the youngest pressed key.
		
		int[] allKeys = new int[] { key_up, key_down, key_left, key_right };
		
		for (int i = 0; i < allKeys.length; i++) {
			// pressing keys
			if (Gdx.input.isKeyJustPressed(allKeys[i])) {
				pressTimes[i] = Time.getTimeMillis();
                //MagmaLogger.log("Pressed",allKeys[i]);
			}

			//releasing
			if (!Gdx.input.isKeyPressed(allKeys[i])) {
				pressTimes[i] = Long.MIN_VALUE;
			}
		}

		// get the most recent pressed key of allKeys[]
		int largest = 0;
		for (int i = 0; i < allKeys.length; i++) {
			if (pressTimes[i] >= pressTimes[largest]) {
				largest = i;
			}
		}
	
		
		if (pressTimes[largest] > 0) {
			isMoving = true;
			switch (largest) {
			case 0: //key_up
				getActor().moveBy(0, sp);
				break;
			case 1: //key_down
				getActor().moveBy(0, -sp);
				break;
			case 2: //key_left
				getActor().moveBy(-sp, 0);
				break;
			case 3: //key_right
				getActor().moveBy(sp, 0);
				break;
			default: // none
				break;
			}
		}else {
			// no key being pressed
			isMoving = true;
		}
		return false;
	}

	public boolean isMoving() {
		return isMoving;
	}
	
	@Override
	public String toString() {
		return "CharacterController sp:" + entity.getStats().speed + " moving? " + isMoving;
	}
}
