package com.magma.gmtk;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.magma.engine.assets.MagmaLoader;
import com.magma.engine.stages.GameStage;
import com.magma.engine.stages.ViewportContext;
import com.badlogic.gdx.graphics.Color;

public class VictoryStage extends GameStage {

    public VictoryStage(SpriteBatch batch) {
        super(ViewportContext.createRetro(200, 200, 640, 480), batch);

        TextureRegion bg = new TextureRegion(MagmaLoader.getAtlasRegion("victory"));
        Actor bgActor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.setColor(Color.WHITE);
                batch.draw(bg, 0, 0, 640, 480);
            }
        };
        ui.addActor(bgActor);
        bgActor.toBack();

        Skin skin = MagmaLoader.getDebugSkin();
        Label label = new Label("You did it!", skin);
        label.setY(420);
        label.setFontScale(3);
        label.setWidth(640);
        label.setAlignment(Align.center);
        ui.addActor(label);

        Label time = new Label(PlatformerStage.victoryTime + " ms", skin);
        time.setFontScale(2);
        time.setY(300);
        time.setWidth(640);
        time.setAlignment(Align.center);
        ui.addActor(time);

        Label resets = new Label(PlatformerStage.resets + " resets", skin);
        resets.setY(180);
        resets.setWidth(640);
        resets.setAlignment(Align.center);
        ui.addActor(resets);

        String text = "You got the EPIC GAMER ending!";
        Color col = Color.RED;
        if (PlatformerStage.isCheating) {
            text = "You got the HACKERMAN ending!";
            col = Color.PINK;
        }
        Label message = new Label(text, skin);
        message.setY(80);
        message.setWidth(640);
        message.setColor(col);
        message.setAlignment(Align.center);
        ui.addActor(message);
    }

}
