package Fractels;

public class Mangel3 extends _Fractle{
    public Mangel3(int presison)
    {
        super(presison, "mangel3");
    }
    public boolean inRange(_ComplexNumber a, _ComplexNumber b)
    {

        a.third();
        a.add(b);
        return Math.abs(a.R) <= 100 ;
    }
}
