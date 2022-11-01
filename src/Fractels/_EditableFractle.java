package Fractels;

public class _EditableFractle extends _Fractle{
    public double var;
    public _EditableFractle(int presison, String name, double editableValue)
    {
        super(presison,name);
        super.EDITABLE = true;
        var = editableValue;
    }
    public boolean inRange(_ComplexNumber a, _ComplexNumber b)
    {
        System.out.println("error in editable range");
        return false;
    }

    public double getVar()
    {
        return var;
    }
    public void changeVar(double to){var = to;}
    public void addToVar(double add){
        //System.out.println("add (" + add + ") (" + var + ")");
        var += add;
    }


}
