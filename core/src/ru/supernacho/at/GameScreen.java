package ru.supernacho.at;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;



/**
 * Created by SuperNacho on 01.10.2017.
 */

public class GameScreen implements Screen {

    private float astGenerationTime = 2.0f;
    private float botGenerationTime = 5.0f;
    private int lvlPlanet;
    private float planetScale;
    private Vector2 planetBotsPos;
    private float planetBotsYdelta;
    private float planetBotsScale;
    private AstroTour mGame;
    private SpriteBatch batch;
    private Player mPlayer;
    private BitmapFont font;
    private int level;
    private float chkTimer = 0.0f;
    private float chkPauseTimer = 0.0f;
    private float chkSoundTimer = 0.0f;
    private float shopTimer = 0.0f;
    private float shopActivationDelay = 3.0f;
    private float distancePerLvl;
    private Rectangle musicRect;
    private Rectangle soundRect;
    private Rectangle pauseRect;
    private boolean isPaused;
    private float dtTemp;
    private MyInputProcessor mip;
    private Music music;
    private Music bossMusic;

    private TextureRegion planet;
    private Vector2 planetPos;
    private Vector2 planetBotsVelocity;
    private float timer = 0.0f;


    private AsteroidEmitter asteroidEmitter;
    private static final int ASTEROIDS_CNT = 30;


    private Background background;
    private BulletEmitter bulletEmitter;
    private PowerUpsEmitter powerUpsEmitter;
    private ParticleEmitter particleEmitter;
    private BossEmitter bossEmitter;
    private BotEmitter botEmitter;
    private TextureRegion sndButton;
    private TextureRegion sndButtonPrsd;
    private TextureRegion sndButtonCurr;
    private TextureRegion mscButton;
    private TextureRegion mscButtonPrsd;
    private TextureRegion mscButtonCurr;
    private TextureRegion pausePlay;
    private TextureRegion pausePlayPrsd;
    private TextureRegion switchScreen;
    private TextureRegion lvlComplete;
    private TextureRegion planetBotTexture;
    private Sound explosion;
    private Sound bulletHit;
    private final Vector2 collisionHelper = new Vector2(0,0);
    private TextureRegion pausePlayCurr;

    private boolean shopReady = false;
    private boolean bossReady = false;
    private boolean bossFighting = false;
    private boolean bossDefeated = false;
    private Music lvlDoneMuscic;

    public GameScreen(AstroTour game, SpriteBatch batch){
        this.mGame = game;
        this.batch = batch;
    }


    @Override
    public void show() {
        Assets as = Assets.getInstances();
        mip = (MyInputProcessor) Gdx.input.getInputProcessor();

        background = new Background(as.atlas);
        TextureRegion botShot = as.atlas.findRegion("canonball");
        TextureRegion astTex = as.atlas.findRegion("asteroid");
        TextureRegion laserShot = as.atlas.findRegion("LaserShot");

//        planet = as.atlas.findRegion("planet"+MathUtils.random(1,6));
//        String planetType = planet.toString().substring(6);


        bulletHit = as.bulletHit;
        font = as.assetManager.get("astrotour.fnt", BitmapFont.class);

        mPlayer = new Player(this);
//        botEmitter = new BotEmitter(this, mPlayer, as.atlas.findRegion("botShip"+planetType), 20, botGenerationTime);
        powerUpsEmitter = new PowerUpsEmitter(this, as.atlas);
        particleEmitter = new ParticleEmitter(as.atlas.findRegion("star16"), 200);
        asteroidEmitter = new AsteroidEmitter(this, astTex, ASTEROIDS_CNT, astGenerationTime);
        bulletEmitter = new BulletEmitter(laserShot, botShot, 100, mPlayer);
        bossEmitter = new BossEmitter(this, 1);
        pauseRect = new Rectangle(AstroTour.SCREEN_WIDTH - 55, AstroTour.SCREEN_HEIGHT - 55, 50, 50);
        soundRect = new Rectangle(AstroTour.SCREEN_WIDTH - 110, AstroTour.SCREEN_HEIGHT - 55, 50, 50);
        musicRect = new Rectangle(AstroTour.SCREEN_WIDTH - 165, AstroTour.SCREEN_HEIGHT - 55, 50, 50);

        pausePlay = as.atlas.findRegion("ppause");
        pausePlayPrsd = as.atlas.findRegion("pplay");
        pausePlayCurr = pausePlay;
        sndButton = as.atlas.findRegion("sound1");
        sndButtonPrsd = as.atlas.findRegion("sound2");
        switchScreen = as.atlas.findRegion("bg");
        lvlComplete = as.atlas.findRegion("lvlComplete");

        if ( AstroTour.soundVolume == 1) {
            sndButtonCurr = sndButton;
        } else {
            sndButtonCurr = sndButtonPrsd;
        }
        mscButton = as.atlas.findRegion("music");
        mscButtonPrsd = as.atlas.findRegion("music2");
        if (AstroTour.musicVolume == 1) {
            mscButtonCurr = mscButton;
        } else{
            mscButtonCurr = mscButtonPrsd;
        }

        dtTemp = 0;
        level = 1;
        distancePerLvl = 20.0f;
        bossFighting = false;

        if (AstroTour.isSavedGame && GameData.getInstance().loadPlayerProgress(this, mPlayer)) {

        } else {
            mPlayer.setWeaponType(0);
        }
        if (level > 6){
            lvlPlanet = level%6+1;
        } else {
            lvlPlanet = level;
        }

        planetScale = MathUtils.random(0.6f, 1.0f);
        planet = as.atlas.findRegion("planet"+lvlPlanet);
        botEmitter = new BotEmitter(this, mPlayer, as.atlas.findRegion("botShip"+lvlPlanet), 20, botGenerationTime);
        planetPos = new Vector2(AstroTour.SCREEN_WIDTH,
                MathUtils.random(0 - planet.getRegionHeight()/2, AstroTour.SCREEN_HEIGHT - planet.getRegionHeight()/2));
        planetBotsPos = new Vector2(planetPos.x, planetPos.y);
        planetBotTexture = as.atlas.findRegion("botShip"+lvlPlanet);
        planetBotsVelocity = new Vector2(10,0);

        isPaused = false;
        explosion = as.explosion;
        lvlDoneMuscic = as.lvlDone;
        bossMusic = as.bossMusic;
        music = as.gameMusic;
        music.setLooping(true);
        music.play();
    }

