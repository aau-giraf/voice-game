package dk.aau.cs.giraf.cars.game.Controller;

    import android.util.Log;

    import dk.aau.cs.giraf.cars.framework.Game;
    import dk.aau.cs.giraf.cars.framework.Input;
    import dk.aau.cs.giraf.cars.game.Car;
    import dk.aau.cs.giraf.cars.game.Interfaces.CarControl;
    private int width, height, bottomOffset;

    public class TouchCarControl implements CarControl {
        private int lastMove = 1;
        private float y = -1;
    public TouchCarControl(int gameWidth, int gameHeight) {
        this.width = gameWidth;
        this.height = (int)(gameHeight * 0.25);
        this.bottomOffset = gameHeight - this.height;
    }

        public TouchCarControl() {
        }

    public float getMove(Input.TouchEvent[] touchEvents, Car car) {
        int width = game.getWidth();
        int height = (int) (game.getHeight() - 2*70);//grassSize

        int len = touchEvents.length;
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents[i];
            if (event.type == Input.TouchEvent.TOUCH_DRAGGED || event.type == Input.TouchEvent.TOUCH_DOWN ) {
                if (inBounds(event, 0, 70, width, height)) {
                    y=event.y;
                }
            }
            if (event.type == Input.TouchEvent.TOUCH_UP){
                lastMove=1;
                y=-1;
                return lastMove;
            }
        }

        if (y!=-1) {
            if (y < car.getY()-10 + (car.getHeight()/2))
                lastMove = -1;
            else if (y > car.getY()+10 + (car.getHeight()/2))
                lastMove = 1;
            else
            {
                lastMove=0;
            }
        }

        return lastMove;
    }

    @Override
    public void Reset()
    {
        lastMove=1;
        y=-1;
    }

    @Override
    public int getBarometerNumber(float y, float height)
    {
        return Math.round(10-(y/(height/10)));
    }

    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        return event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1;
    }
}
