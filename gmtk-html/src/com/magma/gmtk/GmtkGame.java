package com.magma.gmtk;

import com.magma.engine.MagmaGame;
import com.magma.engine.assets.MagmaLoader;
import com.magma.engine.debug.MagmaLogger;
import com.magma.engine.stages.StageSwitcher;

public class GmtkGame extends MagmaGame {

    private static PlatformerStage stage;
    private static int curLevel;

    public GmtkGame() {
        super("gmtk/");
        PlatformerStage.resets = 0;
    }

    @Override
    protected void initStages() {
        MagmaLoader.loadAtlas("sprites");
        PlatformerAudio.load();
        StageSwitcher.open(new PlatformerMenuStage(batch));
        MagmaLogger.log(this,"pssst hey ... yes you! did you know you can press f4 to bring up debug mode?");
    }

    public static void loadLevel(int i) {
        if (i == 11) {
            StageSwitcher.open(new VictoryStage(batch));
            return;
        }
        if (i == -1) {
            StageSwitcher.open(new SanityStage(batch));
            return;
        }
        stage = new PlatformerStage(i, batch);
        curLevel = i;
        StageSwitcher.open(stage);
    }

    public static void restart() {
        PlatformerStage.resets++;
        loadLevel(curLevel);
        MagmaLogger.log("RESET");
    }

    public static void nextLevel() {
        loadLevel(curLevel + 1);
    }

    public static void prevLevel() {
        loadLevel(curLevel - 1);
    }
}
