package Fractels;

public class Egael  extends _Fractle{
    public Egael(int presison)
    {
        super(presison,"egael");
    }
    public boolean inRange(_ComplexNumber a, _ComplexNumber b) {
        a.sqrt(0.66666);
        //a.sqrt(1.1);
        a.add(b);
        return Math.abs(a.R) <= 2;
    }
}
