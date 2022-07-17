package com.magma.engine.debug.modules;

public interface MapModuleListener {

    public void unloadMap();

    public void requestMap(String name);

    public void setTriggersVisible(boolean on);
}

