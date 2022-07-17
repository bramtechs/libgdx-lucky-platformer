package com.magma.engine.gfx;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.magma.engine.utils.tweens.Tween;

public class HoverInEffect extends Action {

	private final ModelInstance instance;
	private final Vector3 pos;

	private Tween tween;

	public HoverInEffect(final ModelInstance instance, final float sink, final float duration, float delay) {
		this.instance = instance;
		this.pos = instance.transform.getTranslation(new Vector3());
		instance.transform.setTranslation(pos.x, pos.y + sink, pos.z);
		Timer.schedule(new Task() {
			@Override
			public void run() {
				tween = new Tween(sink, pos.y, duration);
			}
		}, delay);
	}

	@Override
	public boolean act(float delta) {
		if (tween != null) {
			if (tween.isDone()) {
				instance.transform.setTranslation(pos.x, pos.y, pos.z);
				return true;
			} else {
				float y = tween.getValue();
				instance.transform.setTranslation(pos.x, y, pos.z);
			}
		}
		return false;
	}

}
