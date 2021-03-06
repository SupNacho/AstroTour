package ru.supernacho.at;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by SuperNacho on 29.09.2017.
 */

public class BotEmitter extends ObjectPool<Bot> {

    private final GameScreen game;
    private final Player player;
    private TextureRegion botTexture;
    private float generationTime;
    private float innerTime;
    private boolean isStoped;

    public BotEmitter(GameScreen game, Player player, TextureRegion botTexture, int size, float generationTime) {
        super();
        this.botTexture = botTexture;
        this.generationTime = generationTime;
        this.game = game;
        this.player = player;
        this.isStoped = false;
    }

    public void update(float dt) {
        innerTime += dt;
        if (!isStoped) {
            if (innerTime > generationTime) {
                innerTime -= generationTime;
                setUp();
            }
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void setGenerationTime(float generationTime) {
        this.generationTime = generationTime;
    }

    public void setUp(float x, float y){
        Bot b = getActiveElement();
        b.activate(x,y);
    }
    public void setUp(){
        Bot b = getActiveElement();
        float x = MathUtils.random(AstroTour.SCREEN_WIDTH + 128, AstroTour.SCREEN_WIDTH * 1.5f);
        float y = MathUtils.random(0.0f, AstroTour.SCREEN_HEIGHT);
        b.activate(x,y);
    }

    @Override
    protected Bot newObject() {
        return new Bot(game, botTexture);
    }

    public boolean isStoped() {
        return isStoped;
    }

    public void setStoped(boolean stoped) {
        isStoped = stoped;
    }
}
