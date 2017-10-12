package ru.supernacho.at;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by SuperNacho on 01.10.2017.
 */

public abstract class Ship extends SpaceObject {

    protected int spsEngineThrotle;
    protected float currentFire;
    protected float fireRate;
    protected boolean isPlayer;
    protected Vector2 weaponDirection;
    protected int projectileVelocity;
    protected Weapon weapon;

//    public void pressFire(float dt, int objWidth, int objHeight, float bulletVx, float bulletVy){
//        currentFire += dt;
//        if (currentFire > fireRate){
//            currentFire -= fireRate;
//            fire(objWidth, objHeight, bulletVx, bulletVy );
//        }
//    }
//
//    public void fire(int objWidth, int objHeight,  float bulletVx, float bulletVy) {
//        game.getBulletEmitter().setUp(position.x + objWidth /2,
//                position.y + objHeight/2,bulletVx, bulletVy, isPlayer);
//        if(!isPlayer){
//            Assets.getInstances().canon.play();
//        }
//    }

}
