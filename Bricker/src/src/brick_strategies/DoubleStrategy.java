package src.brick_strategies;

import danogl.GameObject;
import danogl.util.Counter;

import java.util.Random;

public class DoubleStrategy extends RemoveBrickStrategyDecorator{

    private CollisionStrategy toBeDecorated;
    private CollisionStrategy toBeDecorated2;

    DoubleStrategy(CollisionStrategy toBeDecorated, CollisionStrategy toBeDecorated2) {
        super(toBeDecorated);
        this.toBeDecorated = toBeDecorated;
        this.toBeDecorated2 = toBeDecorated2;
    }

    /**
     * run the onCollision methods from constructor
      * @param thisObj
     * @param otherObj
     * @param counter
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        toBeDecorated.onCollision(thisObj, otherObj, counter);
        toBeDecorated2.onCollision(thisObj, otherObj, counter);
    }
}
