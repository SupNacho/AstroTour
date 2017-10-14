package ru.supernacho.at;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by SuperNacho on 03.10.2017.
 */

public class StartScreen implements Screen {
    private Stage stage;
    private Table tableBg;
    private Table tableMain;
    private Table tableSound;
    private AstroTour game;
    private SpriteBatch batch;
    private Background background;
    private Vector2 vector;
    private Texture mainTexture;
    private Music music;

    public StartScreen(AstroTour game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
    }

    @Override
    public void show() {
        stage = new Stage(game.getViewport());
        Gdx.input.setInputProcessor(stage);
        Assets as = Assets.getInstances();
        vector = new Vector2(20.0f, 10.0f);
        mainTexture = as.assetManager.get("main.png", Texture.class);

        TextButton.TextButtonStyle mainStyle = new TextButton.TextButtonStyle();
        mainStyle.up = new TextureRegionDrawable(as.atlas.findRegion("mainUp"));
        mainStyle.down = new TextureRegionDrawable(as.atlas.findRegion("mainDown"));
        mainStyle.font = as.assetManager.get("mainmenu.fnt", BitmapFont.class);

        ImageButton.ImageButtonStyle musicStyle = new ImageButton.ImageButtonStyle();
        musicStyle.up = new TextureRegionDrawable(as.atlas.findRegion("music"));
        musicStyle.down = new TextureRegionDrawable(as.atlas.findRegion("music"));
        musicStyle.checked = new TextureRegionDrawable(as.atlas.findRegion("music2"));

        ImageButton.ImageButtonStyle soundStyle = new ImageButton.ImageButtonStyle();
        soundStyle.up = new TextureRegionDrawable(as.atlas.findRegion("sound1"));
        soundStyle.down = new TextureRegionDrawable(as.atlas.findRegion("sound1"));
        soundStyle.checked = new TextureRegionDrawable(as.atlas.findRegion("sound2"));

        TextButton playButt = new TextButton("NEW GAME", mainStyle);
        TextButton loadButt = new TextButton("LOAD", mainStyle);
        TextButton topPilotButt = new TextButton("TOP PILOTS", mainStyle);
        TextButton exitButt = new TextButton("EXIT", mainStyle);

        final ImageButton musicButt = new ImageButton(musicStyle);
        ImageButton soundButt = new ImageButton(soundStyle);

        tableBg = new Table();
        tableMain = new Table();
        tableSound = new Table();


        this.background = new Background(as.atlas);
        tableBg.background(background);
        tableBg.setFillParent(true);

//        tableMain.setDebug(true);
        tableMain.pad(0.0f, 330.0f, 500.0f, 0);

        tableMain.add(playButt).pad(0,0,25,0);
        tableMain.row();
        tableMain.add(loadButt).pad(0,0,25,0);
        tableMain.row();
        tableMain.add(topPilotButt).pad(0,0,25,0);
        tableMain.row();
        tableMain.add(exitButt).pad(0,0,25,0);


//        tableSound.setDebug(true);
        tableSound.pad(0, AstroTour.SCREEN_WIDTH * 2 - 200, AstroTour.SCREEN_HEIGHT * 2 - 100, 0);
        tableSound.add(musicButt).pad(0,10,0,10);
        tableSound.add(soundButt).pad(0,10,0,10);

        playButt.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                ScreenManager.getIncstance().switchScreen(ScreenManager.ScreenType.GAME);
            }
        });
        loadButt.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                AstroTour.isSavedGame = true;
                ScreenManager.getIncstance().switchScreen(ScreenManager.ScreenType.GAME);
            }
        });
        topPilotButt.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        exitButt.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.exit();
            }
        });

        soundButt.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (AstroTour.soundVolume == 1) {
                    AstroTour.soundVolume = 0;
                } else {
                    AstroTour.soundVolume = 1;
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        musicButt.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (AstroTour.musicVolume == 1) {
                    AstroTour.musicVolume = 0;
                    music.pause();
                } else {
                    AstroTour.musicVolume = 1;
                    music.play();
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        music = as.menuMusic;
        music.setLooping(true);
        music.play();

        if (AstroTour.soundVolume == 0) soundButt.setChecked(true);
        if (AstroTour.musicVolume == 0) {
            musicButt.setChecked(true);
            music.pause();
        }

        stage.addActor(tableBg);
        stage.addActor(tableMain);
        stage.addActor(tableSound);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        batch.begin();
        batch.draw(mainTexture, 0, 0);
        batch.end();

    }

    public void update(float dt){
//        music.setVolume(AstroTour.musicVolume);
        background.update(dt, vector);
    }


    @Override
    public void resize(int width, int height) {
        game.getViewport().update(width, height, true);
        game.getViewport().apply();
    }

    @Override
    public void pause() {

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
