package ru.supernacho.at;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by SuperNacho on 24.10.2017.
 */

public class Drone extends Ship {

    private TextureRegion texture;
    private boolean isActive;
    private boolean targetLocked;
    private float rocketEngen;
    private Bot target;

    public Drone(GameScreen game) {
        this.texture = Assets.getInstances().atlas.findRegion("starkaiter");
        this.rocketEngen = 500;
        this.objWidth = texture.getRegionWidth();
        this.objHeight = texture.getRegionHeight();
        this.isActive = false;
        this.targetLocked = false;
        this.position = new Vector2(100,360);
        this.isPlayer = true;
        this.weapon = new Weapon(game, this);
        this.game = game;
        this.velocity = new Vector2(0,0);
        this.weapon.setWeapon(Weapon.WeaponType.STARKAITER_MG);
        this.weaponDirection = new Vector2(0,0);
    }

    @Override
    public void onDestroy() {
        isActive = false;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (isActive) {
            batch.draw(texture, position.x - objWidth / 2, position.y - objHeight / 2,
                    objWidth / 2, objHeight / 2, objWidth, objHeight,
                    1, 1, velocity.y / 25);
        }
    }

    @Override
    public void update(float dt) {
        if (isActive) {
            if ((game.getPlayer().getPosition().x - (game.getPlayer().objWidth / 2 + 20)) - position.x > 100) {
                velocity.x += rocketEngen * dt;
            }
            if (position.x - (game.getPlayer().getPosition().x - (game.getPlayer().objWidth / 2 + 20)) > 10) {
                velocity.x -= rocketEngen * dt;
            }
            if (game.getPlayer().getPosition().y - position.y > 100) {
                velocity.y += rocketEngen * dt;
            }
            if (position.y - game.getPlayer().getPosition().y > 100) {
                velocity.y -= rocketEngen * dt;
            }

            game.getParticleEmitter().setup(position.x, position.y,
                    -MathUtils.random(5.0f, 10.0f), MathUtils.random(-10.0f, 10.0f), 0.2f, 1.0f, 0.3f,
                    1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.5f);


            position.mulAdd(velocity, dt);
            velocity.scl(0.99f);
        }
    }

    public void setTarget(Bot target, float dt){
            weaponDirection.set(target.getPosition().x + MathUtils.random(-64, 64),
                    target.getPosition().y + MathUtils.random(-64, 64));
            weaponDirection.sub(this.position).nor().scl(weapon.getBulletVelocity());
            weapon.pressFire(dt, 0, 0, weaponDirection.x, weaponDirection.y, false);
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    public void activate() {
        isActive = true;
        position.x = game.getPlayer().getPosition().x  - (game.getPlayer().objWidth/2 + 20);
        position.y = game.getPlayer().getPosition().y;
        System.out.println("Drone Activate");
    }
    public void deactivate() {
        if (Math.abs(game.getPlayer().getPosition().x
                - position.x) < 50 && Math.abs(game.getPlayer().getPosition().y - position.y) < 50) {
            isActive = false;
            System.out.println("Drone Deactivate");
        }
    }
}
