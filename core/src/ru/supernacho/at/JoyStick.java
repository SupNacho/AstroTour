package ru.supernacho.at;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by SuperNacho on 01.10.2017.
 */

public class JoyStick {
    private TextureRegion joyfield;
    private TextureRegion joystick;
    private Rectangle mRectangle;
    private float joyCenterX, joyCenterY;
    private int lastId;
    private boolean touched;
    private Vector2 vs;
    private Vector2 norm;

    public JoyStick (TextureRegion field, TextureRegion stick){
        this.joyfield = field;
        this.joystick = stick;
        mRectangle = new Rectangle(0, 0, 200, 200);
        joyCenterX = mRectangle.x + mRectangle.width / 2;
        joyCenterY = mRectangle.y + mRectangle.height / 2;
        lastId = -1;
        vs = new Vector2(0,0);
        norm = new Vector2(0,0);
    }

    public void render(SpriteBatch batch){
        batch.draw(joyfield, mRectangle.x, mRectangle.y);
        batch.draw(joystick, joyCenterX + vs.x - 25, joyCenterY + vs.y - 25);

    }

    public void update(float dt){
        touched = false;
        MyInputProcessor mip = (MyInputProcessor) Gdx.input.getInputProcessor();
        if (lastId == -1) {
            lastId = mip.isTouchedInArea((int) mRectangle.x, (int) mRectangle.y, (int) mRectangle.width, (int) mRectangle.height);
        }
        if (lastId > -1) {
            touched = true;
            float touchX = mip.getX(lastId);
            float touchY = mip.getY(lastId);
            vs.x = touchX - joyCenterX;
            vs.y = touchY - joyCenterY;
            if (vs.len() > 75) {
                vs.nor().scl(75);
            }
        }
        if ( lastId > -1 && !mip.isTouched(lastId)){
            lastId = -1;
            vs.x = 0;
            vs.y = 0;
        }
        norm.set(vs);
        norm.nor();
    }

    public Vector2 getNorm() {
        return norm;
    }

    public float getPower(){
        return vs.len() / 75.0f;
    }
}
