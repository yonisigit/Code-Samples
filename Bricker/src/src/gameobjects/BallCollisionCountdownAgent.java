package src.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.brick_strategies.ChangeCameraStrategy;

public class BallCollisionCountdownAgent extends GameObject {

    private int startingCount; // saves initial amount to be able to check if 4 collisions happened
    private Ball ball;
    private ChangeCameraStrategy owner;
    private int countDownValue;

    /**
     *
     * @param ball
     * @param owner
     * @param countDownValue
     */
    public BallCollisionCountdownAgent(Ball ball, ChangeCameraStrategy owner, int countDownValue) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.ball = ball;
        this.owner = owner;
        this.countDownValue = countDownValue;
        this.startingCount = ball.getCollisionCount();
    }

    /**
     * resets camera after 4 collisions
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (ball.getCollisionCount() - countDownValue - 1 >= startingCount){
            owner.turnOffCameraChange();
            owner.getGameObjectCollection().removeGameObject(this);
        }
    }
}
