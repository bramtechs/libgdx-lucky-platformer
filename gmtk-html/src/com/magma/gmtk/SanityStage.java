package com.magma.gmtk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.magma.engine.assets.MagmaLoader;
import com.magma.engine.stages.GameStage;
import com.magma.engine.stages.ViewportContext;

public class SanityStage extends GameStage {

    public SanityStage(SpriteBatch batch) {
        super(ViewportContext.createRetro(200, 200, 640, 480), batch);

        TextureRegion bg = new TextureRegion(MagmaLoader.getAtlasRegion("sanity"));
        Actor bgActor = new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha){
                batch.setColor(Color.WHITE);
                batch.draw(bg,0,0,640,480);
            }
        };
        ui.addActor(bgActor);
        bgActor.toBack();

        Skin skin = MagmaLoader.getDebugSkin();
        Label label = new Label("You gave up.", skin);
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

        Label message = new Label("You got the sane ending!", skin);
        message.setY(80);
        message.setWidth(640);
        message.setColor(Color.LIME);
        message.setAlignment(Align.center);
        ui.addActor(message);
    }
}

