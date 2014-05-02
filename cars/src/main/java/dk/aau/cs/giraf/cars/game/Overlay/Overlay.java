package dk.aau.cs.giraf.cars.game.Overlay;

import java.util.ArrayList;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.GameState;
import dk.aau.cs.giraf.cars.game.Interfaces.Drawable;
import dk.aau.cs.giraf.cars.game.Interfaces.GameObject;
import dk.aau.cs.giraf.cars.game.Interfaces.Updatable;
import dk.aau.cs.giraf.cars.game.Interfaces.UpdatableGameState;

public abstract class Overlay implements dk.aau.cs.giraf.cars.game.Interfaces.Drawable, UpdatableGameState {

    ArrayList<Drawable> drawables;
    ArrayList<Updatable> updatables;

    public Overlay() {
        drawables = new ArrayList<Drawable>();
        updatables = new ArrayList<Updatable>();
    }

    protected void Add(Drawable d) {
        drawables.add(d);
    }

    protected void Add(Updatable u) {
        updatables.add(u);
    }

    protected void Add(GameObject g) {
        drawables.add(g);
        updatables.add(g);
    }

    @Override
    public void Draw(Graphics graphics, float deltaTime) {
        for (Drawable i : drawables) {
            i.Draw(graphics, deltaTime);
        }

    }


    public GameState Update(Input.TouchEvent[] touchEvents, float deltaTime) {
        for (Updatable i : updatables) {
            i.Update(touchEvents, deltaTime);
        }
        return GameState.Running;
    }
}
