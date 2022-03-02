package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.FastTimeChanger;
import src.gameobjects.SlowTimeChanger;

public class TimeChangeStrategy extends RemoveBrickStrategyDecorator{

    private static final int CHANGER_LENGTH = BrickerGameManager.PADDLE_LENGTH;
    private static final int CHANGER_HEIGHT = BrickerGameManager.PADDLE_HEIGHT;
    private static final float SPEED = 250;

    private ImageReader imageReader;
    private WindowController windowController;
    private Vector2 windowDimensions;
    private GameObjectCollection gameObjects;

    TimeChangeStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader,
                       WindowController windowController,
                       Vector2 windowDimensions, GameObjectCollection gameObjects) {
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.windowController = windowController;
        this.windowDimensions = windowDimensions;
        this.gameObjects = gameObjects;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        Renderable slowTimeImage = imageReader.readImage("assets/slow.png", false);
        Renderable fastTimeImage = imageReader.readImage("assets/quicken.png", false);
        GameObject[] timeChangers = {new FastTimeChanger(thisObj.getTopLeftCorner(),
                new Vector2(CHANGER_LENGTH, CHANGER_HEIGHT), fastTimeImage, windowController, windowDimensions,
                gameObjects), new SlowTimeChanger(thisObj.getTopLeftCorner(),
                new Vector2(CHANGER_LENGTH, CHANGER_HEIGHT), slowTimeImage, windowController, windowDimensions,
                gameObjects)};
        GameObject chosen;
        if(windowController.getTimeScale() < 1){
            chosen = timeChangers[0];
        }
        else{
            chosen = timeChangers[1];
        }
        chosen.setVelocity(Vector2.DOWN.mult(SPEED));
        gameObjects.addGameObject(chosen);
    }
}
