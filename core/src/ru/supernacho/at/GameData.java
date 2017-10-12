package ru.supernacho.at;

/**
 * Created by SuperNacho on 08.10.2017.
 */

public class GameData {
    private static final GameData ourInstance = new GameData();

    public static GameData getInstance() {
        return ourInstance;
    }

    private float playerScore = 0;
    private float playerMoney = 0;
    private float playerDistance = 0;
    private float playerLevel = 0;

    public void setData(float score, float money, float distance, float level){
        playerScore = score;
        playerMoney = money;
        playerDistance = distance;
        playerLevel = level;
    }

    public float getPlayerScore() {
        return playerScore;
    }

    public float getPlayerMoney() {
        return playerMoney;
    }

    public float getPlayerDistance() {
        return playerDistance;
    }

    public float getPlayerLevel() {
        return playerLevel;
    }

    private GameData() {
    }
}
