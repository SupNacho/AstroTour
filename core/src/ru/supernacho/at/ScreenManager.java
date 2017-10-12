package ru.supernacho.at;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

/**
 * Created by SuperNacho on 07.10.2017.
 */

public class ScreenManager {
    enum ScreenType {
        MENU, GAME, GAMEOVER
    }

    private static final ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getIncstance(){
        return ourInstance;
    }

    private AstroTour game;
    private StartScreen menuScreen;
    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;

    public void init(AstroTour game){
        this.game = game;
        this.menuScreen = new StartScreen(game,game.getBatch());
        this.gameScreen = new GameScreen(game,game.getBatch());
        this.gameOverScreen = new GameOverScreen(game,game.getBatch());
    }

    public void switchScreen(ScreenType type){
        Screen screen = game.getScreen();
        Assets.getInstances().assetManager.clear();
        Assets.getInstances().assetManager.dispose();
        Assets.getInstances().assetManager = new AssetManager();
        if (screen != null) {
            screen.dispose();
        }
        switch (type){
            case MENU:
                Assets.getInstances().loadAssets(ScreenType.MENU);
                game.setScreen(menuScreen);
                break;
            case GAME:
                Assets.getInstances().loadAssets(ScreenType.GAME);
                game.setScreen(gameScreen);
                break;
            case GAMEOVER:
                Assets.getInstances().loadAssets(ScreenType.GAMEOVER);
                game.setScreen(gameOverScreen);
                break;
            default:
                throw new RuntimeException("ScreenManager switch case not found");
        }
    }
    private ScreenManager (){

    }

    public void dispose(){

    }
}
