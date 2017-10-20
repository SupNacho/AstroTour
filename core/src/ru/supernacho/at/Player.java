package ru.supernacho.at;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;


/**
 * Created by SuperNacho on 17.09.2017.
 */

public class Player extends Ship {
    private  TextureRegion mTexture;
    private  TextureRegion[] shipTextures;
    private  TextureRegion currentTexture;
    private  TextureRegion hpBgTexture;
    private  TextureRegion hpFillTexture;
    private  JoyStick mJoyStick;
    private  FireButton mFireButton;
    private  float shipRegen;
    private float distanceCompleteCnt;
    private  Vector2 plrTripVelocity;
    private int lives;
    private int scoreCount;
    private int money = 1000;
    private int weaponType;
    private int previosWeaponType;
    private  StringBuilder hudStringHelper;
    private  Sound gunSound;
    private float weaponUpTimer;
    private boolean isWeaponUp;
    private  boolean isDead;

    public Player(GameScreen game) {
        Assets as = Assets.getInstances();
        this.spsEngineThrotle = 1000;
        this.gunSound = as.laser;
        this.shipTextures = new TextureRegion[] {as.atlas.findRegion("spaceShip2"),
                as.atlas.findRegion("spaceShip2-05hp"),
                as.atlas.findRegion("spaceShip2-0hp")};
        this.objWidth = shipTextures[0].getRegionWidth();
        this.objHeight = shipTextures[0].getRegionHeight();
        this.mTexture = shipTextures[0];
        this.position = new Vector2(100, 360);
        this.game = game;
        setVelocity(0,0);
        this.hpMax = 250;
        this.hp = this.hpMax;
        this.shipRegen = 0;
        this.hpBgTexture = as.atlas.findRegion("hpBg");
        this.hpFillTexture = as.atlas.findRegion("hpFill");
        this.hitArea = new Circle(position, objHeight/2);
        this.plrTripVelocity = new Vector2(0,0);
        this.currentTexture = mTexture;
        this.lives = 3;
        this.hudStringHelper = new StringBuilder(50);
        this.scoreCount = 0;
        this.isPlayer = true;
        this.weapon = new Weapon(this.game, this);
        this.weaponType = GameData.getInstance().getPlayerWeaponType();
        this.weaponDirection = new Vector2(1.0f, 0);
        this.projectileVelocity = 1000;
        this.mJoyStick = new JoyStick(as.atlas.findRegion("joyfield"), as.atlas.findRegion("joystick"));
        this.mFireButton = new FireButton(as.atlas.findRegion("fireButt"), as.atlas.findRegion("fireButtPressed"));
        this.isDead = false;
    }

    public Player(GameScreen game, Sound gunSound, TextureRegion joyfield,  TextureRegion joystick,
                  TextureRegion fireButton, TextureRegion fireButtonPrsd,TextureRegion hpBg,
                  TextureRegion hpFg, TextureRegion[] texture,
                  float x, float y) {
        this.spsEngineThrotle = 500;
        this.gunSound = gunSound;
        this.shipTextures = texture;
        this.objWidth = texture[0].getRegionWidth();
        this.objHeight = texture[0].getRegionHeight();
        this.game = game;
        this.mTexture = texture[0];
        this.position = new Vector2(x,y);
        setVelocity(0,0);
        this.hpMax = 250;
        this.hp = this.hpMax;
        this.shipRegen = 0;
        this.hpBgTexture = hpBg;
        this.hpFillTexture = hpFg;
        this.hitArea = new Circle(position, objHeight/2);
        this.plrTripVelocity = new Vector2(0,0);
        this.currentTexture = mTexture;
        this.lives = 3;
        this.hudStringHelper = new StringBuilder(50);
        this.scoreCount = 0;
        this.isPlayer = true;
        this.weapon = new Weapon(game, this);
        this.weaponDirection = new Vector2(1.0f, 0);
        this.projectileVelocity = 1000;
        this.mJoyStick = new JoyStick(joyfield, joystick);
        this.mFireButton = new FireButton(fireButton, fireButtonPrsd);
        this.isDead = false;
    }


    @Override
    public void render(SpriteBatch batch){
        batch.draw(currentTexture, position.x - objWidth /2, position.y - objHeight /2,
                objWidth /2, objHeight /2, objWidth, objHeight,
                1,1, velocity.y / 25);
    }

