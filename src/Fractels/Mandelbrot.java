package Fractels;

public class Mandelbrot extends _Fractle {
    final private int RANGE = 2;

    public Mandelbrot(int precision)
    {
        super(precision,"mandelbort");
    }
    public boolean inRange(_ComplexNumber a, _ComplexNumber b)
    {
        /*_ComplexNumber t = a.clone();
        a.square();
        a.add(b);
        t.add(b);
        t.devoid(new _ComplexNumber(2,2));
        a.devoid(t);*/
        a.square();
        a.add(b);
        return Math.abs(a.R) <= 2;
    }
}
