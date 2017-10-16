package ru.supernacho.at;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;

/**
 * Created by SuperNacho on 12.10.2017.
 */

public class ShopScreen implements Screen {

    private Stage stage;
    private Table table;
    private AstroTour game;
    private SpriteBatch batch;
    private Background background;
    private Vector2 vector;
    private float hp;
    private float hpMax;
    private int money;
    private int playerLives;
    private TextureRegion hpBg;
    private TextureRegion hpFill;
    private TextureRegion[] playerShip;
    private BitmapFont font;
    private StringBuilder sBhelper;

    private final int repCost = 100;
    private final int livesCost = 500;
    private final int laserCost = 1000;
    private final int canonCost = 500;

    public ShopScreen(AstroTour game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
    }

    @Override
    public void show() {
        stage = new Stage(game.getViewport());
        Gdx.input.setInputProcessor(stage);
        sBhelper = new StringBuilder(50);
        TextureAtlas atlas = Assets.getInstances().atlas;
        background = new Background(atlas);
        playerShip = new TextureRegion[]{
          atlas.findRegion("spaceShip2-0hp"),
                atlas.findRegion("spaceShip2-05hp"),
                atlas.findRegion("spaceShip2")
        };
        vector = new Vector2(100,0);
        table = new Table();
        table.setFillParent(true);
        table.background(background);
        stage.addActor(table);
//        table.setDebug(true);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(atlas.findRegion("shopUp"));
        style.down = new TextureRegionDrawable(atlas.findRegion("shopDown"));
        style.font = Assets.getInstances().assetManager.get("shop.fnt", BitmapFont.class);
        font = Assets.getInstances().assetManager.get("shop.fnt", BitmapFont.class);

        TextButton repair = new TextButton("Repair x " + repCost, style);
        TextButton lives = new TextButton("Buy lives x " + livesCost, style);
        TextButton laser = new TextButton("Buy Heavy Laser x " + laserCost, style);
        TextButton canon = new TextButton("Buy Canon x " + canonCost, style);
        TextButton exit = new TextButton("Save & Exit", style);
        TextButton startNextLvl = new TextButton("Continue", style);
        hpBg = atlas.findRegion("hpBg");
        hpFill = atlas.findRegion("hpFill");


        repair.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                return super.touchDown(event, x, y, pointer, button);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (money >= repCost) {
                    money -= repCost;
                    hp = hpMax;
                    GameData.getInstance().viewData();
                    GameData.getInstance().setPlayerHp(hpMax);
                    GameData.getInstance().setPlayerMoney(money);
                    GameData.getInstance().viewData();
                }
            }
        });
        lives.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (money >= livesCost) {
                    money -= livesCost;
                    playerLives++;
                    GameData.getInstance().viewData();
                    GameData.getInstance().setPlayerLives(playerLives);
                    GameData.getInstance().setPlayerMoney(money);
                    GameData.getInstance().viewData();
                }
            }
        });
        laser.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (money >= laserCost) {
                    money -= laserCost;
                    GameData.getInstance().setPlayerWeaponType(Weapon.WeaponType.HEAVYLASER.getIndex());
                    GameData.getInstance().setPlayerMoney(money);
                }
            }
        });
        canon.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (money >= canonCost) {
                    money -= canonCost;
                    GameData.getInstance().setPlayerWeaponType(Weapon.WeaponType.CANON.getIndex());
                    GameData.getInstance().setPlayerMoney(money);
                }
            }
        });
        exit.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                GameData.getInstance().viewData();
                GameData.getInstance().savePlayerProgress();
                GameData.getInstance().viewData();
                ScreenManager.getIncstance().switchScreen(ScreenManager.ScreenType.MENU);
            }
        });
        startNextLvl.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                GameData.getInstance().savePlayerProgress();
                AstroTour.isSavedGame = true;
                ScreenManager.getIncstance().switchScreen(ScreenManager.ScreenType.GAME);
            }
        });

        table.align(Align.bottom);
        table.padBottom(50.0f);
        table.add(repair).pad(25.0f);
        table.add(lives).pad(25.0f);
        table.add(laser).pad(25.0f);
        table.add(canon).pad(25.0f);
        table.row().colspan(2);
        table.add(exit).pad(25.0f);
        table.add(startNextLvl).pad(25.0f);

        getPlayerData();

    }

    private void getPlayerData(){
        hp = GameData.getInstance().getPlayerHp();
        hpMax = GameData.getInstance().getPlayerHpMax();
        money = GameData.getInstance().getPlayerMoney();
        playerLives = GameData.getInstance().getPlayerLives();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(game.getCamera().combined);
        background.update(delta, vector);
        stage.act(delta);
        stage.draw();
        batch.begin();
        batch.draw(hpBg, AstroTour.SCREEN_WIDTH/2 - 125, AstroTour.SCREEN_HEIGHT/2 +170);
        batch.draw(hpFill, AstroTour.SCREEN_WIDTH/2 - 125, AstroTour.SCREEN_HEIGHT/2 +170,hp,hpFill.getRegionHeight());
        sBhelper.setLength(0);
        sBhelper.append("Money: ").append(money).append("\nLives: ").append(playerLives)
        .append("\nWeapon: ").append(Weapon.WeaponType.values()[GameData.getInstance().getPlayerWeaponType()]);
        font.draw(batch, sBhelper, AstroTour.SCREEN_WIDTH/2 - 125, AstroTour.SCREEN_HEIGHT/2 + 150);
        if (hp < hpMax / 2) batch.draw(playerShip[0], AstroTour.SCREEN_WIDTH/2 - playerShip[0].getRegionWidth() / 2,
                AstroTour.SCREEN_HEIGHT/2 +210);
        if (hp >= hpMax / 2 && hp < hpMax) batch.draw(playerShip[1], AstroTour.SCREEN_WIDTH/2 - playerShip[0].getRegionWidth() / 2,
                AstroTour.SCREEN_HEIGHT/2 +210);
        if (hp == hpMax) batch.draw(playerShip[2], AstroTour.SCREEN_WIDTH/2 - playerShip[0].getRegionWidth() / 2,
                AstroTour.SCREEN_HEIGHT/2 +210);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        GameData.getInstance().savePlayerProgress();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        game.getInputProcessor();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
