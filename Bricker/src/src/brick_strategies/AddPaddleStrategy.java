package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.MockPaddle;

public class AddPaddleStrategy extends RemoveBrickStrategyDecorator{
    private static final int NUM_COLLISIONS_TO_DISAPPEAR = 3;

    private ImageReader imageReader;
    private UserInputListener userInputListener;
    private Vector2 windowDimensions;

    AddPaddleStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader,
                      UserInputListener userInputListener, Vector2 windowDimensions) {
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.userInputListener = userInputListener;
        this.windowDimensions = windowDimensions;
    }

    /**
     * creates new paddle in middle of screen
     * @param thisObj
     * @param otherObj
     * @param counter
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        if(!MockPaddle.isInstantiated){
            Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
            MockPaddle mockPaddle = new MockPaddle(Vector2.ZERO, new Vector2(BrickerGameManager.PADDLE_LENGTH,
                    BrickerGameManager.PADDLE_HEIGHT), paddleImage, userInputListener, windowDimensions,
                    getGameObjectCollection(), BrickerGameManager.MIN_DISTANCE_FROM_SCREEN_EDGE + 1,
                    NUM_COLLISIONS_TO_DISAPPEAR);
            mockPaddle.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y()/2));
            getGameObjectCollection().addGameObject(mockPaddle);
        }
    }
}
