package ru.supernacho.at;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by SuperNacho on 10.11.2017.
 */

public class BossEmitter extends ObjectPool<Boss> {
    private GameScreen game;
    private TextureRegion texture;

    public BossEmitter(GameScreen game, int size) {
        super();
        this.game = game;
        for (int i = 0; i < size; i++) {
            texture = Assets.getInstances().atlas.findRegion("boss1");// TODO: 14.11.2017 исправить индексы
            freeList.add(newObject());
        }
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            Boss boss = activeList.get(i);
            boss.render(batch);
        }
    }

    public void update(float dt){
        if (game.isBossReady()){
            setUp();
            game.setBossReady(false);
            game.setBossFighting(true);
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public void setUp(){
        Boss b = getActiveElement();
        float x = MathUtils.random(800, 1000);
        float y = MathUtils.random(0.0f, AstroTour.SCREEN_HEIGHT);
        b.activate(x,y, game.getLevel());
    }

    @Override
    protected Boss newObject() {
        return new Boss(game, texture);
    }
}
