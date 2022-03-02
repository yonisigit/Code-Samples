package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

public class RemoveBrickStrategy implements CollisionStrategy {

    private GameObjectCollection gameObjectCollection;

    public RemoveBrickStrategy(GameObjectCollection gameObjectCollection){
        this.gameObjectCollection = gameObjectCollection;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        if(gameObjectCollection.removeGameObject(thisObj, Layer.STATIC_OBJECTS)){
            counter.decrement();
        }
    }

    @Override
    public GameObjectCollection getGameObjectCollection() {
        return this.gameObjectCollection;
    }
}
