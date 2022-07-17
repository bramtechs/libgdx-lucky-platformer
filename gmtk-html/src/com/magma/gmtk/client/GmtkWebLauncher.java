package com.magma.gmtk.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.magma.engine.debug.MagmaLogger;
import com.magma.gmtk.GmtkGame;

public class GmtkWebLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                MagmaLogger.init(new MagmaLogger("GMTK Game"));
                GwtApplicationConfiguration config = new GwtApplicationConfiguration(640,480);
                return config;
        }



        @Override
        public ApplicationListener createApplicationListener () {
                return new GmtkGame(); 
        }

}
