package Fractels;

public class MendelEdit  extends _EditableFractle{
    public MendelEdit(int presison)
    {
        super(presison,"mendel_edit",2);
    }
    public boolean inRange(_ComplexNumber a, _ComplexNumber b)
    {
        a.power(getVar());
        a.add(b);
        return Math.abs(a.R) <= 2;
    }
}
