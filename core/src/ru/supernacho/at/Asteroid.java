package ru.supernacho.at;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by SuperNacho on 17.09.2017.
 */

public class Asteroid extends SpaceObject implements Poolable {
    public static final float MIN_SPD = 120.0f;
    public static final float MAX_SPD = 360.0f;
    private static final float BASE_HP = 10.0f;
    private static final int ASTEROID_SIZE = 256;

    private float scale;
    private float rotation;
    private float angularSpeed;


    public Asteroid(GameScreen game, TextureRegion texture) {
        this.game = game;
        this.mTexture = texture;
        this.position = new Vector2(MathUtils.random(AstroTour.SCREEN_WIDTH, AstroTour.SCREEN_WIDTH * 2),
                MathUtils.random(0, AstroTour.SCREEN_HEIGHT));
        this.velocity = new Vector2(-MathUtils.random(MIN_SPD, MAX_SPD),0);
        this.scale = MathUtils.random(0.1f, 1.2f);
        this.rotation = MathUtils.random(0.0f, 360.0f);
        this.angularSpeed = MathUtils.random(-45f, 45f);
        this.hpMax = (BASE_HP * scale) + game.getPlayer().getDistanceCompleteCnt()/100 ; // Рост хп астеройдов по мере продолжительности полета игрока
        this.hp = hpMax;
        this.hitArea = new Circle(this.position.x, this.position.y, ASTEROID_SIZE/2 * this.scale);
        this.woundCNT = 0.0f;
        this.objWidth = ASTEROID_SIZE; //mTexture.getRegionWidth();
        this.objHeight = ASTEROID_SIZE; //mTexture.getRegionHeight();
        this.active = false;
    }




    public float getWounds() {
        return woundCNT;
    }

    public void update(float dt){
        rotation += angularSpeed * dt;
        position.mulAdd(velocity, dt);
        if (position.x < -ASTEROID_SIZE){
            deactivate();
        }
        if (position.y < -ASTEROID_SIZE){
            deactivate();
        }
        if (position.y > AstroTour.SCREEN_HEIGHT + ASTEROID_SIZE){
            deactivate();
        }
        if (position.x > AstroTour.SCREEN_WIDTH + ASTEROID_SIZE && this.velocity.x > 0){
            deactivate();
        }
        hitArea.setPosition(position);
        woundCNT -= dt * 0.2f;
        if (woundCNT < 0.0f) woundCNT = 0.0f;

    }


    public int getAsteroidSize() {
        return ASTEROID_SIZE;
    }

    public float getRotation() {
        return rotation;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (woundCNT > 0.01f){
            batch.setColor(1.0f, 1.0f - woundCNT, 1.0f - woundCNT, 1.0f);
        }
        batch.draw(mTexture, position.x - ASTEROID_SIZE/2, position.y - ASTEROID_SIZE/2, ASTEROID_SIZE/2, ASTEROID_SIZE/2,
                ASTEROID_SIZE, ASTEROID_SIZE, scale, scale, rotation);
        if (woundCNT > 0.01f) {
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    @Override
    public void onDestroy() {
        deactivate();
        game.getParticleEmitter().setup(getPosition().x + MathUtils.random(-24, 24), getPosition().y + MathUtils.random(-24, 24),
                getVelocity().x + MathUtils.random(-24, 24), getVelocity().y + MathUtils.random(-24, 24), 0.6f,
                6.0f, 1.5f,
                0.50f, 0.50f, 0.0f, 7.0f, 0.0f, 0.0f, 0.0f, 0.1f);
        game.getParticleEmitter().setup(getPosition().x + MathUtils.random(-24, 24), getPosition().y + MathUtils.random(-24, 24),
                getVelocity().x + MathUtils.random(-24, 24), getVelocity().y + MathUtils.random(-24, 24), 0.6f,
                6.0f, 1.5f,
                0.50f, 0.00f, 0.0f, 8.0f, 0.0f, 0.0f, 0.0f, 0.1f);
        game.getParticleEmitter().setup(getPosition().x + MathUtils.random(-24, 24), getPosition().y + MathUtils.random(-24, 24),
                getVelocity().x + MathUtils.random(-24, 24), getVelocity().y + MathUtils.random(-24, 24), 0.6f,
                6.0f, 1.5f,
                0.50f, 0.50f, 0.0f, 7.0f, 0.0f, 0.0f, 0.0f, 0.1f);
        game.getParticleEmitter().setup(getPosition().x + MathUtils.random(-24, 24), getPosition().y + MathUtils.random(-24, 24),
                getVelocity().x + MathUtils.random(-24, 24), getVelocity().y + MathUtils.random(-24, 24), 0.6f,
                6.0f, 1.5f,
                0.50f, 0.00f, 0.0f, 8.0f, 0.0f, 0.0f, 0.0f, 0.1f);
        game.getParticleEmitter().setup(getPosition().x, getPosition().y,
                getVelocity().x, getVelocity().y, 0.6f,
                6.0f, 1.5f,
                0.50f, 0.50f, 0.50f, 8.0f, 0.0f, 0.0f, 0.0f, 0.1f);

    }

    public TextureRegion getTexture() {
        return mTexture;
    }


    public float getScale() {

        return scale;
    }

    public void activate(float x, float y, float vx, float vy){
        position.set(x, y);
        velocity.set(vx, vy);

        this.scale = MathUtils.random(0.1f , 0.8f);
        this.rotation = MathUtils.random(0.0f, 360.0f);
        this.hpMax = (BASE_HP * this.scale) + game.getPlayer().getDistanceCompleteCnt()/100; // Рост хп астеройдов в зависимости от пройденого расстояния
        this.hp = this.hpMax;
        this.hitArea.radius = ASTEROID_SIZE/2 * this.scale;
        this.woundCNT = 0.0f;

        this.active = true;
    }



}
