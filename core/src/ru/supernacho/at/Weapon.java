package ru.supernacho.at;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by SuperNacho on 08.10.2017.
 */

public class Weapon {
    public enum WeaponType{
        LASER(0), CANON(1), HEAVYLASER(2), BOTCANON(3), PLASMA_PU(4), BOTPLASMA(5), STARKAITER_MG(6);

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
    private WeaponType activeWeapon;


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
        this.activeWeapon = WeaponType.LASER;
    }

    public void setWeapon(WeaponType type){
        switch (type){
            case LASER:
                this.fireRate = 0.2f;
                this.currentFireRate = 0.0f;
                this.weaponDMG = 3;
                this.bulletVelocity = 1000.0f;
                this.bulletTexture = Assets.getInstances().atlas.findRegion("LaserShot");
                this.bulletSound = Assets.getInstances().laser;
                this.activeWeapon = WeaponType.LASER;
                break;
            case HEAVYLASER:
                this.fireRate = 0.3f;
                this.currentFireRate = 0.0f;
                this.weaponDMG = 7;
                this.bulletVelocity = 1000.0f;
                this.bulletTexture = Assets.getInstances().atlas.findRegion("heavylaser");
                this.bulletSound = Assets.getInstances().heavylaser;
                this.activeWeapon = WeaponType.HEAVYLASER;
                break;
            case CANON:
                this.fireRate = 0.8f;
                this.currentFireRate = 0.7f;
                this.weaponDMG = 10;
                this.bulletVelocity = 600.0f;
                this.bulletTexture = Assets.getInstances().atlas.findRegion("canonball");
                this.bulletSound = Assets.getInstances().canon;
                this.activeWeapon = WeaponType.CANON;
                break;
            case BOTCANON:
                this.fireRate = 2.0f;
                this.currentFireRate = 1.9f;
                this.weaponDMG = 20;
                this.bulletVelocity = 500.0f;
                this.bulletTexture = Assets.getInstances().atlas.findRegion("canonball");
                this.bulletSound = Assets.getInstances().canon;
                this.activeWeapon = WeaponType.BOTCANON;
                break;
            case PLASMA_PU:
                this.fireRate = 0.2f;
                this.currentFireRate = 0.0f;
                this.weaponDMG = 5;
                this.bulletVelocity = 500.0f;
                this.bulletTexture = Assets.getInstances().atlas.findRegion("plasma");
                this.bulletSound = Assets.getInstances().heavylaser;
                this.activeWeapon = WeaponType.PLASMA_PU;
                break;
            case BOTPLASMA:
                this.fireRate = 1.0f;
                this.currentFireRate = 0.0f;
                this.weaponDMG = 10;
                this.bulletVelocity = 500.0f;
                this.bulletTexture = Assets.getInstances().atlas.findRegion("plasma");
                this.bulletSound = Assets.getInstances().heavylaser;
                this.activeWeapon = WeaponType.BOTPLASMA;
                break;
            case STARKAITER_MG:
                this.fireRate = 0.8f;
                this.currentFireRate = 0.0f;
                this.weaponDMG = 1;
                this.bulletVelocity = 800.0f;
                this.bulletTexture = Assets.getInstances().atlas.findRegion("plasmamg");
                this.bulletSound = Assets.getInstances().laser;
                this.activeWeapon = WeaponType.STARKAITER_MG;
                break;
        }
    }

    public void pressFire(float dt, int objWidth, int objHeight, float bulletVx, float bulletVy, boolean isWeaponUp){
        currentFireRate += dt;
        if (currentFireRate > fireRate){
            currentFireRate -= fireRate;
            fire(objWidth, objHeight, bulletVx, bulletVy, isWeaponUp );
        }
    }

    public void fire(int objWidth, int objHeight,  float bulletVx, float bulletVy, boolean isWeaponUp) {
        if (isWeaponUp) {
            game.getBulletEmitter().setUp(ship.position.x + objWidth / 2,
                    ship.position.y + objHeight / 2, bulletVx, bulletVy, ship);
            game.getBulletEmitter().setUp(ship.position.x + objWidth / 2,
                    ship.position.y + objHeight / 2, bulletVx, bulletVx / 2, ship);
            game.getBulletEmitter().setUp(ship.position.x + objWidth / 2,
                    ship.position.y + objHeight / 2, bulletVx, -bulletVx / 2, ship);
        } else {
            game.getBulletEmitter().setUp(ship.position.x + objWidth / 2,
                    ship.position.y + objHeight / 2, bulletVx, bulletVy, ship);
        }
        System.out.println(activeWeapon);
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

    public WeaponType getActiveWeapon() {
        return activeWeapon;
    }
}
