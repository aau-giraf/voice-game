package dk.aau.cs.giraf.cars.game.Overlay;

import dk.aau.cs.giraf.cars.R;
import dk.aau.cs.giraf.cars.framework.Game;
import dk.aau.cs.giraf.cars.framework.Graphics;
import dk.aau.cs.giraf.cars.framework.Input;
import dk.aau.cs.giraf.cars.game.Assets;
import dk.aau.cs.giraf.cars.game.Overlay.Overlay;
import dk.aau.cs.giraf.cars.game.Overlay.OverlayButton;

public class WinningOverlay extends Overlay {
    private OverlayButton resetButton;
    private OverlayButton menuButton;

    public WinningOverlay(Game game) {
        super(game);

        int width = game.getWidth();
        int height = game.getHeight();

        resetButton = new OverlayButton(game, (int) (width * 0.25), (int) (height * 0.65), game.getResources().getString(R.string.play_again_button_text));
        menuButton = new OverlayButton(game, (int) (width * 0.75), (int) (height * 0.65), game.getResources().getString(R.string.menu_button_text));

        Add(resetButton);
        Add(menuButton);
    }

    public boolean ResetButtonPressed(Input.TouchEvent[] events) {
        return resetButton.IsButtonPressed(events);
    }

    public boolean MenuButtonPressed(Input.TouchEvent[] events) {
        return menuButton.IsButtonPressed(events);
    }

    @Override

    public void Draw(Graphics graphics, float deltaTime) {
        int width = game.getWidth();
        int height = game.getHeight();
        graphics.drawARGB(155, 0, 0, 0);
        graphics.drawImage(Assets.GetTrophy(), (int) (width * .50) - Assets.GetTrophy().getWidth() / 2, 0);
        super.Draw(graphics, deltaTime);
    }
}
