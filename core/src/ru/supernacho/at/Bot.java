package ru.supernacho.at;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by SuperNacho on 29.09.2017.
 */

public class Bot extends Ship implements Poolable {

    private int vy;
    private static final int baseHP = 10;

    public Bot(GameScreen game, TextureRegion texture) {
        this.game = game;
        this.mTexture = texture;
        this.objWidth = mTexture.getRegionWidth();
        this.objHeight = mTexture.getRegionHeight();
        this.position = new Vector2(0,0);
        this.velocity = new Vector2(0,0);
        this.hitArea = new Circle(0,0,64);
        this.currentFire = 0.0f;
        this.fireRate = 2.0f;
        this.weaponDirection = new Vector2(0,0);
        this.weapon = new Weapon(game, this);
        int weaponType = Integer.parseInt(this.mTexture.toString().substring(7));
        switch (weaponType){
            case 1:
                this.weapon.setWeapon(Weapon.WeaponType.BOTCANON);
                break;
            case 2:
                this.weapon.setWeapon(Weapon.WeaponType.BOTPLASMA);
                break;
            case 3:
                this.weapon.setWeapon(Weapon.WeaponType.BOTPLASMA);
                break;
            case 4:
                this.weapon.setWeapon(Weapon.WeaponType.BOTCANON);
                break;
            case 5:
                this.weapon.setWeapon(Weapon.WeaponType.BOTPLASMA);
                break;
            case 6:
                this.weapon.setWeapon(Weapon.WeaponType.BOTCANON);
                break;
            default:
                throw new RuntimeException("Uncnown bot weaponType in create case");
        }

        this.vy = 50;
        this.projectileVelocity = 500;
        this.isPlayer = false;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (woundCNT > 0.01f){
            batch.setColor(1.0f, 1.0f - woundCNT, 1.0f - woundCNT, 1.0f);
        }
        batch.draw(mTexture, position.x - objWidth/2, position.y - objHeight/2);
        if (woundCNT > 0.01f) {
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    @Override
    public void update(float dt) {
        velocity.y += vy *dt;
        if (this.velocity.y > 200) {
            vy = -200;
        }
        else if (this.velocity.y < -200) {
            vy = 200;
        }

        if (position.x - game.getPlayer().getPosition().x < 800){
            weaponDirection.set(game.getPlayer().getPosition().x + MathUtils.random(-64, 64),
                    game.getPlayer().getPosition().y + MathUtils.random(-64, 64));
            weaponDirection.sub(this.position).nor().scl(weapon.getBulletVelocity());
            weapon.pressFire(dt, 0, 0, weaponDirection.x, weaponDirection.y, false);
        }
        position.mulAdd(velocity, dt);
        if (position.x < -hitArea.radius*2){
            deactivate();
        }
        if (position.y < -hitArea.radius*2){
            deactivate();
        }
        if (position.y > AstroTour.SCREEN_HEIGHT + hitArea.radius*2){
            deactivate();
        }
        if (position.x > AstroTour.SCREEN_WIDTH + hitArea.radius*2 && this.velocity.x > 0){
            deactivate();
        }
        hitArea.setPosition(position);
        woundCNT -= dt * 0.2f;
        if (woundCNT < 0.0f) woundCNT = 0.0f;
    }

    public void activate(float x, float y){
        this.position.set(x,y);
        this.velocity.set(-MathUtils.random(30, 140), 0);
        this.hpMax = baseHP + game.getLevel();
        this.hp = this.hpMax;
        this.woundCNT = 0.0f;
        active = true;
    }

    @Override
    public void onDestroy() {
        deactivate();

    }
}