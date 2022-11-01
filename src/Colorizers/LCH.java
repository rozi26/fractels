package Colorizers;

public class LCH extends Colorize{
    public LCH()
    {
        super("LCH");
    }
    public int Paint(double brightness)
    {
        final double s = Math.cos(Math.PI * brightness);
        final double v = s * s;
        final double r = 75 - (75 * v);
        final double g = 28 + r*s;
        final double b = 255 - Math.pow(255 * v,1.5) % 255;
        return RGB_to_int((int)r,(int)g,(int)b);
    }
}
