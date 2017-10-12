package ru.supernacho.at;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 * Created by SuperNacho on 21.09.2017.
 */

public class BulletEmitter extends ObjectPool<Bullet> {

    private TextureRegion bulletTexture;
    private TextureRegion botBulletTexture;
    private TextureRegion currentBullet;
    private Player player;

    public BulletEmitter(TextureRegion bulletTexture, TextureRegion botBullet, int size, Player player){
        super();
        this.player = player;
        this.bulletTexture = bulletTexture;
        this.botBulletTexture = botBullet;
        for (int i = 0; i < size; i++) {
            freeList.add(newObject());
        }
    }

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

    public void update(float dt){
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
//        System.out.println("All bullets: " + (activeList.size() + freeList.size()));
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            Bullet bullet = activeList.get(i);
            batch.draw(bullet.getTexture(), bullet.getPosition().x - bullet.getTexture().getRegionWidth()/2,
                    bullet.getPosition().y - bullet.getTexture().getRegionHeight()/2);
        }
    }

    public void setUp(float x, float y, float vx, float vy, Ship ship){
            Bullet b = getActiveElement();
            b.setTexture(ship.weapon.getBulletTexture());
            ship.weapon.getSound().play(0.05f * AstroTour.soundVolume);
            b.activate(x, y, vx, vy, ship.isPlayer, ship);
    }
}
