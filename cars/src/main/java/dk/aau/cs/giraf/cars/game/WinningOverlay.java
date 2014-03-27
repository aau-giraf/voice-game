package dk.aau.cs.giraf.cars.game;

import android.content.Intent;
import android.graphics.Paint;

import java.util.List;

import android.util.Log;
import dk.aau.cs.giraf.cars.MainMenu;
import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.framework.Screen;

public class WinningOverlay extends Overlay {
    private GameSettings gameSettings;

    public WinningOverlay(){ super(); }

    public WinningOverlay(GameSettings gs){
        gameSettings = gs;
    }

    @Override
    public GameState ButtonPressed(Game game)
    {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int width = game.getWidth();
        int height = game.getHeight();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                if (inBounds(event, 0,height/2,width/2, height/2)) {
                    Log.d("Settings","Spil igen");
                    game.setScreen(new GameScreen(game, new TestObstacles(),gameSettings));
                    return GameState.Running;
                    }
                if (inBounds(event, width/2, height/2, width/2, height/2)) {
                    Log.d("Settings","Menu");
                    Intent intent = new Intent(game, MainMenu.class);
                    intent.putExtra("GameSettings",gameSettings);
                    game.startActivity(intent);
                }
            }
        }
        return GameState.Won;
    }

    public void Draw(Game game)
    {
        int width = game.getWidth();
        int height = game.getHeight();
        Graphics g = game.getGraphics();
        g.drawARGB(155, 0, 0, 0);
        g.drawImage(Assets.GetTrophy(), (int) (width * .50) - Assets.GetTrophy().getWidth() / 2, (int) (height * .25) - Assets.GetTrophy().getHeight() / 2);
        g.drawString(game.getResources().getString(R.string.play_again_button_text), (int) (width * .25), (int) (height * .85), pButton);
        g.drawString(game.getResources().getString(R.string.menu_button_text), (int)(width*.75), (int)(height*.85), pButton);
    }
}
