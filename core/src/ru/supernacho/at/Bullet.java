package ru.supernacho.at;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by SuperNacho on 17.09.2017.
 */

public class Bullet implements Poolable{
    private TextureRegion mTexture;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private boolean playerSrc;
    private Ship ship;

    public Bullet() {
        this.position = new Vector2(0,0);
        this.velocity = new Vector2(0,0);
        this.active = false;
    }

    public void update(float dt){
        position.mulAdd(velocity,dt);
        if (position.x > AstroTour.SCREEN_WIDTH){
            deactivate();
        }

    }

    public void activate(float x, float y, float vx, float vy, boolean player, Ship ship){
        position.set(x,y);
        velocity.set(vx,vy);
        this.playerSrc = player;
        this.ship = ship;
        active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Ship getShip() {
        return ship;
    }

    public boolean isPlayer() {
        return playerSrc;
    }

    public TextureRegion getTexture() {
        return mTexture;
    }

    public void setTexture(TextureRegion texture) {
        mTexture = texture;
    }
}
