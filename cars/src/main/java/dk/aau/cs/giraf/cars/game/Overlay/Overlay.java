package dk.aau.cs.giraf.cars.game.Overlay;

import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.game.Interfaces.Drawable;
import dk.aau.cs.giraf.cars.game.Interfaces.GameObject;
import dk.aau.cs.giraf.cars.game.Interfaces.Updatable;

import java.util.ArrayList;

public abstract class Overlay {

    ArrayList<Drawable> drawables;
    ArrayList<Updatable> updatables;
    Game game;

    public Overlay(Game game) {
        this.game = game;
        drawables = new ArrayList<Drawable>();
        updatables = new ArrayList<Updatable>();
    }

    protected void Add(Drawable d)
    {
            drawables.add(d);
    }

    protected void Add(Updatable u)
    {
        updatables.add(u);
    }

    protected void Add(GameObject g)
    {
        drawables.add(g);
        updatables.add(g);
    }


    public void Draw(Graphics graphics, float deltaTime) {
        for (Drawable i : drawables) {
            i.Draw(graphics,deltaTime);
        }

    }

    public void Update(float deltaTime){
        for (Updatable i : updatables) {
            i.Update(deltaTime);
        }

    }
}
