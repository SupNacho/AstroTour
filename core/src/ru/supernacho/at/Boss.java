package ru.supernacho.at;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuperNacho on 05.11.2017.
 */

public class Boss extends Ship implements Poolable{
    private final TextureAtlas atlas;
    private TextureRegion hpFillTexture;
    private TextureRegion hpBgTexture;
    private TextureRegion bossEyeTexture;
    private List<BossEye> eyes;
    private int vy;
    private int vx;
    private boolean rageQuit;
    private boolean playerLocked;
    private boolean rageAttakStarted;
    private Vector2 playerDirection;
    private Vector2 playerPosition;
    private Player player;

    Boss(GameScreen game, TextureRegion texture){
        this.mTexture = texture;
        eyes = new ArrayList();
        atlas = Assets.getInstances().atlas;
        this.hpFillTexture = atlas.findRegion("hpFill");
        this.hpBgTexture = atlas.findRegion("hpBg");
        this.bossEyeTexture = atlas.findRegion("bossEye");
        this.game = game;
        this.objHeight = mTexture.getRegionHeight();
        this.objWidth = mTexture.getRegionWidth();
        this.hpMax = 500;
        this.hp = hpMax;
        this.position = new Vector2(AstroTour.SCREEN_WIDTH + 200, AstroTour.SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0,0);
        this.hitArea = new Circle(this.position, this.objWidth/2);
        this.isPlayer = false;
        this.vy = 50;
        this.vx = 50;
        this.playerDirection = new Vector2(0,0);
        this.playerPosition = new Vector2(0,0);
        this.rageQuit = false;
        this.playerLocked = false;
        this.rageAttakStarted = false;
        this.player = game.getPlayer();
        eyes.add(new BossEye(game, bossEyeTexture, this, new Vector2(-15, 80)));
        eyes.add(new BossEye(game, bossEyeTexture, this, new Vector2(-15, -15)));
        eyes.add(new BossEye(game, bossEyeTexture, this, new Vector2(-15, -117)));
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(mTexture, position.x - objWidth/2, position.y - objHeight/2);
        for (int i = 0; i < eyes.size(); i++) {
            eyes.get(i).render(batch);
        }
        renderHUD(batch, AstroTour.SCREEN_WIDTH/2 - 250, AstroTour.SCREEN_HEIGHT - 32);
    }

    public void renderHUD(SpriteBatch batch, float x, float y){
        batch.draw(hpBgTexture, x, y);
        batch.draw(hpFillTexture,  x, y, (int) (hp / hpMax * 250), 16);
    }

    @Override
    public void update(float dt) {
// TODO: 10.11.2017 boss Ai

        for (int i = 0; i < eyes.size(); i++) {
            eyes.get(i).update(dt);
        }
        if (position.y > AstroTour.SCREEN_HEIGHT - objHeight/2 && !rageQuit){
            position.y = AstroTour.SCREEN_HEIGHT - objHeight/2;
            vy = -50;
        } else if (position.y < objHeight/2 && !rageQuit){
            position.y = objHeight/2;
            vy = 50;
        }

        if (position.x > AstroTour.SCREEN_WIDTH - objWidth/2 && !rageQuit){
            position.x = AstroTour.SCREEN_WIDTH - objWidth/2;
            vx = -50;
        } else if (position.x < AstroTour.SCREEN_WIDTH/2 && !rageQuit) {
            vx = 50;
        }
        if (rageQuit){
            if (!playerLocked && !rageAttakStarted){
                playerPosition.set(player.getPosition());
                playerLocked = true;
            } else if (playerLocked && !rageAttakStarted) {
                playerDirection.set(playerPosition);
                playerDirection.sub(position).nor().scl(300);
                velocity.set(playerDirection);
                playerLocked = false;
                rageAttakStarted = true;
            } else if (rageAttakStarted){
                position.mulAdd(velocity, dt);
                if (hitArea.contains(playerPosition)){
                    rageAttakStarted = false;
                    rageQuit = false;
                }
            }
        }
        if (!rageAttakStarted) {
            position.mulAdd(velocity, dt);
            velocity.y = vy;
            velocity.x = vx;
        }
        hitArea.setPosition(position);
    }


    public void activate(float x, float y, int lvl){
        this.position.set(x,y);
        this.velocity.set(-20, 0);
        this.hpMax = 500 * lvl;
        this.hp = hpMax;
        active = true;
        for (int i = 0; i < eyes.size(); i++) {
            eyes.get(i).activate(x, y);
        }
    }

    public List<BossEye> getEyes() {
        return eyes;
    }

    @Override
    public void onDestroy() {
        deactivate();
        game.setBossFighting(false);
        game.setBossDefeated(true);
        game.setBossReady(false);
        float dst = player.getDistanceCompleteCnt();
        player.setDistanceCompleteCnt(dst + 1);
        player.setMoney(player.getMoney() + 300);

    }

    public boolean isRageQuit() {
        return rageQuit;
    }

    public void setRageQuit(boolean rageQuit) {
        this.rageQuit = rageQuit;
    }
}
