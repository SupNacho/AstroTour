package ru.supernacho.at;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class AstroTour extends Game implements ApplicationListener {

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static int soundVolume = 1;
    public static int musicVolume = 1;
    public static boolean isSavedGame = false;

    private SpriteBatch batch;
	private Viewport mViewport;
	private Camera mCamera;
    private MyInputProcessor mip;

    public Viewport getViewport() {
        return mViewport;
    }

    public Camera getCamera() {
        return mCamera;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
	public void create () {
		batch = new SpriteBatch();
        mCamera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        mViewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, mCamera);
        mip = new MyInputProcessor(this);
        Gdx.input.setInputProcessor(mip);
        ScreenManager.getIncstance().init(this);
//        ScreenManager.getIncstance().switchScreen(ScreenManager.ScreenType.MENU);
        ScreenManager.getIncstance().switchScreen(ScreenManager.ScreenType.GAMEOVER);
//        ScreenManager.getIncstance().switchScreen(ScreenManager.ScreenType.SHOP);
	}
    @Override
	public void resize(int width, int height){
        mViewport.update(width, height, true);
        mViewport.apply();
    }

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
        getScreen().render(dt);

	}

    @Override
	public void dispose () {
		batch.dispose();
        }

        public void getInputProcessor(){
            Gdx.input.setInputProcessor(mip);
        }
}

