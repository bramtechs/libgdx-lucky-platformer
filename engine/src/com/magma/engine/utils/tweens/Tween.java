package com.magma.engine.utils.tweens;

import com.magma.engine.utils.Time;

public class Tween {

	private float start;
	private float end;
	private float duration;
	private float startTime;
	
	public Tween() {
		
	}
	
	//LINEAR: y = start + x / duration * (end - start)
	public Tween(float start, float end, float duration) {
		init(start,end,duration);
	}
	
	public void init(float start, float end, float duration) {
		if (duration < 0) {
			float origEnd = end;
			end = start;
			start = origEnd;
			duration = -duration;
		}
		this.startTime = Time.getTime(); 
		this.start = start;
		this.end = end;
		this.duration = duration;
	}
		
	public boolean isBusy() {
		return Time.getTime() < startTime + duration;
	}
	
	public boolean isDone() {
		return Time.getTime() > startTime + duration;
	}
	
	public float getTime() {
		return Time.getTime()-startTime;
	}
	
	public float getValue() {
		return start + getTime() / duration * (end - start);
	}
}
