package com.magma.engine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

@SuppressWarnings("unused")
public class Time {
	
	private static long startTime = TimeUtils.millis();
	
	public static long getTimeMillis() {
		return TimeUtils.millis()-startTime;
	}

	public static float getTime() {
		return getTimeMillis()/1000f;
	}

    public static long getStartTime() {
        return startTime;
    }
}
