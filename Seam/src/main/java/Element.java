import java.awt.Color;

/**
 * Created by Xingan Wang on 11/5/17.
 */
public class Element {

    private Color color;
    private int energy2;

    Element(Element e) {
        color = e.color;
        energy2 = e.energy2;
    }

    Element(Color c) {
        color = c;
        energy2 = 1000000;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getEnergy() {
        return Math.sqrt(energy2);
    }

    public void calculateEnergy2(Color cr, Color cl, Color cd, Color ct) {
        energy2 = squareSum(cr, cl) + squareSum(cd, ct);
    }
    private int squareSum(Color cr, Color cl) {
        int r = cr.getRed() - cl.getRed();
        int g = cr.getGreen() - cl.getGreen();
        int b = cr.getBlue() - cl.getBlue();
        return r * r + g * g + b * b;
    }
}
