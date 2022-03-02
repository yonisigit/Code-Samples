package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Puck;

import java.util.Random;

public class PuckStrategy extends RemoveBrickStrategyDecorator{

    private static final int PUCK_SPEED = 250;

    private ImageReader imageReader;
    private SoundReader soundReader;

    public PuckStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader, SoundReader soundReader) {
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
    }

    /**
     * creates 3 pucks to replace the brick
     * @param thisObj
     * @param otherObj
     * @param counter
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        Renderable puckImage = imageReader.readImage("assets/mockBall.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        Vector2 start = thisObj.getTopLeftCorner().add(Vector2.ONES);
        int puckSize = (int)(thisObj.getDimensions().x() - 6)/3;
        for (int i = 0; i < 3; i++) {
            Puck puck = new Puck(new Vector2(start.x() + i*(puckSize + 1), start.y()), new Vector2(puckSize,
                    puckSize), puckImage, collisionSound);
            // set angles and speed
            float puckVelX = PUCK_SPEED;
            float puckVelY = PUCK_SPEED;
            Random rand = new Random();
            if (rand.nextBoolean()){
                puckVelX *= -1;
            }
            puck.setVelocity(new Vector2(puckVelX, puckVelY));
            super.getGameObjectCollection().addGameObject(puck);
        }
    }
}
