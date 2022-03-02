package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class SlowTimeChanger extends GameObject {
    private static final float SLOW_TIME = 0.9f;
    private WindowController windowController;
    private Vector2 windowDimensions;
    private GameObjectCollection gameObjects;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public SlowTimeChanger(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                           WindowController windowController, Vector2 windowDimensions,
                           GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable);
        this.windowController = windowController;
        this.windowDimensions = windowDimensions;
        this.gameObjects = gameObjects;
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other instanceof Paddle;
    }

    /**
     * makes time slower on contact with paddle
     * @param other
     * @param collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        windowController.setTimeScale(SLOW_TIME);
        gameObjects.removeGameObject(this);
    }

    /**
     * removes object if out of bounds
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (getCenter().y() > windowDimensions.y()){
            gameObjects.removeGameObject(this);
        }
    }
}

