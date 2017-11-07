import java.awt.Color;

/**
 * Created by Xingan Wang on 11/5/17.
 */
public class Element {

    private Color _color;
    private int _energy2;

    Element(Element e) {
        _color = e._color;
        _energy2 = e._energy2;
    }

    Element(Color c) {
        _color = c;
        _energy2 = 1000000;
    }

    public Color getColor() {
        return _color;
    }

    public void setColor(Color color) {
        _color = color;
    }

    public int getEnergy2() {
        return _energy2;
    }

    public void calculateEnergy2(Color cr, Color cl, Color cd, Color ct) {
        _energy2 = squareSum(cr, cl) + squareSum(cd, ct);
    }
    private int squareSum(Color cr, Color cl) {
        int r = cr.getRed() - cl.getRed();
        int g = cr.getGreen() - cl.getGreen();
        int b = cr.getBlue() - cl.getBlue();
        return r * r + g * g + b * b;
    }
}
