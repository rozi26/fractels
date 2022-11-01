package Fractels;

public class Burning_ship extends _Fractle{
    public Burning_ship(int presison)
    {
        super(presison,"burning ship");
    }
    public boolean inRange(_ComplexNumber a, _ComplexNumber b)
    {
        final double temp = a.I;
        a.I = 2 * Math.abs(a.I * a.R) + b.I;
        a.R = a.R*a.R - temp * temp + b.R;
        return Math.abs(a.R) <= 2;
    }
}
