package src.brick_strategies;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;
import src.BrickerGameManager;

public interface CollisionStrategy {


    void onCollision(GameObject thisObj, GameObject otherObj, Counter counter);

    GameObjectCollection getGameObjectCollection();
}
