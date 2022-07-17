package com.magma.gmtk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.magma.engine.MagmaGame;
import com.magma.engine.assets.MagmaLoader;
import com.magma.engine.stages.GameStage;
import com.magma.engine.stages.ViewportContext;

public class PlatformerMenuStage extends GameStage {


	public PlatformerMenuStage(SpriteBatch batch) {
		super(ViewportContext.createRetro(10, 10, 640, 480), batch);

        Gdx.input.setInputProcessor(ui);

        MagmaGame.setBackgroundColor(Color.TEAL);
        TextureRegion logo = MagmaLoader.getAtlasRegion("logo");
        Image spriteLogo = new Image(logo); 
        spriteLogo.scaleBy(-0.3f);
        spriteLogo.setPosition(60, 280);
        
        ui.addActor(spriteLogo);

        TextureRegion descr = MagmaLoader.getAtlasRegion("descr");
        Image descrLogo = new Image(descr); 
        descrLogo.scaleBy(-0.6f);
        descrLogo.setPosition(50, 220);

        ui.addActor(descrLogo);

        Skin skin = MagmaLoader.getDebugSkin();

        TextButton start = new TextButton("Play",skin);
        start.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                PlatformerAudio.playLevelMusic();
                GmtkGame.loadLevel(0);
            }
        });
        start.setSize(250, 40);
        start.setPosition(125, 40);
        ui.addActor(start);

        Label warn = new Label("Note: This game is frustratingly hard by (lack of) design.",skin);
        warn.setColor(Color.YELLOW);
        warn.setFontScale(0.9f);
        warn.setPosition(2, 2);
        ui.addActor(warn);
	}
}

