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

}
