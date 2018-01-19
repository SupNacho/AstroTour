package ru.supernacho.at;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by SuperNacho on 25.09.2017.
 */

public class AsteroidEmitter extends ObjectPool<Asteroid> {
    private TextureRegion mTextureRegion;
    private float generationTime;
    private float innerTimer;
    private GameScreen game;
    private boolean isStoped;

    public AsteroidEmitter(GameScreen game, TextureRegion textureRegion, int size, float generationTime) {
        super();
        this.game = game;
        this.mTextureRegion = textureRegion;
        this.generationTime = generationTime;
        this.innerTimer = 0.0f;
        for (int i = 0; i < size; i++) {
            freeList.add(newObject());
        }
    }

    public void update(float dt){
        if (!isStoped) {
            innerTimer += dt;
            if (innerTimer > generationTime) {
                innerTimer -= generationTime;
                setUp();
            }
            for (int i = 0; i < activeList.size(); i++) {
                activeList.get(i).update(dt);
            }
        }
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
//            Asteroid asteroid = activeList.get(i);
//            float reddish = asteroid.getWounds();
//            if (reddish > 0.01f) {
//                batch.setColor(1.0f, 1.0f - reddish, 1.0f - reddish, 1.0f);
//            }
//            batch.draw(mTextureRegion, asteroid.getPosition().x - mTextureRegion.getRegionWidth()/2,
//                    asteroid.getPosition().y - mTextureRegion.getRegionHeight()/2,
//                    (float) asteroid.getAsteroidSize()/2, (float) asteroid.getAsteroidSize()/2,
//                    asteroid.getAsteroidSize(), asteroid.getAsteroidSize(),
//                    asteroid.getScale(),asteroid.getScale(), asteroid.getRotation());
//            if (reddish > 0.01f) {
//                batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
//            }
        }
    }

    public void setUp(float x, float y, float vx, float vy){
        Asteroid a = getActiveElement();
        a.activate(x,y,vx,vy);
    }
    public void setUp(){
        Asteroid a = getActiveElement();
        float x = MathUtils.random(AstroTour.SCREEN_WIDTH + 128, AstroTour.SCREEN_WIDTH * 1.5f);
        float y = MathUtils.random(0.0f, AstroTour.SCREEN_HEIGHT);
        float vx = -MathUtils.random(120.0f, 320.0f);
        float vy = 0;
        a.activate(x,y,vx,vy);
    }

    public void setGenerationTime(float generationTime) {
        this.generationTime = generationTime;
    }

    @Override
    protected Asteroid newObject() {
        return new Asteroid(game, mTextureRegion);
    }

    public boolean isStoped() {
        return isStoped;
    }

    public void setStoped(boolean stoped) {
        isStoped = stoped;
    }
}
