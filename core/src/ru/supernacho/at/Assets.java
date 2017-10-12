package ru.supernacho.at;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by SuperNacho on 07.10.2017.
 */

public class Assets {
    private static final Assets ourInstance = new Assets();

    public static Assets getInstances(){
        return ourInstance;
    }

    AssetManager assetManager;
    TextureAtlas atlas;
    Sound explosion;
    Sound laser;
    Sound canon;
    Sound bulletHit;
    Sound collisionHit;
    Music menuMusic;
    Music gameMusic;
    Music gameOverMusic;

    private Assets(){
        assetManager = new AssetManager();
    }

    public void loadAssets(ScreenManager.ScreenType type){
        switch (type){
            case MENU:
                assetManager.load("startmenu.pack", TextureAtlas.class);
                assetManager.load("main.png", Texture.class);
                assetManager.load("menuScreen.ogg", Music.class);
                assetManager.finishLoading();
                atlas = assetManager.get("startmenu.pack", TextureAtlas.class);
                menuMusic = assetManager.get("menuScreen.ogg", Music.class);
                break;
            case GAME:
                assetManager.load("my.pack", TextureAtlas.class);
                assetManager.load("astrotour.fnt", BitmapFont.class);
                assetManager.load("laser4.wav", Sound.class);
                assetManager.load("explosion2.wav", Sound.class);
                assetManager.load("musket-4.wav", Sound.class);
                assetManager.load("bullethit.mp3", Sound.class);
                assetManager.load("collision.ogg", Sound.class);
                assetManager.load("boost.wav", Sound.class);
                assetManager.load("mainTheme.ogg", Music.class);
                assetManager.finishLoading();
                atlas = assetManager.get("my.pack", TextureAtlas.class);
                explosion = assetManager.get("explosion2.wav", Sound.class);
                laser = assetManager.get("laser4.wav", Sound.class);
                canon = assetManager.get("musket-4.wav", Sound.class);
                bulletHit = assetManager.get("bullethit.mp3", Sound.class);
                collisionHit = assetManager.get("collision.ogg", Sound.class);
                gameMusic = assetManager.get("mainTheme.ogg", Music.class);
                break;
            case GAMEOVER:
                assetManager.load("gameover.pack", TextureAtlas.class);
                assetManager.load("astrotour.fnt", BitmapFont.class);
                assetManager.load("over.ogg", Music.class);
                assetManager.finishLoading();
                atlas = assetManager.get("gameover.pack", TextureAtlas.class);
                gameOverMusic = assetManager.get("over.ogg", Music.class);
                break;
            default:
                throw new RuntimeException("Assets switch case not found");
        }
    }
}
