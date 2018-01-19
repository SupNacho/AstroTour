package ru.supernacho.at;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by SuperNacho on 14.11.2017.
 */

public class BossEye extends Ship{
    private TextureRegion texture;
    private TextureRegion hpFillTexture;
    private TextureRegion hpBgTexture;
    private GameScreen game;
    private Boss boss;
    private Vector2 dPosition;
    private Vector2 hitAreaPosition;
    private final TextureAtlas atlas;

    BossEye(GameScreen game, TextureRegion texture, Boss boss, Vector2 deltaPos){
        this.game = game;
        this.texture = texture;
        atlas = Assets.getInstances().atlas;
        this.hpFillTexture = atlas.findRegion("hpFill");
        this.hpBgTexture = atlas.findRegion("hpBg");
        this.boss = boss;
        this.dPosition = deltaPos;
        this.objWidth = texture.getRegionWidth();
        this.objHeight = texture.getRegionHeight();
        this.position = new Vector2(0,0);
        this.velocity = new Vector2(0,0);
        this.hitAreaPosition = new Vector2(0,0);
        this.hitArea = new Circle(position, objWidth/2);
        this.hpMax = 100;
        this.hp = this.hpMax;
        this.weaponDirection = new Vector2(0,0);
        this.weapon = new Weapon(game, this);
        this.weapon.setWeapon(Weapon.WeaponType.BOTCANON);
    }

    public void activate(float x, float y){
        this.hpMax = 100;
        this.hp = hpMax;
        active = true;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
        batch.draw(hpBgTexture, position.x, position.y - 8, 32, 5);
        batch.draw(hpFillTexture, position.x, position.y - 8, (int)(hp / hpMax * 32), 5);

    }

    @Override
    public void update(float dt) {
        position.x = boss.position.x + dPosition.x;
        position.y = boss.position.y + dPosition.y;
        hitAreaPosition.x = position.x + objWidth/2;
        hitAreaPosition.y = position.y + objHeight/2;
        hitArea.setPosition(hitAreaPosition);
        shootThePlayer(dt);
    }


    private void shootThePlayer(float dt) {
        weaponDirection.set(game.getPlayer().getPosition().x + MathUtils.random(-64, 64),
                game.getPlayer().getPosition().y + MathUtils.random(-64, 64));
        weaponDirection.sub(this.position).nor().scl(weapon.getBulletVelocity());
        weapon.pressFire(dt, 0, 0, weaponDirection.x, weaponDirection.y, false);
    }

    @Override
    public void onDestroy() {
        deactivate();
        boss.getEyes().remove(boss.getEyes().indexOf(this));
        boss.takeDamage(150 * game.getLevel());
        boss.setRageQuit(true);
    }

}
