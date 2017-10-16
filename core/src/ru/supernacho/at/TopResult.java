package ru.supernacho.at;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.StringBuilder;

/**
 * Created by SuperNacho on 16.10.2017.
 */

public class TopResult {

    private FileHandle file;

    private int score;
    private int distanse;
    private int lvl;

    public TopResult() {
        file = Gdx.files.external("player.result");
        if ( file.exists()) {
            String str = file.readString();
            String[] strings = str.split(" ");
            this.score = Integer.parseInt(strings[0]);
            this.distanse = Integer.parseInt(strings[1]);
            this.lvl = Integer.parseInt(strings[2]);
        } else {
            this.score = 0;
            this.distanse = 0;
            this.lvl = 0;
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDistanse() {
        return distanse;
    }

    public void setDistanse(int distanse) {
        this.distanse = distanse;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public void saveResult(){
        StringBuilder sb = new StringBuilder();
        sb.append(score).append(" ").append(distanse).append(" ").append(lvl);
        file.writeString(sb.toString(), false);
    }
}
