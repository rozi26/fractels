package Fractels;

public class Magebrot extends _Fractle{
    public Magebrot(int preseason)
    {
        super(preseason, "megabort");
    }
    public boolean inRange(_ComplexNumber a, _ComplexNumber b)
    {
        a.square();
        a.square();
        a.add(b);
        return Math.abs(a.R) <= 2;
    }
}
