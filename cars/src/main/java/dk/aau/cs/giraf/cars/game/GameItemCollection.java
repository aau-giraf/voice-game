package dk.aau.cs.giraf.cars.game;

import java.util.ArrayList;

import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Interfaces.GameObject;

public class GameItemCollection implements GameObject {
    private ArrayList<RoadItem> roadItems;
    private RoadItemGenerator generator;

    private static final int ROADITEM_SIZE = 100;

    public GameItemCollection(RoadItemGenerator generator) {
        this.generator = generator;
        this.roadItems = new ArrayList<RoadItem>();
    }

    public void resetRoadItems() {
        this.roadItems.clear();
        for (RoadItem roadItems : this.generator.CreateRoadItems(ROADITEM_SIZE, ROADITEM_SIZE))
            this.roadItems.add(roadItems);
    }

    public RoadItem findCollision(GameItem gameItem) {
        for (RoadItem roadItem : roadItems)
            if (roadItem.CollidesWith(gameItem))
                return roadItem;
        return null;
    }

    public void removeRoadItem(RoadItem roadItem) {
        roadItems.remove(roadItem);
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
