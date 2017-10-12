package ru.supernacho.at;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;

/**
 * Created by SuperNacho on 03.10.2017.
 */

public class GameOverScreen implements Screen {
    private TextureRegion bg;
    private TextureRegion skull;
    private TextureRegion buttonRetry;
    private BitmapFont font;
    private AstroTour mGame;
    private SpriteBatch batch;
    private Background background;
    private Rectangle retryRect;
    private MyInputProcessor mip;
    private StringBuilder stringHelper;

    public GameOverScreen(AstroTour game, SpriteBatch batch) {
        this.mGame = game;
        this.batch = batch;
    }

    @Override
    public void show() {
        Assets as = Assets.getInstances();
        background = new Background(as.atlas);
        buttonRetry = as.atlas.findRegion("retry");
        skull = as.atlas.findRegion("skull");
        font = as.assetManager.get("astrotour.fnt", BitmapFont.class);
        retryRect = new Rectangle(AstroTour.SCREEN_WIDTH / 2 - buttonRetry.getRegionWidth()/2, 50,
                buttonRetry.getRegionWidth(), buttonRetry.getRegionHeight());
        mip = (MyInputProcessor) Gdx.input.getInputProcessor();
        bg = as.atlas.findRegion("bg");
        stringHelper = new StringBuilder(50);
        Music music = as.gameOverMusic;
        music.setLooping(false);
        music.setVolume(AstroTour.musicVolume);
        music.play();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix( mGame.getCamera().combined);
        batch.begin();
        background.render(batch);
        batch.draw(skull, AstroTour.SCREEN_WIDTH/2 - skull.getRegionWidth()/2, AstroTour.SCREEN_HEIGHT/2 - skull.getRegionHeight()/2,
                skull.getRegionWidth(), skull.getRegionHeight());
        batch.setColor(1.0f, 1.0f, 1.0f, 0.70f);
        batch.draw(bg, 0, AstroTour.SCREEN_HEIGHT - 405, AstroTour.SCREEN_WIDTH, 185);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        stringHelper.setLength(0);
        stringHelper.append("Gained score: ").append((int) GameData.getInstance().getPlayerScore());
        font.draw(batch, stringHelper, 430.0f, (float) AstroTour.SCREEN_HEIGHT - 230.0f);
        stringHelper.setLength(0);
        stringHelper.append("Earned money: ").append((int) GameData.getInstance().getPlayerMoney());
        font.draw(batch, stringHelper, 430.0f, (float) AstroTour.SCREEN_HEIGHT - 275.0f);
        stringHelper.setLength(0);
        stringHelper.append("Gone distance: ").append((int) GameData.getInstance().getPlayerDistance());
        font.draw(batch, stringHelper, 430.0f, (float) AstroTour.SCREEN_HEIGHT - 320.0f);
        stringHelper.setLength(0);
        stringHelper.append("Reached level: ").append((int) GameData.getInstance().getPlayerLevel());
        font.draw(batch, stringHelper, 430.0f, (float) AstroTour.SCREEN_HEIGHT - 365.0f);

        batch.draw(buttonRetry, AstroTour.SCREEN_WIDTH / 2 - buttonRetry.getRegionWidth()/2, 50);
        batch.end();

    }

    public void update(float dt){
        background.update(dt, new Vector2(20.0f, 10.0f));
        if (mip.isTouchedInArea(retryRect) != -1){
            ScreenManager.getIncstance().switchScreen(ScreenManager.ScreenType.GAME);
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
