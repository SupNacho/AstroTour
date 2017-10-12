package ru.supernacho.at;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by SuperNacho on 01.10.2017.
 */

public class FireButton {
    private TextureRegion fireButton;
    private TextureRegion fireButtonPressed;
    private TextureRegion currentTexture;
    private Rectangle mRectangle;
    private int lastId;
    private boolean touched;

    public FireButton(TextureRegion field, TextureRegion buttonPrs){
        this.fireButton = field;
        this.fireButtonPressed = buttonPrs;
        this.currentTexture = field;
        mRectangle = new Rectangle(AstroTour.SCREEN_WIDTH - field.getRegionWidth(), 0, fireButton.getRegionWidth(), fireButton.getRegionHeight());
        lastId = -1;
    }

    public void render(SpriteBatch batch){
        batch.draw(currentTexture, mRectangle.x, mRectangle.y);

    }

    public void update(){
        touched = false;
        MyInputProcessor mip = (MyInputProcessor) Gdx.input.getInputProcessor();
        if (lastId == -1) {
            lastId = mip.isTouchedInArea((int) mRectangle.x, (int) mRectangle.y, (int) mRectangle.width, (int) mRectangle.height);
        }
        if (lastId > -1) {
            touched = true;
            currentTexture = fireButtonPressed;
        }
        if ( lastId > -1 && !mip.isTouched(lastId)){
            lastId = -1;
            currentTexture = fireButton;
        }
    }

    public boolean isTouched() {
        return touched;
    }
}
