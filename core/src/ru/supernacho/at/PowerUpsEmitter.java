package ru.supernacho.at;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by SuperNacho on 28.09.2017.
 */

public class PowerUpsEmitter {

    private PowerUp[] powerUps;
    private TextureRegion money10;
    private TextureRegion money20;
    private TextureRegion money50;
    private TextureRegion medKit;
    private TextureRegion plasmaKit;
    private TextureRegion canonKit;

    public PowerUpsEmitter(GameScreen game, TextureAtlas atlas) {
        this.money10 = atlas.findRegion("m10");
        this.money20 = atlas.findRegion("m20");
        this.money50 = atlas.findRegion("m50");
        this.medKit = atlas.findRegion("hpCrate");
        this.plasmaKit = atlas.findRegion("plasmaCrate");
        this.canonKit = atlas.findRegion("canonUp");
        this.powerUps = new PowerUp[50];
        for (int i = 0; i < powerUps.length; i++) {
            powerUps[i] = new PowerUp(game);
        }
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < powerUps.length; i++) {
            if (powerUps[i].isActive()){
                int powerupsType = powerUps[i].getType().getNumber();
                TextureRegion renderTexture;
                switch (powerupsType){
                    case 0 :
                        renderTexture = money10;
                        break;
                    case 1 :
                        renderTexture = money20;
                        break;
                    case 2 :
                        renderTexture = money50;
                        break;
                    case 3 :
                        renderTexture = medKit;
                        break;
                    case 4 :
                        renderTexture = plasmaKit;
                        break;
                    case 5 :
                        renderTexture = canonKit;
                        break;
                    default:
                        throw new RuntimeException("PowerUpsEmitter render() switch case unknown");
                }
                batch.draw(renderTexture, powerUps[i].getPosition().x - renderTexture.getRegionWidth() / 2,
                        powerUps[i].getPosition().y - renderTexture.getRegionHeight() / 2);
            }
        }
    }

    public void update(float dt){
        for (int i = 0; i < powerUps.length; i++) {
            if (powerUps[i].isActive()){
                powerUps[i].update(dt);
            }
        }

    }

    public void makePower(float x, float y, boolean bot){
        if (bot){
            for (int i = 0; i < powerUps.length; i++) {
                if (!powerUps[i].isActive()) {
                    PowerUp.Type t = PowerUp.Type.values()[MathUtils.random(0,PowerUp.Type.values().length-1)];
                    powerUps[i].activate(x, y, t);
                    break;
                }
            }
        }
        if (Math.random() < 0.25) {
            for (int i = 0; i < powerUps.length; i++) {
                if (!powerUps[i].isActive()) {
                    PowerUp.Type t = PowerUp.Type.values()[MathUtils.random(0,3)];
                    powerUps[i].activate(x, y, t);
                    break;
                }
            }
        }

    }

    public PowerUp[] getPowerUps() {
        return powerUps;
    }
}