    public void renderHUD(SpriteBatch batch, BitmapFont fnt, float x, float y){

        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(hpBgTexture, x + (int) (Math.random() * woundCNT * 10),
                y + (int) (Math.random() * woundCNT * 10));
        batch.draw(hpFillTexture,  x + (int) (Math.random() * woundCNT * 10),
                y + (int) (Math.random() * woundCNT * 10), (int) (hp / hpMax * 250), 16);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        batch.setColor(1,1,0, woundCNT);
        batch.draw(hpBgTexture, x + (int) (Math.random() * woundCNT * 25),
                y + (int) (Math.random() * woundCNT * 25));
        batch.draw(hpFillTexture,  x + (int) (Math.random() * woundCNT * 25),
                y + (int) (Math.random() * woundCNT * 25), (int) (hp / hpMax * 250), 16);
        batch.setColor(1,1,1,1);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        hudStringHelper.setLength(0);
        hudStringHelper.append("Score: ").append(String.valueOf(scoreCount));
        fnt.draw(batch, hudStringHelper, x, AstroTour.SCREEN_HEIGHT - 40.0f);
        hudStringHelper.setLength(0);
        hudStringHelper.append("Distance of tour: ").append(String.valueOf((int)distanceCompleteCnt));
        fnt.draw(batch, hudStringHelper, x, AstroTour.SCREEN_HEIGHT - 70.0f);
        hudStringHelper.setLength(0);
        hudStringHelper.append("Cash earned: ").append(String.valueOf(money));
        fnt.draw(batch, hudStringHelper, x, AstroTour.SCREEN_HEIGHT - 100.0f);
        hudStringHelper.setLength(0);
        hudStringHelper.append("x").append(lives);
        fnt.draw(batch, hudStringHelper, x + 255, AstroTour.SCREEN_HEIGHT - 15.0f);
        hudStringHelper.setLength(0);
        hudStringHelper.append("Level: ").append(game.getLevel());
        fnt.draw(batch, hudStringHelper, x, AstroTour.SCREEN_HEIGHT - 130.0f);

        mJoyStick.render(batch);
        mFireButton.render(batch);

    }



    @Override
    public void update(float dt){
        weaponUpProcessor(dt);

        if (this.hp < hpMax && this.hp > hpMax /2) {
            currentTexture = shipTextures[1];
        } else if (this.hp < hpMax / 2 && this.hp > 0){
            currentTexture = shipTextures[2];
        } else {
            currentTexture = mTexture;
        }

        if (this.hp < 250){
            this.hp += shipRegen;
            if (this.hp > 250) hp = 250;
            shipRegen = 0;
        }

        if (position.y < objWidth /2){
            if (velocity.y < 0 ){
                position.y = objWidth /2;
            }
        }
        if (position.y > AstroTour.SCREEN_HEIGHT - objWidth /2){
            if (velocity.y > 0) {
                position.y = AstroTour.SCREEN_HEIGHT - objWidth /2;
            }
        }
        if (position.x < objHeight /2){
            if (velocity.x < 0){
                position.x = objHeight /2;
            }
        }
        if (position.x > AstroTour.SCREEN_WIDTH - objHeight /2){
            if (velocity.x > 0){
                position.x = AstroTour.SCREEN_WIDTH - objHeight /2;
            }
        }

        mJoyStick.update(dt);
        if (mJoyStick.getPower() > 0.02f){
            velocity.x += spsEngineThrotle * dt * mJoyStick.getNorm().x * mJoyStick.getPower();
            velocity.y += spsEngineThrotle * dt * mJoyStick.getNorm().y * mJoyStick.getPower();
        }

        mFireButton.update();
        if (mFireButton.isTouched()){
            weapon.pressFire(dt, objWidth / 2, 0,
                    weaponDirection.x * weapon.getBulletVelocity(),
                    weaponDirection.y * weapon.getBulletVelocity(), isWeaponUp);
        }


//      Управление кораблем с клавиатуры
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            weapon.setWeapon(Weapon.WeaponType.LASER);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
            weapon.setWeapon(Weapon.WeaponType.CANON);

        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            velocity.x += spsEngineThrotle * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            velocity.x -= spsEngineThrotle * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            velocity.y += spsEngineThrotle * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            velocity.y -= spsEngineThrotle * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            weapon.pressFire(dt, objWidth, 0,
                    weaponDirection.x * weapon.getBulletVelocity(),
                    weaponDirection.y * weapon.getBulletVelocity(), isWeaponUp);
        }
        // Ускорение прохождения дистанции в зависимости от положения коробля по координате Х
        distanceCompleteCnt += (1 + plrTripVelocity.set(position.x, 0).dst(0,0)/200) * dt;

