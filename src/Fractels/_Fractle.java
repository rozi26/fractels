package Fractels;

public class _Fractle {
    final private String name;

    protected boolean JULIA;
    protected boolean EDITABLE;

    protected _ComplexNumber start;
    public int precision;

    public _Fractle(int _precision, String _name)
    {
        precision = _precision;
        JULIA = false;
        start = new _ComplexNumber(0,0);
        name = _name;
    }


    public double getBrightness(double x, double y)
    {
        _ComplexNumber z;
        _ComplexNumber c;
        if(isJULIA())
        {
            c = start.clone();
            z = new _ComplexNumber(x,y);
        }
        else
        {
            c = new _ComplexNumber(x,y);
            z = start.clone();
        }
        int n = 0;
        while (inRange(z,c) && n < precision)
            n++;
        //System.out.println("(" + x + "," + y +") got " + (int)((double)n / precision) +" (" + z + ")");
        return 1 - ((double)n) / precision;
        //return (n + 1 - Math.log(Math.log(Math.abs(z.getRealValue())) / Math.log(2))) / precision;
    }

    public boolean inRange(_ComplexNumber a, _ComplexNumber b){
        System.out.println("error in range from fractel");
        return false;
    }

    public void setStart(_ComplexNumber new_start)
    {
        start = new_start;
        JULIA = true;
    }
    public void cancelJulia(){
        start = new _ComplexNumber(0,0);
        JULIA = false;
    }
    public _ComplexNumber getStart()
    {
        return start.clone();
    }

    public String getName(){return name;}
    public boolean isJULIA()
    {
        return JULIA;
    }
    public boolean isEDITABLE(){return EDITABLE;}

    public static _Fractle[] getFractals(int precision)
    {
        return new _Fractle[]
                {
                        new Mandelbrot(precision),
                        new Mangel3(precision),
                        new Magebrot(precision),
                        new Burning_ship(precision),
                        new Egael(precision),
                        new Feather(precision),
                        new Tricorn(precision),
                        new Dump1.PixelEgg(precision),
                        new Dump1.Dragon(precision),
                        new MendelEdit(precision),
                };
    }

    public void addToVar(double i) {
        System.out.println("add to var from inside");
    }
    public void changeVar(double i)
    {
        System.out.println("set var from inside");
    }
    public double getVar()
    {
        return 1;
    }
}
