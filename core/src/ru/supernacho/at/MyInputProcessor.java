package ru.supernacho.at;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import org.w3c.dom.css.Rect;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SuperNacho on 01.10.2017.
 */

public class MyInputProcessor implements InputProcessor {
    class TouchInfo{
        int x;
        int y;
        boolean touched;
        boolean touchUped;
    }

    private AstroTour mGame;
    private HashMap<Integer, TouchInfo> touchMap = new HashMap<Integer, TouchInfo>();
    private Vector2 temp = new Vector2(0,0);

    public MyInputProcessor(AstroTour mGame){
        this.mGame = mGame;
        for (int i = 0; i < 5; i++) {
            touchMap.put(i, new TouchInfo());
        }
    }

    public void clear(){
        for (int i = 0; i < 5; i++) {
            touchMap.put(i, new TouchInfo());
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        mGame.getViewport().unproject(temp.set(screenX, screenY));
        touchMap.get(pointer).x = (int) temp.x;
        touchMap.get(pointer).y = (int) temp.y;
        touchMap.get(pointer).touched = true;
        touchMap.get(pointer).touchUped = false;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touchMap.get(pointer).x = 0;
        touchMap.get(pointer).y = 0;
        touchMap.get(pointer).touched = false;
        touchMap.get(pointer).touchUped = true;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        mGame.getViewport().unproject(temp.set(screenX, screenY));
        touchMap.get(pointer).x = (int) temp.x;
        touchMap.get(pointer).y = (int) temp.y;
        touchMap.get(pointer).touched = true;
        touchMap.get(pointer).touchUped = false;
        return false;
    }

    public boolean isTouched(int pointer){
        return touchMap.get(pointer).touched;
    }
    public boolean isTouchUped(int pointer){
        return touchMap.get(pointer).touchUped;
    }

    public int getX(int pointer){
        return touchMap.get(pointer).x;
    }
    public int getY(int pointer){
        return touchMap.get(pointer).y;
    }

    public int isTouchedInArea(Rectangle rect){
        return isTouchedInArea((int) rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
    }
    public int isTouchedInArea(int x, int y, int w, int h){
        for (Map.Entry<Integer, TouchInfo> o : touchMap.entrySet()){
            if (o.getValue().touched){
                int  id = o.getKey();
                TouchInfo t = o.getValue();
                if (t.x > x && t.x < x + w && t.y > y && t.y < y + h){
                    return id;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
