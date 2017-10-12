package ru.supernacho.at;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by SuperNacho on 03.10.2017.
 */

public class StartScreen implements Screen {
    private AstroTour mGame;
    private SpriteBatch batch;
    private Background background;
    private Rectangle playButtRect;
    private Rectangle exitButtRect;
    private TextureRegion playButton;
    private TextureRegion exitTexture;
    private TextureRegion currentButtonTexture;
    private MyInputProcessor mip;
    private Vector2 vector;
    private Texture mainTexture;
    private Music music;
    private TextureRegion sndButton;
    private TextureRegion sndButtonPrsd;
    private TextureRegion sndButtonCurr;
    private TextureRegion mscButton;
    private TextureRegion mscButtonPrsd;
    private TextureRegion mscButtonCurr;
    private float chkSoundTimer;
    private Rectangle musicRect;
    private Rectangle soundRect;
    private Rectangle loadSavedRect;

    public StartScreen(AstroTour game, SpriteBatch batch) {
        this.mGame = game;
        this.batch = batch;
    }

    @Override
    public void show() {
        Assets as = Assets.getInstances();
        this.background = new Background(as.atlas);
        this.playButton = as.atlas.findRegion("play");
        this.exitTexture = as.atlas.findRegion("exit");
        this.currentButtonTexture = playButton;
        this.exitButtRect = new Rectangle(AstroTour.SCREEN_WIDTH - 350,
                AstroTour.SCREEN_HEIGHT / 2 - playButton.getRegionHeight() / 2,
                exitTexture.getRegionWidth(), exitTexture.getRegionHeight());
        this.playButtRect = new Rectangle(100,
                AstroTour.SCREEN_HEIGHT / 2 - playButton.getRegionHeight() / 2,
                playButton.getRegionWidth(), playButton.getRegionHeight());
        this.mainTexture = as.assetManager.get("main.png", Texture.class);
        vector = new Vector2(20.0f, 10.0f);

        sndButton = as.atlas.findRegion("sound1");
        sndButtonPrsd = as.atlas.findRegion("sound2");
        sndButtonCurr = sndButton;
        mscButton = as.atlas.findRegion("music");
        mscButtonPrsd = as.atlas.findRegion("music2");
        mscButtonCurr = mscButton;
        soundRect = new Rectangle(AstroTour.SCREEN_WIDTH - 55, AstroTour.SCREEN_HEIGHT - 55, 50, 50);
        musicRect = new Rectangle(AstroTour.SCREEN_WIDTH - 110, AstroTour.SCREEN_HEIGHT - 55, 50, 50);
        loadSavedRect = new Rectangle(AstroTour.SCREEN_WIDTH - 165, AstroTour.SCREEN_HEIGHT - 55, 50, 50);

        mip = (MyInputProcessor) Gdx.input.getInputProcessor();
        music = as.menuMusic;
        music.setLooping(true);
        music.play();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(mGame.getCamera().combined);
        batch.begin();
        background.render(batch);
        batch.draw(mainTexture, 0, 0);
        batch.draw(currentButtonTexture, 100,
                AstroTour.SCREEN_HEIGHT / 2 - playButton.getRegionWidth() / 2);
        batch.draw(exitTexture, AstroTour.SCREEN_WIDTH - 350,
                AstroTour.SCREEN_HEIGHT / 2 - playButton.getRegionWidth() / 2);
        batch.draw(sndButtonCurr, soundRect.x, soundRect.y);
        batch.draw(mscButtonCurr, musicRect.x, musicRect.y);
        batch.end();

    }

    public void update(float dt){
        music.setVolume(AstroTour.musicVolume);
        background.update(dt, vector);
        updateSNDcontroll(dt);
        if (mip.isTouchedInArea(loadSavedRect) > -1) {
            AstroTour.isSavedGame = true;
            ScreenManager.getIncstance().switchScreen(ScreenManager.ScreenType.GAME);
        }

        if (mip.isTouchedInArea(playButtRect) != -1){
            ScreenManager.getIncstance().switchScreen(ScreenManager.ScreenType.GAME);
        }

        if (mip.isTouchedInArea(exitButtRect) != -1){
            Gdx.app.exit();
        }
    }

    private void updateSNDcontroll(float dt){
        if (chkSoundTimer <= 0){
            chkSoundTimer = 0;
            if (mip.isTouchedInArea(soundRect) > -1) {
                if (AstroTour.soundVolume == 1) {
                    AstroTour.soundVolume = 0;
                    sndButtonCurr = sndButtonPrsd;
                    chkSoundTimer = 0.5f;
                } else {
                    AstroTour.soundVolume = 1;
                    sndButtonCurr = sndButton;
                    chkSoundTimer = 0.5f;

                }
            }
        } else {
            chkSoundTimer -= dt;
        }
        if (chkSoundTimer <= 0){
            chkSoundTimer = 0;
            if (mip.isTouchedInArea(musicRect) > -1) {
                if (AstroTour.musicVolume == 1) {
                    AstroTour.musicVolume = 0;
                    mscButtonCurr = mscButtonPrsd;
                    chkSoundTimer = 0.5f;
                } else {
                    AstroTour.musicVolume = 1;
                    mscButtonCurr = mscButton;
                    chkSoundTimer = 0.5f;

                }
            }
        } else {
            chkSoundTimer -= dt;
        }
    }




    @Override
    public void resize(int width, int height) {
        mGame.getViewport().update(width, height, true);
        mGame.getViewport().apply();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }

}
