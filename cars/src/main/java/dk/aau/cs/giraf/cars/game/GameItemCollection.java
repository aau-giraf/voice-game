package dk.aau.cs.giraf.cars.game;

import java.util.ArrayList;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Interfaces.GameObject;

public class GameItemCollection implements GameObject {
    private ArrayList<PickupItem> pickupItems;
    private ArrayList<Obstacle> obstacles;
    private RoadItemGenerator generator;
    
    private static final int ROADITEM_SIZE = 100;

    public GameItemCollection(RoadItemGenerator generator) {
        this.generator = generator;
        if (gameMode == GameMode.pickup)
            pickupItems = new ArrayList<PickupItem>();
        if (gameMode == GameMode.avoid)
            obstacles = new ArrayList<Obstacle>();
    }

    public void resetRoadItems() {
        if (gameMode == GameMode.pickup) {
            this.pickupItems.clear();
            for (PickupItem pickupItem : this.generator.CreatePickupItems(ROADITEM_SIZE, ROADITEM_SIZE))
                this.pickupItems.add(pickupItem);
        }
        if (gameMode == GameMode.avoid) {
            this.pickupItems.clear();
            for (Obstacle obstacle : this.generator.CreateObstacles(ROADITEM_SIZE, ROADITEM_SIZE))
                this.obstacles.add(obstacle);
        }
    }

    public RoadItem findCollision(GameItem gameItem) {
        if (gameMode == GameMode.pickup){
            for (PickupItem pickupItem : pickupItems)
                if (pickupItem.CollidesWith(gameItem))
                    return pickupItem;
            return null;
        }
        if (gameMode == GameMode.pickup){
            for (PickupItem pickupItem : pickupItems)
                if (pickupItem.CollidesWith(gameItem))
                    return pickupItem;
            return null;
        }
        return
    }

    public void removeObstacle(Obstacle obstacle){
        roadItems.remove(obstacle);
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        for (RoadItem roadItem : roadItems)
            roadItem.Draw(graphics, deltaTime);
    }

    @Override
    public void Update(Input.TouchEvent[] touchEvents, float deltaTime) {
        for (RoadItem roadItem : roadItems)
            roadItem.Update(touchEvents, deltaTime);
    }
}
