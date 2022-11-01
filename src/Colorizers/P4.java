package Colorizers;

public class P4 extends Colorize{
    public P4()
    {
        super("blue");
    }
    private double log(double x, int a)
    {
        return Math.log(x) / Math.log(a);
    }
    public int Paint(double brightness)
    {
        final double c = brightness * 255.0;
        return RGB_to_int((int)(log(c,2)*4),(int)(log(c,4)*16),(int)(c));
    }
}
