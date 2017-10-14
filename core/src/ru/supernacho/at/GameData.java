package ru.supernacho.at;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.StringBuilder;

/**
 * Created by SuperNacho on 08.10.2017.
 */

public class GameData {
    private static final GameData ourInstance = new GameData();


    public static GameData getInstance() {
        return ourInstance;
    }

    private int playerScore = 0;
    private int playerMoney = 0;
    private float playerDistance = 0;
    private int playerLevel = 0;
    private float playerHp = 0;
    private int playerLives = 0;
    private int playerWeaponType = 0;
    private float playerHpMax = 250;
    private FileHandle file = Gdx.files.external("save.game");

    public void setData(int score, int money, float distance, int level, float playerHp, float playerHpMax, int lives){
        this.playerScore = score;
        this.playerMoney = money;
        this.playerDistance = distance;
        this.playerLevel = level;
        this.playerHp = playerHp;
        this.playerHpMax = playerHpMax;
        this.playerLives = lives;
    }

    public float getPlayerScore() {
        return playerScore;
    }

    public int getPlayerMoney() {
        return playerMoney;
    }

    public float getPlayerDistance() {
        return playerDistance;
    }

    public float getPlayerLevel() {
        return playerLevel;
    }

    public float getPlayerHp() {
        return playerHp;
    }

    public float getPlayerHpMax() {
        return playerHpMax;
    }

    public void setPlayerHp(float playerHp) {
        this.playerHp = playerHp;
    }

    public int getPlayerLives() {
        return playerLives;
    }

    public void setPlayerLives(int playerLives) {
        this.playerLives = playerLives;
    }

    public void setPlayerMoney(int playerMoney) {
        this.playerMoney = playerMoney;
    }

    public void savePlayerProgress() {
        StringBuilder sb = new StringBuilder();
        sb.append(playerHp).append(" ").append(playerDistance).append(" ");
        sb.append(playerScore).append(" ").append(playerLives).append(" ");
        sb.append(playerMoney).append(" ").append(playerWeaponType).append(" ").append(playerLevel);
        file.writeString(sb.toString(), false);
    }

    public boolean loadPlayerProgress(GameScreen game, Player player){
        if (file.exists()) {
            String str = file.readString();
            String[] strings = str.split(" ");
            player.loadPlayer(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5]);
            game.setLevel(Integer.parseInt(strings[6]));
            return true;
        }
        return false;
    }

    private GameData() {
    }

    public int getPlayerWeaponType() {
        return playerWeaponType;
    }

    public void setPlayerWeaponType(int playerWeaponType) {
        this.playerWeaponType = playerWeaponType;
    }
    public void viewData(){
        System.out.println("Pl hp - " + playerHp + " | Pl Lives - " + playerLives + " | Pl weapon - " + playerWeaponType);
    }
}
