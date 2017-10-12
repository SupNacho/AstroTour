package ru.supernacho.at;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by SuperNacho on 01.10.2017.
 */

public abstract class SpaceObject {
    protected GameScreen game;
    protected TextureRegion mTexture;
    protected Vector2 position;
    protected Vector2 velocity;
    protected float hp;
    protected float hpMax;
    protected float woundCNT;
    protected Circle hitArea;
    protected int objWidth;
    protected int objHeight;
    protected boolean active;



    public abstract void render(SpriteBatch batch);

    public abstract void update(float dt);

    public Circle getHitArea() {
        return hitArea;
    }

    public void setVelocity(float x, float y) {
        this.velocity = new Vector2(x,y);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public abstract void onDestroy();

    public boolean takeDamage(float dmg){
        this.hp -= dmg;
        this.woundCNT += 0.2f;
        if (woundCNT > 1.0f) this.woundCNT = 1.0f;
        if (this.hp <=0 ) {
            onDestroy();
            return true;
        }
        return false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getHpMax() {
        return hpMax;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }
}
