package Colorizers;

public class P1 extends Colorize{
    public P1()
    {
        super("gray");
    }

    public int Paint(double brightness)
    {
        final byte c = (byte)(brightness * 255);
        return RGB_to_int(c,c,c);
    }
}
