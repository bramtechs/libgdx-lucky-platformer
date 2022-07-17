package com.magma.engine.gfx;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.magma.engine.assets.MagmaLoader;
import com.magma.engine.utils.Time;

public class AnimatedSprite extends SpriteActor {

	private HashMap<Integer,Animation<AtlasRegion>> animations;
	private int curAnim;
	private boolean isStopped;
	
	public AnimatedSprite(AnimSlot... slots) {
		super(new Sprite(MagmaLoader.getPlaceholderTexture()));
		animations = new HashMap<Integer,Animation<AtlasRegion>>();
		for (AnimSlot slot: slots){
			Array<AtlasRegion> regions = MagmaLoader.getAtlasRegions(slot.name);
			Animation<AtlasRegion> anim = new Animation<AtlasRegion>(1f/slot.fps, regions);
			animations.put(slot.index,anim);
		}
	}

	public void setCurrentAnimation(AnimSlot slot){
		if (!animations.containsKey(slot.index)){
			throw new IllegalArgumentException("Cannot get animation of index " + slot.index + " ("+slot.name+") Not registered!");
		}
		isStopped = false;
		curAnim = slot.index;
	}
	
	public int getCurrentAnimation(){
		return curAnim;
	}

	// TODO: Add more control
	public AnimatedSprite setPlayMode(PlayMode mode) {
		for (Animation<AtlasRegion> anim : animations.values()){
			anim.setPlayMode(mode);
		}
		return this;
	}
	public AnimatedSprite setPlayMode(PlayMode mode, AnimSlot slot) {
		if (!animations.containsKey(slot.index)){
			throw new IllegalArgumentException("Cannot get animation of index " + slot.index + " ("+slot.name+")");
		}
		animations.get(slot.index).setPlayMode(mode);
		return this;
	}

	public PlayMode getPlayMode(AnimSlot slot) {
		if (!animations.containsKey(slot.index)){
			throw new IllegalArgumentException("Cannot get animation of index " + slot.index + " ("+slot.name+")");
		}
		return animations.get(slot.index).getPlayMode();
	}

	public void stop() {
		isStopped = true;
	}
	
	@Override
	public void act(float delta) {
		float time = 0;
		if (!isStopped) {
			time = Time.getTime();
		}
		
		Animation<AtlasRegion> animation = animations.get(curAnim);
		TextureRegion region = null;
		if (animation == null) {
			throw new IllegalStateException("No animation set for " + getClass().getSimpleName());
		}
		
		region = animation.getKeyFrame(time);
		
		// setRegion removes sprites flip, so we need to do this terribleness
		boolean origFlipX = isFlipX();
		boolean origFlipY = isFlipY();
		setRegion(region);
		setFlip(origFlipX, origFlipY);
		super.act(delta);
	}
}
