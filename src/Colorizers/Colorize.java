package Colorizers;

import java.awt.image.BufferedImage;

public class Colorize{
    final String name;
    protected Colorize(String _name)
    {
        name = _name;
    }

    protected int RGB_to_int(byte r, byte g, byte b)
    {
        return r*65536 + g*256 + b;
    }
    protected int RGB_to_int(int r, int g, int b)
    {
        return r*65536 + g*256+b;
    }
    protected int RGB_to_int(double r,double g,double b)
    {
        return RGB_to_int((int)r,(int)g,(int)b);
    }

    public int HSV_to_int(double h, double s, double v)
    {
        return HSV_to_int((int)(h*360),s,v);
    }
    public int HSV_to_int(int h, double s, double v)
    {
        final double M = v * s;
        final double m = M*(1-Math.abs((h / 60.0) % 2 - 1));
        final double z = v - M;
        double r;
        double g;
        double b;
        if(h < 60)
        {
            r = M;
            g = m;
            b = 0;
        }
        else if(h < 120)
        {
            r = m;
            g = M;
            b = 0;
        }
        else if(h < 180)
        {
            r = 0;
            g = M;
            b = m;
        }
        else if(h < 240)
        {
            r = 0;
            g = m;
            b = M;
        }
        else if(h < 300)
        {
            r = m;
            g = 0;
            b = M;
        }
        else
        {
            r = M;
            g = 0;
            b = m;
        }
        return RGB_to_int((r + z) * 255,(g + z) * 255,(b + z) * 255);
    }

    public static int[] int_to_RGB(int a) {
        return new int[]{a / 65536,a % 65536 / 256,a % 256};
    }
    public int Paint(double brightness)
    {
        return 0;
    }
    protected double getSource(int color){return ((color % 256) / 255.0);}

    public void switchColors(BufferedImage img, Colorize newColor)
    {
        for(int y = 0; y < img.getHeight(); y++)
        {
            for(int x =0; x < img.getWidth(); x++)
            {
                img.setRGB(x,y,newColor.Paint(getSource(img.getRGB(x,y))));
            }
        }
    }

    public static Colorize[] getMethods()
    {
        return new Colorize[]{
                new P1(),
                new P2(),
                new P4(),
                new Orange(),
                new LCH()
        };
    }
    public String getName(){return name;}
}
