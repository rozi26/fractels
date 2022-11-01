package Fractels;

public class Feather extends _Fractle{
    public Feather(int presison)
    {
        super(presison,"feather");
    }
    public boolean inRange(_ComplexNumber a, _ComplexNumber b)
    {
        _ComplexNumber temp = a.clone();
        temp.square();
        a.multiply(temp);
        temp.R++;
        a.devoid(temp);
        a.add(b);
        return Math.abs(a.R) <= 2;
    }
}
