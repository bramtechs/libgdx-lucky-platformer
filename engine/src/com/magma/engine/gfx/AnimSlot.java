package com.magma.engine.gfx;

// A convoluted way to not compare strings
public class AnimSlot {
    private static int nextIndex;
    
    public final int index;
    public final float fps;
    public final String name;
    
    public AnimSlot(String name, float fps){
        this.index = nextIndex;
        this.fps = fps;
        this.name = name;
        nextIndex++;
    }
    
    public static AnimSlot of(String name, float fps) {
    	return new AnimSlot(name,fps);
    }
    public static AnimSlot of(String name) {
    	return new AnimSlot(name,2);
    }
}
