package Fractels;

public class Tricorn extends _Fractle{
    public Tricorn(int presison)
    {
        super(presison,"tricorn");
    }
    public boolean inRange(_ComplexNumber a, _ComplexNumber b)
    {
        a.square();
        a.I = -a.I;
        a.add(b);
        return Math.abs(a.R) <= 2;
    }
}
