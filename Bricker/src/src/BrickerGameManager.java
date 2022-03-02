package src;

import src.brick_strategies.BrickStrategyFactory;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.*;

import java.awt.*;
import java.util.Random;

// added

public class BrickerGameManager extends GameManager {

    public static final int BORDER_WIDTH = 20;
    public static final int PADDLE_LENGTH = 100;
    public static final int PADDLE_HEIGHT = 15;
    public static final int MIN_DISTANCE_FROM_SCREEN_EDGE = 20;

    private static final int BALL_SPEED = 250;
    private static final int FRAME_RATE = 90;
    private static final int BALL_SIZE = 20;
    private static final int PADDLE_DIST = 30;
    private static final int BRICK_HEIGHT = 15;
    private static final int WINDOW_LENGTH = 700;
    private static final int WINDOW_HEIGHT = 500;
    private static final int BRICKS_PER_ROW = 8;
    private static final int BRICK_ROWS = 5;
    private static final int LIVES = 4;
    private static final int LIVES_HEIGHT = 25;
    private static final int LIVES_WIDTH = 15;
    private static final int SPACE = 15;

    private GameObject ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private Counter bricksCounter;
    private Counter livesCounter;
    private NumericLifeCounter numericLifeCounter;

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    private void createGraphicCounter(Vector2 windowDimensions, ImageReader imageReader){
        Renderable lifeImage = imageReader.readImage("assets/heart.png", true);
        GraphicLifeCounter graphicLifeCounter =
                new GraphicLifeCounter(new Vector2(numericLifeCounter.getTopLeftCorner().x() + SPACE,
                        windowDimensions.y() - LIVES_HEIGHT), new Vector2(LIVES_WIDTH,
                        LIVES_HEIGHT),
                        livesCounter, lifeImage, gameObjects(), LIVES);
        gameObjects().addGameObject(graphicLifeCounter);
    }

    private void createNumericalCounter(Vector2 windowDimensions){
        this.livesCounter = new Counter(LIVES);
        this.numericLifeCounter = new NumericLifeCounter(livesCounter, new Vector2(0,
                windowDimensions.y() - LIVES_HEIGHT), new Vector2(LIVES_WIDTH, LIVES_HEIGHT),
                gameObjects());
        gameObjects().addGameObject(numericLifeCounter);
    }

    private void createBall(ImageReader imageReader, SoundReader soundReader, Vector2 windowDimensions){
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        Ball ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage,
                collisionSound);
        this.ball = ball;
        ball.setCenter(windowDimensions.mult(0.5f));
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()){
            ballVelX *= -1;
        }
        if (rand.nextBoolean()){
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        gameObjects().addGameObject(ball);
    }

    private void createPaddle(ImageReader imageReader, UserInputListener inputListener,
                              Vector2 windowDimensions){
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        Paddle paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_LENGTH, PADDLE_HEIGHT), paddleImage,
                inputListener, windowDimensions, MIN_DISTANCE_FROM_SCREEN_EDGE);
        paddle.setCenter(new Vector2(windowDimensions.x()/2, (int)windowDimensions.y() - PADDLE_DIST));
        gameObjects().addGameObject(paddle);
    }

    private void createBackground(Vector2 windowDimensions, ImageReader imageReader){
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions,
                imageReader.readImage("assets/DARK_BG2_small.jpeg", false));
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    private void createBorders(Vector2 windowsDimensions){
        float x = windowsDimensions.x();
        float y = windowsDimensions.y();
        RectangleRenderable rectangle = new RectangleRenderable(Color.BLUE);
        GameObject[] borders = {new GameObject(Vector2.ZERO, new Vector2(BORDER_WIDTH, y), rectangle),
                                new GameObject(new Vector2(x - BORDER_WIDTH, 0),
                                            new Vector2(BORDER_WIDTH, y), rectangle),
                                new GameObject(Vector2.ZERO, new Vector2(x, BORDER_WIDTH), rectangle)};
        for (int i = 0; i < borders.length; i++) {
            gameObjects().addGameObject(borders[i]);
        }
    }

    private void createBricks(Vector2 windowDimensions, ImageReader imageReader, BrickStrategyFactory brickStrategyFactory) {
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        this.bricksCounter = new Counter();
        int brick_length = (int)(windowDimensions.x() - (BORDER_WIDTH * 2))/BRICKS_PER_ROW;
        Vector2 start = new Vector2(BORDER_WIDTH, BORDER_WIDTH);
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICKS_PER_ROW; j++) {
                Brick brick = new Brick(new Vector2(start.x() + (j * brick_length),
                        start.y() + (i * BRICK_HEIGHT)), new Vector2(brick_length, BRICK_HEIGHT), brickImage,
                        brickStrategyFactory.getStrategy(), bricksCounter);
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
                bricksCounter.increment();
            }
        }
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        this.windowController = windowController;
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(FRAME_RATE);
        windowDimensions = windowController.getWindowDimensions();
        BrickStrategyFactory brickStrategyFactory = new BrickStrategyFactory(gameObjects(), this,
                imageReader, soundReader, inputListener, windowController, windowDimensions);
        // create gameObjects
        createBall(imageReader, soundReader, windowDimensions);
        createPaddle(imageReader, inputListener, windowDimensions);
        createBorders(windowDimensions);
        createBackground(windowDimensions, imageReader);
        createBricks(windowDimensions, imageReader, brickStrategyFactory);
        createNumericalCounter(windowDimensions);
        createGraphicCounter(windowDimensions, imageReader);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
    }

    private void checkForGameEnd() {
        String prompt = "";
        if(ball.getCenter().y() > windowDimensions.y()) {
            livesCounter.decrement();
            if (livesCounter.value() == 0) {
                prompt = "You Lose!";
            } else {
                ball.setCenter(windowDimensions.mult(0.5f));
            }
        }
        if(bricksCounter.value() == 0){
            prompt = "You Win!";
        }
        if(!prompt.isEmpty()){
            prompt += " Play Again?";
            if(windowController.openYesNoDialog(prompt)){
                windowController.resetGame();
            }
            else {
                windowController.closeWindow();
            }
        }
    }


    public static void main(String[] args) {
        new BrickerGameManager("Bricker", new Vector2(WINDOW_LENGTH, WINDOW_HEIGHT)).run();
    }
}

