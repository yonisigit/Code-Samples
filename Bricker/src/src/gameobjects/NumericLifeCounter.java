package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class NumericLifeCounter extends GameObject {
    private TextRenderable textRenderable;
    private GameObject gameObject;
    private int currentCounter; // saves amount of lives to know when to remove them
    private Counter livesCounter;
    private Vector2 topLeftCorner;
    private Vector2 dimensions;
    private GameObjectCollection gameObjectCollection;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions,
                              GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, null);
        this.currentCounter = livesCounter.value();
        this.livesCounter = livesCounter;
        this.topLeftCorner = topLeftCorner;
        this.dimensions = dimensions;
        this.gameObjectCollection = gameObjectCollection;
        this.textRenderable = new TextRenderable(String.format("%d", livesCounter.value()));
        this.gameObject = new GameObject(topLeftCorner, dimensions, textRenderable);
        gameObjectCollection.addGameObject(gameObject, Layer.FOREGROUND);
    }

    /**
     * updates the counter when life is lost
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(livesCounter.value() < currentCounter){
            currentCounter = livesCounter.value();
            gameObjectCollection.removeGameObject(gameObject, Layer.FOREGROUND);
            textRenderable = new TextRenderable(String.format("%d", livesCounter.value()));
            gameObject = new GameObject(topLeftCorner, dimensions, textRenderable);
            gameObjectCollection.addGameObject(gameObject, Layer.FOREGROUND);
        }
    }
}
