package ru.supernacho.at;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by SuperNacho on 28.09.2017.
 */

public class PowerUp {


    enum Type {
        MONEY10(0), MONEY20(1), MONEY50(2), MEDKIT(3), PLASMAKIT(4), CANONKIT(5);

        int number;

        Type(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }

    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private float time;
    private float maxTime;
    private Type type;
    private Circle hitArea;
    private final GameScreen game;

    public PowerUp(GameScreen game) {
        this.position = new Vector2(0,0);
        this.velocity = new Vector2(0,0);
        this.active = false;
        this.time = 0;
        this.maxTime = 6.0f;;
        this.hitArea = new Circle(0,0, 32);
        this.game = game;
    }

    public void update(float dt){
        time += dt;
        position.mulAdd(velocity, dt);
        if (time > maxTime){

            game.getParticleEmitter().setup(position.x + MathUtils.random(-24, 24), position.y + MathUtils.random(-24, 24),
                    velocity.x + MathUtils.random(-24, 24), velocity.y + MathUtils.random(-24, 24), 0.6f,
                    3.0f, 1.5f,
                    0.50f, 0.00f, 0.0f, 8.0f, 0.0f, 0.0f, 0.0f, 0.1f);
            game.getParticleEmitter().setup(position.x + MathUtils.random(-24, 24), position.y + MathUtils.random(-24, 24),
                    velocity.x + MathUtils.random(-24, 24), velocity.y + MathUtils.random(-24, 24), 0.6f,
                    3.0f, 1.5f,
                    0.50f, 0.00f, 0.0f, 8.0f, 0.0f, 0.0f, 0.0f, 0.1f);
            game.getParticleEmitter().setup(position.x + MathUtils.random(-24, 24), position.y + MathUtils.random(-24, 24),
                    velocity.x + MathUtils.random(-24, 24), velocity.y + MathUtils.random(-24, 24), 0.6f,
                    3.0f, 1.5f,
                    0.50f, 0.00f, 0.0f, 8.0f, 0.0f, 0.0f, 0.0f, 0.1f);
                deactivate();
        }
        hitArea.setPosition(position);
    }

    public boolean isActive() {
        return active;
    }
    public void activate(float x, float y, Type type){
        this.position.set(x,y);
        this.velocity.set(MathUtils.random(-50.0f, 50.0f), MathUtils.random(-50.0f, 50.0f));
        this.type = type;
        this.time = 0.0f;
        this.active = true;

    }
    public void deactivate(){
        this.active = false;
    }

    public void use (Player player){
        Assets.getInstances().powerUp.play(AstroTour.soundVolume);
        switch (type){
            case MONEY10:
                player.addMoney(10);
                game.getParticleEmitter().setup(position.x, position.y,
                        velocity.x, velocity.y, 0.6f,
                        3.0f, 1.5f,
                        0.50f, 0.50f, 0.50f, 0.80f, 0.0f, 0.0f, 0.0f, 0.1f);
                break;
            case MONEY20:
                player.addMoney(20);
                game.getParticleEmitter().setup(position.x, position.y,
                        velocity.x, velocity.y, 0.6f,
                        3.0f, 1.5f,
                        0.00f, 0.50f, 0.50f, 0.80f, 0.0f, 0.0f, 0.0f, 0.1f);
                break;
            case MONEY50:
                player.addMoney(50);
                game.getParticleEmitter().setup(position.x, position.y,
                        velocity.x, velocity.y, 0.6f,
                        3.0f, 1.5f,
                        0.80f, 0.00f, 0.50f, 0.80f, 0.0f, 0.0f, 0.0f, 0.1f);
                break;
            case MEDKIT:
                player.setShipRegen(50.0f);
                game.getParticleEmitter().setup(position.x, position.y,
                        velocity.x, velocity.y, 0.6f,
                        3.0f, 1.5f,
                        0.00f, 0.80f, 0.0f, 0.80f, 0.0f, 0.0f, 0.0f, 0.1f);
                break;
            case PLASMAKIT:
//                player.setWeaponType(Weapon.WeaponType.PLASMA_PU.getIndex());
                player.setWeaponUp(Weapon.WeaponType.PLASMA_PU);
                game.getParticleEmitter().setup(position.x, position.y,
                        velocity.x, velocity.y, 0.6f,
                        3.0f, 1.5f,
                        0.00f, 0.80f, 0.0f, 0.80f, 0.0f, 0.0f, 0.0f, 0.1f);
                break;
            case CANONKIT:
//                player.setWeaponType(Weapon.WeaponType.PLASMA_PU.getIndex());
                player.setWeaponUp(Weapon.WeaponType.CANON);
                game.getParticleEmitter().setup(position.x, position.y,
                        velocity.x, velocity.y, 0.6f,
                        3.0f, 1.5f,
                        0.00f, 0.80f, 0.0f, 0.80f, 0.0f, 0.0f, 0.0f, 0.1f);
                break;
            default:
                throw new RuntimeException("Unknown Switch case in PowerUp use()");
        }
        deactivate();
    }

    public Type getType() {
        return type;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Circle getHitArea() {
        return hitArea;
    }
}