        woundCNT -= dt * 2.0f;
        if (woundCNT < 0.0f) woundCNT = 0.0f;
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);
        velocity.scl(0.90f);
        if (velocity.x > 0) {
            game.getParticleEmitter().setup(position.x - objWidth / 2 + 20, position.y + 20,
                    -MathUtils.random(5.0f, 10.0f), MathUtils.random(-10.0f, 10.0f), 0.2f, 1.5f, 0.5f,
                    1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.5f);
            game.getParticleEmitter().setup(position.x - objWidth / 2 + 20, position.y - 5,
                    -MathUtils.random(5.0f, 10.0f), MathUtils.random(-10.0f, 10.0f), 0.2f, 1.5f, 0.5f,
                    1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.5f);
        }
    }

    public void setWeaponUp(Weapon.WeaponType newWeapon){
        isWeaponUp = true;
        previosWeaponType = weapon.getActiveWeapon().getIndex();
        System.out.println("prev weapon - " + previosWeaponType);
        setWeaponType(newWeapon.getIndex());
    }

    private void weaponUpProcessor(float dt) {
        if (isWeaponUp) {
            if (weaponUpTimer < 10.0f) {
            weaponUpTimer += dt;
            } else {
                weaponUpTimer = 0.0f;
                isWeaponUp = false;
                setWeaponType(previosWeaponType);
            }

        }
    }

    public boolean isWeaponUp() {
        return isWeaponUp;
    }

    public void setVelocity(float x, float y) {
        this.velocity = new Vector2(x,y);
    }


    public int getSpsEngineThrotle() {
        return spsEngineThrotle;
    }

    public void setSpsEngineThrotle(int spsEngineThrotle) {
        this.spsEngineThrotle = spsEngineThrotle;
    }

    @Override
    public void onDestroy() {
        if (this.hp <=0 && lives != 0) {
            this.hp = hpMax;
            lives--;
        } else if (this.hp <=0 && lives == 0){

            game.getParticleEmitter().setup(position.x + MathUtils.random(-24, 24), position.y + MathUtils.random(-24, 24),
                    velocity.x + MathUtils.random(-24, 24), velocity.y + MathUtils.random(-24, 24), 0.6f,
                    6.0f, 1.5f,
                    0.50f, 0.50f, 0.0f, 7.0f, 0.0f, 0.0f, 0.0f, 0.1f);
            game.getParticleEmitter().setup(position.x + MathUtils.random(-24, 24), position.y + MathUtils.random(-24, 24),
                    velocity.x + MathUtils.random(-24, 24), velocity.y + MathUtils.random(-24, 24), 0.6f,
                    6.0f, 1.5f,
                    0.50f, 0.00f, 0.0f, 8.0f, 0.0f, 0.0f, 0.0f, 0.1f);
            game.getParticleEmitter().setup(position.x + MathUtils.random(-24, 24), position.y + MathUtils.random(-24, 24),
                    velocity.x + MathUtils.random(-24, 24), velocity.y + MathUtils.random(-24, 24), 0.6f,
                    6.0f, 1.5f,
                    0.50f, 0.50f, 0.0f, 7.0f, 0.0f, 0.0f, 0.0f, 0.1f);
            game.getParticleEmitter().setup(position.x + MathUtils.random(-24, 24), position.y + MathUtils.random(-24, 24),
                    velocity.x + MathUtils.random(-24, 24), velocity.y + MathUtils.random(-24, 24), 0.6f,
                    6.0f, 1.5f,
                    0.50f, 0.00f, 0.0f, 8.0f, 0.0f, 0.0f, 0.0f, 0.1f);
            game.getParticleEmitter().setup(position.x, position.y,
                    velocity.x, velocity.y, 0.6f,
                    6.0f, 1.5f,
                    0.50f, 0.50f, 0.50f, 8.0f, 0.0f, 0.0f, 0.0f, 0.1f);

            GameData.getInstance().setData(scoreCount, money, distanceCompleteCnt, game.getLevel(), hp, hpMax, lives);

            isDead = true;
        }
    }

    public void setShipRegen(float shipRegen) {
        this.shipRegen = shipRegen;
    }

    public float getDistanceCompleteCnt() {
        return distanceCompleteCnt;
    }

    public void setScoreCount(int scoreCount) {
        this.scoreCount += scoreCount;
    }

    public void addMoney(int money) {
        this.money += money;
    }

    public int getScoreCount() {
        return scoreCount;
    }

    public int getMoney() {
        return money;
    }
    public float getHp() {
        return this.hp;
    }

    public int getLives() {
        return lives;
    }

    public void setDistanceCompleteCnt(float distanceCompleteCnt) {
        this.distanceCompleteCnt = distanceCompleteCnt;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(int weaponType) {
        weapon.setWeapon(Weapon.WeaponType.values()[weaponType]);
    }


    public void setHp(float hp) {
        this.hp = hp;
    }
    public void loadPlayer(String hp, String distance, String score, String lives, String money, String weaponType) {
        this.hp = Float.parseFloat(hp);
        this.distanceCompleteCnt = Float.parseFloat(distance);
        this.scoreCount = Integer.parseInt(score);
        this.lives = Integer.parseInt(lives);
        this.money = Integer.parseInt(money);
        this.weaponType = Integer.parseInt(weaponType);
        setWeaponType(this.weaponType);
    }

    public boolean isDead() {
        return isDead;
    }

}
