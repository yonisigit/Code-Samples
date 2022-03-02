package src.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import src.BrickerGameManager;

import java.util.Random;

public class BrickStrategyFactory {

    private static final int STRATEGIES_NUM = 6;

    private GameObjectCollection gameObjectCollection;
    private BrickerGameManager gameManager;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private Vector2 windowDimensions;

    public  BrickStrategyFactory(GameObjectCollection gameObjectCollection, BrickerGameManager gameManager,
                                 ImageReader imageReader, SoundReader soundReader,
                                 UserInputListener inputListener, WindowController windowController,
                                 Vector2 windowDimensions){

        this.gameObjectCollection = gameObjectCollection;
        this.gameManager = gameManager;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowDimensions;
    }

    public CollisionStrategy getStrategy(){
        Random rand = new Random();
        int i = rand.nextInt(STRATEGIES_NUM);
        RemoveBrickStrategy removeBrickStrategy = new RemoveBrickStrategy(gameObjectCollection);
        switch (i){
            case 0:
                return removeBrickStrategy;
            case 1:
                return new PuckStrategy(removeBrickStrategy, imageReader, soundReader);
            case 2:
                return  new AddPaddleStrategy(removeBrickStrategy, imageReader, inputListener,
                        windowDimensions);
            case 3:
                return new ChangeCameraStrategy(removeBrickStrategy, windowController, gameManager);
            case 4:
                return new TimeChangeStrategy(removeBrickStrategy, imageReader, windowController,
                        windowDimensions, gameObjectCollection);
            case 5:
                return new DoubleStrategy(getStrategy(), getStrategy());
            default:
                return null;
        }
    }
}
