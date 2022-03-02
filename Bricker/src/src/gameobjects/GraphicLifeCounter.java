package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class GraphicLifeCounter extends GameObject {
    private static final int SPACE = 5;
    private int currentCounter; // saves amount of lives to know when to remove them
    private GameObject[] objects;
    private Counter livesCounter;
    private GameObjectCollection gameObjectsCollection;
    private int numOfLives;


    /**
     * creates objects that are a graphical presentation of amount of lives left
     *
     * @param widgetTopLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param widgetDimensions    Width and height in window coordinates.
     * @param widgetRenderable    The renderable representing the object. Can be null, in which case
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions, Counter livesCounter,
                              Renderable widgetRenderable, GameObjectCollection gameObjectsCollection, int numOfLives) {
        super(widgetTopLeftCorner, widgetDimensions, null);
        this.livesCounter = livesCounter;
        this.currentCounter = livesCounter.value();
        this.gameObjectsCollection = gameObjectsCollection;
        this.numOfLives = numOfLives;
        this.objects = new GameObject[numOfLives];
        // creates graphics and adds to game
        for (int i = 0; i < numOfLives; i++) {
            objects[i] =
                    new GameObject(new Vector2(widgetTopLeftCorner.x() + (i * (widgetDimensions.x() + SPACE)),
                    widgetTopLeftCorner.y()), widgetDimensions, widgetRenderable);
            gameObjectsCollection.addGameObject(objects[i], Layer.FOREGROUND);
        }
    }

    /**
     * removes a life when lost
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(livesCounter.value() < currentCounter){
            currentCounter = livesCounter.value();
            gameObjectsCollection.removeGameObject(objects[currentCounter], Layer.FOREGROUND);
        }
    }
}
