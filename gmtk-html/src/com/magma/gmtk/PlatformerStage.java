package com.magma.gmtk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.magma.engine.MagmaGame;
import com.magma.engine.assets.MagmaLoader;
import com.magma.engine.assets.Shapes;
import com.magma.engine.chars.CharacterController.Direction;
import com.magma.engine.debug.MagmaLogger;
import com.magma.engine.stages.GameStage;
import com.magma.engine.stages.ViewportContext;
import com.magma.engine.utils.Time;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class PlatformerStage extends GameStage {

    private static PlatformerStage instance;
    public static PlatformerPalette activePalette;

    public static long victoryTime;

    private static int height;
    private static int width;
    public static boolean isCheating;
    public static int resets;
    private static long startTime;

    private ShapeDrawer shapes;
    private PlatformerPlayer player;

    private char[][] map;
    private Label infoLabel;

    private FortuneWheel wheel;
    private TextButton restart;
    private TextButton giveUp;
    private Label speedrun;
    private Label levelCounter;
    private Label resetCounter;

    private boolean hasSupport;
    private Label cheat;

    private Array<PlatformerTile> ice;

    // TODO: get a map loaded on the screen
    public PlatformerStage(int map, SpriteBatch batch) {
        super(calculateViewports(map), batch);
        if (startTime == 0) {
            startTime = Time.getTimeMillis();
        }
        instance = this;
        setRandomPalette();

        ice = new Array<PlatformerTile>();
        importMap(map);

        InputMultiplexer plex = new InputMultiplexer(this, ui);
        Gdx.input.setInputProcessor(plex);

        Skin skin = MagmaLoader.getDebugSkin();

        cheat = new Label("cheater mode, press function keys, rctrl or enter", skin) {
            @Override
            public void act(float delta) {
                if (Gdx.input.isKeyJustPressed(Keys.F4)) {
                    isCheating = true;
                    cheat.setVisible(isCheating);
                    MagmaLogger.log("hax");
                }
                if (isCheating) {
                    if (Gdx.input.isKeyJustPressed(Keys.CONTROL_RIGHT)) {
                        GmtkGame.prevLevel();
                    }
                    if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
                        GmtkGame.nextLevel();
                    }
                    if (Gdx.input.isKeyJustPressed(Keys.B)) {
                        GmtkGame.loadLevel(10);
                    }
                }
                super.act(delta);
            }
        };
        cheat.setColor(Color.PINK);
        cheat.setVisible(isCheating);
        cheat.setPosition(250, 450);
        ui.addActor(cheat);

        restart = new TextButton("Reset", skin);
        restart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GmtkGame.restart();
                super.clicked(event, x, y);
            }
        });
        restart.setColor(Color.RED);
        restart.setHeight(35);

        giveUp = new TextButton("Give up", skin);
        giveUp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                giveUp();
                super.clicked(event, x, y);
            }
        });

        giveUp.setPosition(175, 10);
        giveUp.setWidth(350);
        giveUp.setHeight(35);
        giveUp.setColor(Color.CYAN);
        ui.addActor(giveUp);

        TextButton muteMusic = new TextButton("Mute music",skin);
        muteMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PlatformerAudio.mute();
                super.clicked(event, x, y);
            }
        });
        muteMusic.setPosition(540,10);
        muteMusic.setWidth(90);
        muteMusic.setHeight(35);
        ui.addActor(muteMusic);

        resetCounter = new Label("Resets " + resets, skin);
        resetCounter.setPosition(10, 420);
        ui.addActor(resetCounter);

        levelCounter = new Label("Level " + (map + 1), skin);
        levelCounter.setPosition(10, 446);
        levelCounter.setFontScale(1.5f);
        ui.addActor(levelCounter);

        // level text
        String text = "";
        Label info = new Label(text, skin) {
            public void act(float delta) {
                toFront();
            }
        };
        switch (map) {
            case 0:
                text = "WASD to move, SPACE to jump\nWhen unlucky or stuck press Reset\nCollect the rainbow blocks(tm) for a surprise mechanic!";
                break;
            case 1:
                text = "Look out for bullets! :O";
                break;
            case 2:
                text = "You can give up any time.";
                break;
            case 3:
                text = "Brrr... who turned on the AC?";
                break;
            case 4:
                text = "Pool party!";
                break;
            case 5:
                text = "Look out below!";
                break;
            case 6:
                text = "Just a few more levels!";
                break;
            case 7:
                text = "This may take a while...";
                break;
            case 8:
                text = "zzzzzzzzzzz...";
                break;
            case 9:
                text = "Huh, where are you going?";
                break;
            case 10:
                text = "Muhahaha... I am evil and you must die!";
                info.setColor(Color.RED);
                break;
        }
        info.setText(text);
        info.setPosition(150, 400);
        ui.addActor(info);

        speedrun = new Label("", skin) {
            @Override
            public void act(float delta) {
                long time = Time.getTimeMillis() - startTime;
                setText(time + " ms");
                super.act(delta);
            }
        };
        speedrun.setPosition(8, 70);
        ui.addActor(speedrun);

        restart.setPosition(10, 10);
        restart.setWidth(150);
        restart.setHeight(35);
        ui.addActor(restart);

        infoLabel = new Label("-", skin) {
            @Override
            public void act(float delta) {
                setText("FPS: " + Gdx.graphics.getFramesPerSecond() + " Objects: " + getRoot().getChildren().size);
                toFront();
                super.act(delta);
            }
        };
        infoLabel.setVisible(false);

        infoLabel.setPosition(20, 50);
        ui.addActor(infoLabel);

        shapes = Shapes.getInstance();
        addActor(new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                toBack();
                shapes.setColor(PlatformerStage.activePalette.getBgColor());
                shapes.filledRectangle(0, 0, 48, 32);
                super.draw(batch, parentAlpha);
            }
        });
        MagmaGame.setBackgroundColor(activePalette.getBgColor());
    }

    public static ViewportContext calculateViewports(int i) {
        String content = Maps.getMapString(i);
        String[] lines = content.split("\n");
        width = lines[0].length();
        height = lines.length + 1;
        ViewportContext views = ViewportContext.createRetro(width, height, 640, 480);
        return views;
    }

    public void importMap(int i) {
        MagmaLogger.log(this, "loading " + i);
        String content = Maps.getMapString(i);
        String[] lines = content.split("\n");
        map = new char[lines[0].length()][lines.length];
        int playerX = 0, playerY = 0;
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[0].length(); x++) {
                char c = Character.toLowerCase(lines[y].charAt(x));
                map[x][y] = c;
                PlatformerTile tile = null;
                int yy = lines.length - y;
                switch (c) {
                    case '=':
                        tile = new PlatformerTile(x, yy);
                        break;
                    case '+':
                        tile = new IceTile(x, yy);
                        ice.add(tile);
                        break;
                    case 'l':
                        tile = new LuckyBlock(x, yy);
                        break;
                    case 's':
                        tile = new SupportTile(x, yy);
                        break;
                    case 'v':
                        tile = new TurretTile(x, yy, Direction.Down);
                        break;
                    case '^':
                        tile = new TurretTile(x, yy, Direction.Up);
                        break;
                    case '<':
                        tile = new TurretTile(x, yy, Direction.Left);
                        break;
                    case '>':
                        tile = new TurretTile(x, yy, Direction.Right);
                        break;
                    case 'p':
                        playerX = x;
                        playerY = yy;
                        break;
                    case 'b':
                        addActor(new PlatformerBoss(this,x, yy));
                        break;
                    default:
                        break;
                }

                if (tile != null)
                    addActor(tile);
            }
        }

        player = new PlatformerPlayer(this, playerX, playerY);
        addActor(player);
    }

    public boolean isSolid(int x, int y) {
        y = map[0].length - y;
        if (x < 0 || x >= map.length || y < 0 || y >= map[0].length) {
            return false;
        }
        return map[x][y] != '#' && map[x][y] != 'b' && map[x][y] != 'p' && !(map[x][y] == 's' && !hasSupport);
    }

    public void deleteTile(PlatformerTile tile) {
        int x = (int) tile.getX();
        int y = height - (int) tile.getY() - 1;
        map[x][y] = '#';
        MagmaLogger.log(this, "delete " + x + " " + y);
    }

    public static void setRandomPalette() {
        activePalette = PlatformerPalette.getRandomPalette();
        MagmaGame.setBackgroundColor(activePalette.getBgColor());
    }

    public void spawnFortuneWheel() {
        if (wheel != null) {
            wheel.remove();
        }
        wheel = new FortuneWheel();
        ui.addActor(wheel);
        MagmaLogger.log("speen");
    }

    public static void melt() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (instance.map[x][y] == '+') {
                    instance.map[x][y] = '#';
                }
            }
        }
        for (PlatformerTile tile : instance.ice) {
            tile.remove();
        }
    }

    public static void flipLevel() {
        OrthographicCamera cam = instance.getOrthoCamera();
        cam.viewportWidth = -cam.viewportWidth;
        cam.rotate(180);
    }

    public static void disarm() {
        ArrayIterator<Actor> it = instance.getRoot().getChildren().iterator();
        while (it.hasNext()) {
            Actor a = it.next();
            if (a instanceof TurretTile) {
                ((TurretTile) a).dismantle();
            }
        }
        MagmaLogger.log("disarmed");
    }

    public static void support() {
        ArrayIterator<Actor> it = instance.getRoot().getChildren().iterator();
        while (it.hasNext()) {
            Actor a = it.next();
            if (a instanceof SupportTile) {
                ((SupportTile) a).activate();
                instance.hasSupport = true;
            }
        }
        MagmaLogger.log("activated support");
    }

    public static void giveUp(){
        victoryTime = Time.getTimeMillis()-startTime;
        PlatformerAudio.stopMusic();
        GmtkGame.loadLevel(-1);
    }

    public static void victory(){
        for (int y = 0; y < 12; y++){
            instance.map[28][y] = '#';
        }
        ArrayIterator<Actor> it = instance.getRoot().getChildren().iterator();
        while (it.hasNext()) {
            Actor a = it.next();
            if (a instanceof PlatformerTile){
                if ((int)a.getX() == 28){
                    a.remove();
                }
            }
        }
        victoryTime = Time.getTimeMillis()-startTime;
    }
}
