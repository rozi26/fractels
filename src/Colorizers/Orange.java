package Colorizers;

public class Orange extends Colorize {
    public Orange()
    {
        super("Orange");
    }
    public int Paint(double brightness)
    {
        brightness = 1 - brightness;
        return HSV_to_int(brightness,1,(brightness != 1)?1:0 );
    }
}
