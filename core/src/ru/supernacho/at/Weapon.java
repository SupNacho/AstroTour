package ru.supernacho.at;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by SuperNacho on 08.10.2017.
 */

public class Weapon {
    public enum WeaponType{
        LASER(0), CANON(1);

        WeaponType(int index) {
            this.index = index;
        }

        private int index;

        public int getIndex(){
            return index;
        }
    }

    private GameScreen game;
    private Ship ship;
    private float fireRate;
    private float currentFireRate;
    private int weaponDMG;
    private float bulletVelocity;
    private TextureRegion bulletTexture;
    private Sound bulletSound;


    public Weapon(GameScreen game, Ship ship) {
        this.game = game;
        this.ship = ship;
        this.fireRate = 0.2f;
        this.currentFireRate = 0.0f;
        this.weaponDMG = 1;
        this.bulletVelocity = 1000.0f;
        Vector2 weaponDirection = new Vector2(1, 0);
        this.bulletTexture = Assets.getInstances().atlas.findRegion("LaserShot");
        this.bulletSound = Assets.getInstances().laser;
    }

    public void setWeapon(WeaponType type){
        switch (type){
            case LASER:
                this.fireRate = 0.2f;
                this.currentFireRate = 0.0f;
                this.weaponDMG = 1;
                this.bulletVelocity = 1000.0f;
                this.bulletTexture = Assets.getInstances().atlas.findRegion("LaserShot");
                this.bulletSound = Assets.getInstances().laser;
                break;
            case CANON:
                this.fireRate = 2.0f;
                this.currentFireRate = 1.9f;
                this.weaponDMG = 10;
                this.bulletVelocity = 500.0f;
                this.bulletTexture = Assets.getInstances().atlas.findRegion("canonball");
                this.bulletSound = Assets.getInstances().canon;
                break;
        }
    }

    public void pressFire(float dt, int objWidth, int objHeight, float bulletVx, float bulletVy){
        currentFireRate += dt;
        if (currentFireRate > fireRate){
            currentFireRate -= fireRate;
            fire(objWidth, objHeight, bulletVx, bulletVy );
        }
    }

    public void fire(int objWidth, int objHeight,  float bulletVx, float bulletVy) {
        game.getBulletEmitter().setUp(ship.position.x + objWidth /2,
                ship.position.y + objHeight/2,bulletVx, bulletVy, ship);
    }

    public float getBulletVelocity() {
        return bulletVelocity;
    }

    public Sound getSound() {
        return bulletSound;
    }

    public TextureRegion getBulletTexture() {
        return bulletTexture;
    }

    public int getWeaponDMG() {
        return weaponDMG;
    }
}
