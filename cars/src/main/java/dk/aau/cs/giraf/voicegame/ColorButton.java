package dk.aau.cs.giraf.voicegame;

public class ColorButton extends android.widget.Button {
    private int color;

    public ColorButton(android.content.Context context) {
        super(context);
    }

    public ColorButton(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorButton(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void SetColor(int color) {
        this.color = color;
        getBackground().setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public int GetColor() {
        return this.color;
    }
}