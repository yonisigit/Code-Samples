package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;
import src.gameobjects.BallCollisionCountdownAgent;
import src.gameobjects.Puck;

public class ChangeCameraStrategy extends RemoveBrickStrategyDecorator{

    private static final int COUNTDOWN_VALUE = 4;
    private WindowController windowController;
    private BrickerGameManager gameManager;

    ChangeCameraStrategy(CollisionStrategy toBeDecorated, WindowController windowController,
                         BrickerGameManager gameManager) {
        super(toBeDecorated);
        this.windowController = windowController;
        this.gameManager = gameManager;
    }

    /**
     * if the object that collided with the brick is the main ball sets the camera to follow the ball
     * @param thisObj
     * @param otherObj
     * @param counter
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        if(!(otherObj instanceof Puck)){
            if(gameManager.getCamera() == null){
                // handles collisions and removal
                BallCollisionCountdownAgent ballCollisionCountdownAgent =
                        new BallCollisionCountdownAgent((Ball) otherObj, this, COUNTDOWN_VALUE);
                getGameObjectCollection().addGameObject(ballCollisionCountdownAgent);
                gameManager.setCamera(new Camera(otherObj, Vector2.ZERO,
                        windowController.getWindowDimensions().mult(1.2f),
                        windowController.getWindowDimensions()));
            }
        }
    }

    /**
     * resets camera
     */
    public void turnOffCameraChange(){
        gameManager.setCamera(null);
    }
}