    @Override
    public void render(float delta) {
        dtTemp = delta;
        float dt;
        if (!isPaused){
            dt = dtTemp;
            pausePlayCurr = pausePlay;
        } else {
            dt = 0;
            pausePlayCurr = pausePlayPrsd;
        }
        update(dt);
        timer += dt;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(mGame.getCamera().combined);
        batch.begin();
        background.render(batch);
        planetView();
        bossEmitter.render(batch);
        asteroidEmitter.render(batch);
        botEmitter.render(batch);
        powerUpsEmitter.render(batch);
        particleEmitter.render(batch);
        mPlayer.render(batch);
        bulletEmitter.render(batch);
        batch.draw(pausePlayCurr,pauseRect.x, pauseRect.y);
        batch.draw(sndButtonCurr,soundRect.x, soundRect.y);
        batch.draw(mscButtonCurr,musicRect.x, musicRect.y);
        mPlayer.renderHUD(batch, font, 50, AstroTour.SCREEN_HEIGHT - 32);
        if (bossDefeated){
            lvlDoneMuscic.setVolume((1.0f - shopTimer / shopActivationDelay) * AstroTour.musicVolume);
            batch.draw(lvlComplete, AstroTour.SCREEN_WIDTH / 2 - lvlComplete.getRegionWidth() / 2,
                    AstroTour.SCREEN_HEIGHT / 2 - lvlComplete.getRegionHeight() / 2);
            batch.setColor(1.0f, 1.0f, 1.0f, shopTimer / shopActivationDelay);
            batch.draw(switchScreen, 0, 0, AstroTour.SCREEN_WIDTH, AstroTour.SCREEN_HEIGHT);
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        batch.end();
    }

    private void planetView() {
        batch.draw(planet, planetPos.x, planetPos.y, planet.getRegionWidth() * planetScale,
                planet.getRegionHeight() * planetScale);
        if (!shopReady) {
            float rotation = 180.0f;
            batch.draw(planetBotTexture, planetBotsPos.x, planetBotsPos.y + planetBotsYdelta,
                    (float) planetBotTexture.getRegionWidth() / 2, (float) planetBotTexture.getRegionHeight() / 2,
                    (float) planetBotTexture.getRegionWidth(),
                    (float) planetBotTexture.getRegionHeight(), planetBotsScale, planetBotsScale, rotation);
            batch.draw(planetBotTexture, planetBotsPos.x, planetBotsPos.y,
                    (float) planetBotTexture.getRegionWidth() / 2, (float) planetBotTexture.getRegionHeight() / 2,
                    (float) planetBotTexture.getRegionWidth(),
                    (float) planetBotTexture.getRegionHeight(), planetBotsScale, planetBotsScale, rotation);
            batch.draw(planetBotTexture, planetBotsPos.x, planetBotsPos.y - planetBotsYdelta,
                    (float) planetBotTexture.getRegionWidth() / 2, (float) planetBotTexture.getRegionHeight() / 2,
                    (float) planetBotTexture.getRegionWidth(),
                    (float) planetBotTexture.getRegionHeight(), planetBotsScale, planetBotsScale, rotation);
        }
    }

    private void planetAnimation(float dt){
        if (planetBotsPos.x - planetPos.x > AstroTour.SCREEN_WIDTH) {
            planetBotsPos.set(planetPos.x + (planet.getRegionWidth()/2 * planetScale),
                    planetPos.y + (planet.getRegionHeight()/2) * planetScale);
            planetBotsScale = 0.1f;
            planetBotsYdelta = 0.0f;
            planetBotsVelocity.x = 10.0f;
        }

        if (planetPos.y > AstroTour.SCREEN_HEIGHT / 2){
            planetBotsPos.y -= 50 * dt;
        } else {
            planetBotsPos.y += 50 * dt;
        }
        planetBotsVelocity.x += 2.0f;
        planetBotsPos.mulAdd(planetBotsVelocity, dt);
        planetBotsScale += 0.1 * dt;
        planetBotsYdelta += 50 * dt;
        if (planetBotsScale >= 0.5f){
            planetBotsScale = 0.5f;
        }
    }

    private void updateLvl(float dt){
        if (chkTimer <= 0) {
            chkTimer = 0;
            if ((mPlayer.getDistanceCompleteCnt() % distancePerLvl) >= distancePerLvl - 1 && !bossReady && !bossFighting) {
                System.out.println("Boss HERE");
                music.pause();
                bossMusic.setVolume(AstroTour.musicVolume);
                bossMusic.isLooping();
                bossMusic.play();
                chkTimer = 1;
                botEmitter.setGenerationTime(10000.0f);
                asteroidEmitter.setGenerationTime(10000.0f);
                for (Bot bot : botEmitter.getActiveList()) {
                    botDestruction(bot);
                    if (bot.position.x > AstroTour.SCREEN_WIDTH) bot.deactivate();
                }
                for (Asteroid asteroid : asteroidEmitter.getActiveList()) {
                    asteroidDestruction(asteroid);
                    if (asteroid.position.x > AstroTour.SCREEN_WIDTH) asteroid.deactivate();
                }
                if (!bossFighting){
                    bossReady = true;
                }
            }
            if (!bossFighting){
               if (mPlayer.getDistanceCompleteCnt() % 100 >= 99) {
                   botEmitter.setGenerationTime(botGenerationTime - (mPlayer.getDistanceCompleteCnt()%distancePerLvl)/150);
                   System.out.println(botGenerationTime - (mPlayer.getDistanceCompleteCnt()%distancePerLvl)/150);
                   System.out.println("Letim 2");
               }
            }
        } else{
            chkTimer -= dt;
        }
        if (!Assets.getInstances().lvlDone.isPlaying() && bossDefeated) {
            music.pause();
            bossMusic.stop();
            lvlDoneMuscic.setVolume(AstroTour.musicVolume);
            lvlDoneMuscic.play();
            level++;
        }
    }

    private void asteroidDestruction(Asteroid asteroid) {
        float len = mPlayer.getPosition().dst(asteroid.getPosition());
        float vol = 1.0f - len / AstroTour.SCREEN_WIDTH;
        explosion.play(vol * AstroTour.soundVolume);
        particleEmitter.setup(asteroid.getPosition().x + MathUtils.random(-24, 24), asteroid.getPosition().y + MathUtils.random(-24, 24),
                asteroid.getVelocity().x + MathUtils.random(-24, 24), asteroid.getVelocity().y + MathUtils.random(-24, 24), 0.6f,
                6.0f, 1.5f,
                0.50f, 0.50f, 0.0f, 7.0f, 0.0f, 0.0f, 0.0f, 0.1f);
        particleEmitter.setup(asteroid.getPosition().x + MathUtils.random(-24, 24), asteroid.getPosition().y + MathUtils.random(-24, 24),
                asteroid.getVelocity().x + MathUtils.random(-24, 24), asteroid.getVelocity().y + MathUtils.random(-24, 24), 0.6f,
                6.0f, 1.5f,
                0.50f, 0.00f, 0.0f, 8.0f, 0.0f, 0.0f, 0.0f, 0.1f);
        particleEmitter.setup(asteroid.getPosition().x + MathUtils.random(-24, 24), asteroid.getPosition().y + MathUtils.random(-24, 24),
                asteroid.getVelocity().x + MathUtils.random(-24, 24), asteroid.getVelocity().y + MathUtils.random(-24, 24), 0.6f,
                6.0f, 1.5f,
                0.50f, 0.50f, 0.0f, 7.0f, 0.0f, 0.0f, 0.0f, 0.1f);
        particleEmitter.setup(asteroid.getPosition().x + MathUtils.random(-24, 24), asteroid.getPosition().y + MathUtils.random(-24, 24),
                asteroid.getVelocity().x + MathUtils.random(-24, 24), asteroid.getVelocity().y + MathUtils.random(-24, 24), 0.6f,
                6.0f, 1.5f,
                0.50f, 0.00f, 0.0f, 8.0f, 0.0f, 0.0f, 0.0f, 0.1f);
        particleEmitter.setup(asteroid.getPosition().x, asteroid.getPosition().y,
                asteroid.getVelocity().x, asteroid.getVelocity().y, 0.6f,
                6.0f, 1.5f,
                0.50f, 0.50f, 0.50f, 8.0f, 0.0f, 0.0f, 0.0f, 0.1f);
        asteroid.deactivate();
    }

    private void botDestruction(Ship bot) {
        float len = mPlayer.getPosition().dst(bot.getPosition());
        float vol = 1.0f - len / AstroTour.SCREEN_WIDTH;
        explosion.play(vol * AstroTour.soundVolume);
        particleEmitter.setup(bot.getPosition().x + MathUtils.random(-24, 24), bot.getPosition().y + MathUtils.random(-24, 24),
                bot.getVelocity().x + MathUtils.random(-24, 24), bot.getVelocity().y + MathUtils.random(-24, 24), 0.6f,
                6.0f, 1.5f,
                0.50f, 0.50f, 0.0f, 7.0f, 0.0f, 0.0f, 0.0f, 0.1f);
        particleEmitter.setup(bot.getPosition().x + MathUtils.random(-24, 24), bot.getPosition().y + MathUtils.random(-24, 24),
                bot.getVelocity().x + MathUtils.random(-24, 24), bot.getVelocity().y + MathUtils.random(-24, 24), 0.6f,
                6.0f, 1.5f,
                0.50f, 0.00f, 0.0f, 8.0f, 0.0f, 0.0f, 0.0f, 0.1f);
        particleEmitter.setup(bot.getPosition().x + MathUtils.random(-24, 24), bot.getPosition().y + MathUtils.random(-24, 24),
                bot.getVelocity().x + MathUtils.random(-24, 24), bot.getVelocity().y + MathUtils.random(-24, 24), 0.6f,
                6.0f, 1.5f,
                0.50f, 0.50f, 0.0f, 7.0f, 0.0f, 0.0f, 0.0f, 0.1f);
        particleEmitter.setup(bot.getPosition().x + MathUtils.random(-24, 24), bot.getPosition().y + MathUtils.random(-24, 24),
                bot.getVelocity().x + MathUtils.random(-24, 24), bot.getVelocity().y + MathUtils.random(-24, 24), 0.6f,
                6.0f, 1.5f,
                0.50f, 0.00f, 0.0f, 8.0f, 0.0f, 0.0f, 0.0f, 0.1f);
        particleEmitter.setup(bot.getPosition().x, bot.getPosition().y,
                bot.getVelocity().x, bot.getVelocity().y, 0.6f,
                6.0f, 1.5f,
                0.50f, 0.50f, 0.50f, 8.0f, 0.0f, 0.0f, 0.0f, 0.1f);
        bot.deactivate();
    }

    public int getLevel() {
        return level;
    }

    private void updateSNDcontroll(){
        if (chkPauseTimer <= 0){
            chkPauseTimer = 0;
            if (mip.isTouchedInArea(pauseRect) > -1) {
                if (!isPaused) {
                    isPaused = true;
                    music.pause();
                    chkPauseTimer = 0.5f;
                } else {
                    isPaused = false;
                    chkPauseTimer = 0.5f;
                    music.play();

                }
            }
        } else {
            chkPauseTimer -= dtTemp;
        }

        if (chkSoundTimer <= 0){
            chkSoundTimer = 0;
            if (mip.isTouchedInArea(soundRect) > -1) {
                if (AstroTour.soundVolume == 1) {
                    AstroTour.soundVolume = 0;
                    sndButtonCurr = sndButtonPrsd;
                    chkSoundTimer = 0.5f;
                } else {
                    AstroTour.soundVolume = 1;
                    sndButtonCurr = sndButton;
                    chkSoundTimer = 0.5f;

                }
            }
        } else {
            chkSoundTimer -= dtTemp;
        }
        if (chkSoundTimer <= 0){
            chkSoundTimer = 0;
            if (mip.isTouchedInArea(musicRect) > -1) {
                if (AstroTour.musicVolume == 1) {
                    AstroTour.musicVolume = 0;
                    music.pause();
                    mscButtonCurr = mscButtonPrsd;
                    chkSoundTimer = 0.5f;
                } else {
                    AstroTour.musicVolume = 1;
                    music.play();
                    mscButtonCurr = mscButton;
                    chkSoundTimer = 0.5f;

                }
            }
        } else {
            chkSoundTimer -= dtTemp;
        }
    }

    public void update(float dt){
        planetAnimation(dt);
        planetMotion(dt);
        updateSNDcontroll();
        updateLvl(dt);
        checkCollision(dt);
        music.setVolume(0.1f * AstroTour.musicVolume);
        mPlayer.update(dt);
        bulletEmitter.update(dt);
        background.update(dt, mPlayer.getVelocity());
        asteroidEmitter.update(dt);
        particleEmitter.update(dt);
        powerUpsEmitter.update(dt);
        botEmitter.update(dt);
        bossEmitter.update(dt);
        bossEmitter.checkPool();
        botEmitter.checkPool();
        asteroidEmitter.checkPool();
        bulletEmitter.checkPool();
        particleEmitter.checkPool();
        if (mPlayer.isDead()){
            explosion.play(1.0f * AstroTour.soundVolume);
            ScreenManager.getIncstance().switchScreen(ScreenManager.ScreenType.GAMEOVER);
        }
        activateShop(dt);
    }

    private void planetMotion(float dt) {
        if (!shopReady) {
            planetPos.x = AstroTour.SCREEN_WIDTH - (mPlayer.getDistanceCompleteCnt() % distancePerLvl) * (AstroTour.SCREEN_WIDTH / distancePerLvl);
        } else {
            planetPos.x -= 30 * dt;
        }
    }

    private void activateShop(float dt) {
        if (bossDefeated && botEmitter.getActiveList().size() == 0 && asteroidEmitter.getActiveList().size() == 0){
            shopTimer += dt;
        }
        if( bossDefeated && shopTimer >= shopActivationDelay){
            GameData.getInstance().setData(mPlayer.getScoreCount(), mPlayer.getMoney(), mPlayer.getDistanceCompleteCnt(),
                    level, mPlayer.getHp(), mPlayer.getHpMax(), mPlayer.getLives());
            ScreenManager.getIncstance().switchScreen(ScreenManager.ScreenType.SHOP);
            bossDefeated = false;
            shopTimer = 0.0f;
        }
    }


    private void checkCollision(float dt){

        //Коллизии астеройдов с кораблем

        for (int i = 0; i < asteroidEmitter.getActiveList().size(); i++) {
            Asteroid asteroid = asteroidEmitter.getActiveList().get(i);
            if (mPlayer.getHitArea().overlaps(asteroid.getHitArea())){
                float len  = mPlayer.getPosition().dst(asteroid.getPosition());
                float interLen = (mPlayer.getHitArea().radius + asteroid.getHitArea().radius) - len;
                collisionHelper.set(asteroid.getPosition()).sub(mPlayer.getPosition()).nor();
                asteroid.getPosition().mulAdd(collisionHelper, interLen);
                mPlayer.getPosition().mulAdd(collisionHelper, -interLen);
                asteroid.getVelocity().mulAdd(collisionHelper, interLen * (100 - 100 * asteroid.getScale()));
                mPlayer.getVelocity().mulAdd(collisionHelper, -interLen * 100 * asteroid.getScale());
                dmgAsteroid(asteroid, true, true);
                mPlayer.takeDamage(asteroid.getHpMax() * asteroid.getScale());
                Assets.getInstances().collisionHit.play(AstroTour.soundVolume);
                float partPosX;
                float partPosY;
                if ( asteroid.getPosition().x > mPlayer.getPosition().x)
                {
                    partPosX = mPlayer.getPosition().x + mPlayer.getHitArea().radius;
                } else {
                    partPosX = mPlayer.getPosition().x - mPlayer.getHitArea().radius;
                }
                if ( asteroid.getPosition().y > mPlayer.getPosition().y)
                {
                    partPosY = mPlayer.getPosition().y + mPlayer.getHitArea().radius;
                } else {
                    partPosY = mPlayer.getPosition().y - mPlayer.getHitArea().radius;
                }

                particleEmitter.setup(partPosX, partPosY,
                        MathUtils.random(-50.0f, 50.0f), MathUtils.random(-50.0f, 50.0f), 0.6f, 1.2f, 0.5f,
                        1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.5f);

            }
        }
        // Коллизии ботов с кораблем
        int botCount = 0;
        for (int i = 0; i < botEmitter.getActiveList().size(); i++) {
            Bot bot = botEmitter.getActiveList().get(i);
            if (mPlayer.getHitArea().overlaps(bot.getHitArea())){
                float len  = mPlayer.getPosition().dst(bot.getPosition());
                float interLen = (mPlayer.getHitArea().radius + bot.getHitArea().radius) - len;
                collisionHelper.set(bot.getPosition()).sub(mPlayer.getPosition()).nor();
                bot.getPosition().mulAdd(collisionHelper, interLen);
                mPlayer.getPosition().mulAdd(collisionHelper, -interLen);
                bot.getVelocity().mulAdd(collisionHelper, interLen);
                mPlayer.getVelocity().mulAdd(collisionHelper, -interLen);
                dmgBot(bot, true, 1.0f);
                mPlayer.takeDamage(20);
                float partPosX;
                float partPosY;
                if ( bot.getPosition().x > mPlayer.getPosition().x)
                {
                    partPosX = mPlayer.getPosition().x + mPlayer.getHitArea().radius;
                } else {
                    partPosX = mPlayer.getPosition().x - mPlayer.getHitArea().radius;
                }
                if ( bot.getPosition().y > mPlayer.getPosition().y)
                {
                    partPosY = mPlayer.getPosition().y + mPlayer.getHitArea().radius;
                } else {
                    partPosY = mPlayer.getPosition().y - mPlayer.getHitArea().radius;
                }

                particleEmitter.setup(partPosX, partPosY,
                        MathUtils.random(-50.0f, 50.0f), MathUtils.random(-50.0f, 50.0f), 0.6f, 1.2f, 0.5f,
                        1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.5f);

            }
            if (bot.getPosition().x < mPlayer.position.x) {
                mPlayer.setTarget(bot, dt);
                botCount++;
            }
        }
        if (botCount == 0) mPlayer.droneDeactivate();
        for (int i = 0; i < bossEmitter.getActiveList().size(); i++) {
            Boss boss = bossEmitter.getActiveList().get(i);
            if (mPlayer.getHitArea().overlaps(boss.hitArea)){
                float len  = mPlayer.getPosition().dst(boss.getPosition());
                float interLen = (mPlayer.getHitArea().radius + boss.getHitArea().radius) - len;
                collisionHelper.set(boss.getPosition()).sub(mPlayer.getPosition()).nor();
                boss.getPosition().mulAdd(collisionHelper, interLen);
                mPlayer.getPosition().mulAdd(collisionHelper, -interLen * 50);
                boss.getVelocity().mulAdd(collisionHelper, interLen);
                mPlayer.getVelocity().mulAdd(collisionHelper, -interLen * 50);
                dmgBot(boss, true, 1.0f);
                mPlayer.takeDamage(10);
                float partPosX;
                float partPosY;
                if ( boss.getPosition().x > mPlayer.getPosition().x)
                {
                    partPosX = mPlayer.getPosition().x + mPlayer.getHitArea().radius;
                } else {
                    partPosX = mPlayer.getPosition().x - mPlayer.getHitArea().radius;
                }
                if ( boss.getPosition().y > mPlayer.getPosition().y)
                {
                    partPosY = mPlayer.getPosition().y + mPlayer.getHitArea().radius;
                } else {
                    partPosY = mPlayer.getPosition().y - mPlayer.getHitArea().radius;
                }

                particleEmitter.setup(partPosX, partPosY,
                        MathUtils.random(-50.0f, 50.0f), MathUtils.random(-50.0f, 50.0f), 0.6f, 1.2f, 0.5f,
                        1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.5f);
            }
        }

        // Коллизии с пулей
        for (int i = 0; i < bulletEmitter.getActiveList().size(); i++) {
            Bullet b = bulletEmitter.getActiveList().get(i);
            for (int j = 0; j < asteroidEmitter.getActiveList().size(); j++) {
                Asteroid asteroid = asteroidEmitter.getActiveList().get(j);
                if (asteroid.getHitArea().contains(b.getPosition())) {
                    dmgAsteroid(asteroid, b.isPlayer(), false);
                    b.deactivate();
                    float len = mPlayer.getPosition().dst(asteroid.getPosition());
                    float vol = 0.8f - len / AstroTour.SCREEN_WIDTH;
                    bulletHit.play(vol * AstroTour.soundVolume);
                    particleEmitter.setup(b.getPosition().x, b.getPosition().y,
                            MathUtils.random(-50.0f, 50.0f), MathUtils.random(-50.0f, 50.0f), 0.6f, 1.2f, 0.5f,
                            1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.5f);
                    break;
                }
            }
            if (b.isPlayer()) {

                for (int j = 0; j < botEmitter.getActiveList().size(); j++) {
                    Bot bot = botEmitter.getActiveList().get(j);
                    if (bot.getHitArea().contains(b.getPosition())) {
                        dmgBot(bot, true, 1.0f);
                        b.deactivate();
                        float len = mPlayer.getPosition().dst(bot.getPosition());
                        float vol = 0.8f - len / AstroTour.SCREEN_WIDTH;
                        bulletHit.play(vol  * AstroTour.soundVolume);
                        particleEmitter.setup(b.getPosition().x, b.getPosition().y,
                                MathUtils.random(-50.0f, 50.0f), MathUtils.random(-50.0f, 50.0f), 0.6f, 1.2f, 0.5f,
                                1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.5f);
                        break;
                    }
                }
                for (int k = 0; k < bossEmitter.getActiveList().size(); k++) {
                    Boss boss = bossEmitter.getActiveList().get(k);
                    boolean hitBossBody = false;
                    if (boss.hitArea.contains(b.getPosition())){
                        if (boss.getEyes().size() == 0) hitBossBody = true;
                        for (int h = 0; h < boss.getEyes().size(); h++) {
                            BossEye eye = boss.getEyes().get(h);
                            if (eye.hitArea.contains(b.getPosition())){
                                System.out.println("EYE HIT");
                                dmgBot(eye, true, 2.0f);
                                b.deactivate();
                                float len = mPlayer.getPosition().dst(boss.getPosition());
                                float vol = 0.8f - len / AstroTour.SCREEN_WIDTH;
                                bulletHit.play(vol  * AstroTour.soundVolume);
                                particleEmitter.setup(b.getPosition().x, b.getPosition().y,
                                        MathUtils.random(-50.0f, 50.0f), MathUtils.random(-50.0f, 50.0f), 0.6f, 1.2f, 0.5f,
                                        1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.5f);
                                break;
                            }
                            if (b.getPosition().x > eye.getPosition().x + MathUtils.random(15, 100)) hitBossBody = true;
                        }
                        if (hitBossBody) {
                            dmgBot(boss, true, 1.0f);
                            b.deactivate();
                            float len = mPlayer.getPosition().dst(boss.getPosition());
                            float vol = 0.8f - len / AstroTour.SCREEN_WIDTH;
                            bulletHit.play(vol * AstroTour.soundVolume);
                            particleEmitter.setup(b.getPosition().x, b.getPosition().y,
                                    MathUtils.random(-50.0f, 50.0f), MathUtils.random(-50.0f, 50.0f), 0.6f, 1.2f, 0.5f,
                                    1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.5f);
                            break;
                        }
                    }
                }
            } else {
                if (mPlayer.getHitArea().contains(b.getPosition())) {
                    mPlayer.takeDamage(b.getShip().weapon.getWeaponDMG());
                    b.deactivate();
                    bulletHit.play(AstroTour.soundVolume);
                    particleEmitter.setup(b.getPosition().x, b.getPosition().y,
                            MathUtils.random(-50.0f, 50.0f), MathUtils.random(-50.0f, 50.0f), 0.6f, 1.2f, 0.5f,
                            1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.5f);
                }

            }
        }

        //Коллизии между астеройдами
        for (int i = 0; i < asteroidEmitter.getActiveList().size() - 1; i++) {
            for (int j = i + 1; j < asteroidEmitter.getActiveList().size(); j++) {
                Asteroid asteroidI = asteroidEmitter.getActiveList().get(i);
                Asteroid asteroidJ = asteroidEmitter.getActiveList().get(j);
                float len = asteroidI.getPosition().dst(asteroidJ.getPosition());
                if (asteroidI.getHitArea().overlaps(asteroidJ.getHitArea())){
                    float interLen = ((asteroidI.getHitArea().radius +
                            asteroidJ.getHitArea().radius) - len);
                    collisionHelper.set(asteroidI.getPosition()).sub(asteroidJ.getPosition()).nor();
                    asteroidI.getPosition().mulAdd(collisionHelper, interLen);
                    asteroidJ.getPosition().mulAdd(collisionHelper, -interLen);
                    if (asteroidI.getPosition().x < AstroTour.SCREEN_WIDTH &&
                            asteroidJ.getPosition().x < AstroTour.SCREEN_WIDTH) {
                        asteroidI.getVelocity().mulAdd(collisionHelper, interLen * 2 * (100 - 100 * asteroidI.getScale()));
                        asteroidJ.getVelocity().mulAdd(collisionHelper, -interLen * 2 * (100 - 100 * asteroidJ.getScale()));
                        dmgAsteroid(asteroidI, false, true);
                        dmgAsteroid(asteroidJ, false, true);
                        float partPosX;
                        float partPosY;
                        if ( asteroidI.getPosition().x > asteroidJ.getPosition().x)
                        {
                            partPosX = asteroidJ.getPosition().x + asteroidJ.getHitArea().radius;
                        } else {
                            partPosX = asteroidJ.getPosition().x - asteroidJ.getHitArea().radius;
                        }
                        if ( asteroidI.getPosition().y > asteroidJ.getPosition().y)
                        {
                            partPosY = asteroidJ.getPosition().y + asteroidJ.getHitArea().radius;
                        } else {
                            partPosY = asteroidJ.getPosition().y - asteroidJ.getHitArea().radius;
                        }

                        particleEmitter.setup(partPosX, partPosY,
                                MathUtils.random(-50.0f, 50.0f), MathUtils.random(-50.0f, 50.0f), 0.6f, 1.2f, 0.5f,
                                0.7f, 0.70f, 0.70f, 1.0f, 0.0f, 0.0f, 0.0f, 0.5f);


                    }
                }
            }
        }

        //Коллизии между астеройдами и ботами
        for (int i = 0; i < asteroidEmitter.getActiveList().size() - 1; i++) {
            for (int j = 0; j < botEmitter.getActiveList().size(); j++) {
                Asteroid asteroidI = asteroidEmitter.getActiveList().get(i);
                Bot bot = botEmitter.getActiveList().get(j);
                float len = asteroidI.getPosition().dst(bot.getPosition());
                if (asteroidI.getHitArea().overlaps(bot.getHitArea())){
                    float interLen = ((asteroidI.getHitArea().radius +
                            bot.getHitArea().radius) - len);
                    collisionHelper.set(asteroidI.getPosition()).sub(bot.getPosition()).nor();
                    asteroidI.getPosition().mulAdd(collisionHelper, interLen);
                    bot.getPosition().mulAdd(collisionHelper, -interLen);

                    float partPosX;
                    float partPosY;
                    if ( asteroidI.getPosition().x > bot.getPosition().x)
                    {
                        partPosX = bot.getPosition().x + bot.getHitArea().radius;
                    } else {
                        partPosX = bot.getPosition().x - bot.getHitArea().radius;
                    }
                    if ( asteroidI.getPosition().y > bot.getPosition().y)
                    {
                        partPosY = bot.getPosition().y + bot.getHitArea().radius;
                    } else {
                        partPosY = bot.getPosition().y - bot.getHitArea().radius;
                    }

                    particleEmitter.setup(partPosX, partPosY,
                            MathUtils.random(-50.0f, 50.0f), MathUtils.random(-50.0f, 50.0f), 0.6f, 1.2f, 0.5f,
                            0.7f, 0.70f, 0.70f, 1.0f, 0.0f, 0.0f, 0.0f, 0.5f);


//                    }
                }
            }
        }

        for (int i = 0; i < powerUpsEmitter.getPowerUps().length; i++) {
            PowerUp p = powerUpsEmitter.getPowerUps()[i];
            if (p.isActive()){
                if (mPlayer.getHitArea().overlaps(p.getHitArea())){
                    p.use(mPlayer);
                }
            }
        }
    }

    private void dmgAsteroid(Asteroid asteroid, boolean isPlayer, boolean isCollision) {
        int dmg = 1;
//        if (isPlayer && isCollision){
//            dmg = mPlayer.weapon.getWeaponDMG();
//        }
        if (isPlayer && !isCollision){
            dmg = mPlayer.weapon.getWeaponDMG();
        }
        if (asteroid.takeDamage(dmg)) {
            asteroidDestruction(asteroid);
            if (isPlayer) {
                mPlayer.setScoreCount((int)asteroid.getHpMax());
                powerUpsEmitter.makePower(asteroid.getPosition().x, asteroid.getPosition().y, false);
//                System.out.println("Ast size: " + (asteroid.getAsteroidSize() * asteroid.getScale()) + " | Ast Radius: " + (asteroid.getHitArea().radius * 2) + " | Regen: " + asteroid.getMaxHp());
            }
        }
    }

    private void dmgBot(Ship bot, boolean isPlayer, float mult) {
        if (bot.takeDamage(mPlayer.weapon.getWeaponDMG() * mult)) {
            botDestruction(bot);
            if (isPlayer) {
                mPlayer.setScoreCount((int) bot.getHpMax() * 10);
                powerUpsEmitter.makePower(bot.getPosition().x, bot.getPosition().y, true);
//                System.out.println("Ast size: " + (asteroid.getAsteroidSize() * asteroid.getScale()) + " | Ast Radius: " + (asteroid.getHitArea().radius * 2) + " | Regen: " + asteroid.getMaxHp());
            }
        }
    }

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }


