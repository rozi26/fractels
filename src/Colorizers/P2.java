package Colorizers;

public class P2 extends Colorize{
    public P2()
    {
        super("reverse gray");
    }

    public int Paint(double brightness)
    {
        final byte c = (byte)(255 - brightness * 255);
        return RGB_to_int(c,c,c);
    }
    public double getSource(int color)
    {
        return 1 - super.getSource(color);
    }
}
