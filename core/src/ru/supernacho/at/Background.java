package ru.supernacho.at;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by SuperNacho on 21.09.2017.
 */

public class Background {

    private class Star{
        private Vector2 position;
        private float speed;

        public Star (){
            this.position = new Vector2(MathUtils.random(0, AstroTour.SCREEN_WIDTH),
                    MathUtils.random(0, AstroTour.SCREEN_HEIGHT));
            this.speed = MathUtils.random(20.0f,100.0f);
        }

        public void update(float dt, Vector2 v){
            position.x -= speed * dt;
            position.mulAdd(v, -dt * 0.07f);
            if (position.y < -20){
                position.y = AstroTour.SCREEN_HEIGHT;
            }
            if (position.y > AstroTour.SCREEN_HEIGHT){
                position.y = -20;
            }

            if (position.x < -40) {
                this.position.x = AstroTour.SCREEN_WIDTH;
                this.speed = MathUtils.random(20.0f, 100.0f);
            }
        }
    }
    private TextureRegion bgTexture;
    private TextureRegion starTexture;
    private Star[] mStars;

    public Background(TextureAtlas textureAtlas) {
        this.bgTexture = textureAtlas.findRegion("bg");
        this.starTexture = textureAtlas.findRegion("star16");
        mStars = new Star[250];
        for (int i = 0; i < mStars.length ; i++) {
            mStars[i] = new Star();
        }
    }

    public void render(SpriteBatch batch){
        batch.draw(bgTexture,0,0, AstroTour.SCREEN_WIDTH, AstroTour.SCREEN_HEIGHT);
        for (Star star : mStars) {
            float scale = star.speed / 100.0f / 2.0f;
            if (Math.random() < 0.01f){
                scale *= 1.5f;
            }
            batch.draw(starTexture, star.position.x, star.position.y, 8,8, 16, 16, scale,scale, 0);
        }
    }

    public void update(float dt, Vector2 v){
        for (Star star : mStars) {
            star.update(dt, v);
        }

    }

}