    public ParticleEmitter getParticleEmitter() {
        return particleEmitter;
    }


    public Player getPlayer() {
        return mPlayer;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private void savePlayerProgress() {
        GameData.getInstance().setData(mPlayer.getScoreCount(), mPlayer.getMoney(), mPlayer.getDistanceCompleteCnt(),
                level, mPlayer.getHp(), mPlayer.getHpMax(), mPlayer.getLives());
        GameData.getInstance().savePlayerProgress();
    }

    public boolean isBossReady() {
        return bossReady;
    }

    public void setBossReady(boolean bossReady) {
        this.bossReady = bossReady;
    }

    public boolean isBossDefeated() {
        return bossDefeated;
    }

    public void setBossDefeated(boolean bossDefeated) {
        this.bossDefeated = bossDefeated;
    }

    public boolean isBossFighting() {
        return bossFighting;
    }

    public void setBossFighting(boolean bossFighting) {
        this.bossFighting = bossFighting;
    }

    public Music getMusic() {
        return music;
    }

    @Override
    public void resize(int width, int height) {
        mGame.getViewport().update(width, height, true);
        mGame.getViewport().apply();
    }

    @Override
    public void pause() {
        savePlayerProgress();
    }


    @Override
    public void resume() {
        System.out.println("Resume");
        mip = (MyInputProcessor) Gdx.input.getInputProcessor();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
