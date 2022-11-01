package Fractels;

public class _ComplexNumber {
    double R;
    double I;
    public _ComplexNumber(double real, double imagery)
    {
        R = real;
        I = imagery;
    }
    public void add(_ComplexNumber num)
    {
        R += num.R;
        I += num.I;
    }
    public void sub(_ComplexNumber num)
    {
        R -= num.R;
        I -= num.I;
    }
    public void multiply(_ComplexNumber num)
    {
        final double T = R;
        R = R * num.R - I * num.I;
        I = T * num.I + I * num.R;
    }
    public void devoid(_ComplexNumber num)
    {
        final double temp = R;
        final double dev = (num.R*num.R + num.I*num.I);
        R = (R*num.R + I * num.I) / dev;
        I = (I*num.R - temp*num.I) / dev;
    }

    public void sqrt2(boolean positive)
    {
        double t = Math.sqrt((Math.sqrt(R*R+I*I) - R) / 2);
        if(!positive) t = -t;
        R = I / (t * 2);
        I = t;
    }
    public void square()
    {
        final double T = R;
        R = R * R - I * I;
        I = 2 * T * I;
    }
    public void third()
    {
        final double temp = R * R;
        R = temp * R  - 3 * I * I * R;
        I = 3 * temp * I - Math.pow(I,3);
    }

    public void power(double degree)
    {
        sqrt(1/degree);
    }
    public void sqrt(double degree)
    {
        final double radius = Math.pow(Math.sqrt(R*R + I*I),1/degree);
        final double O = ((R == 0?0:Math.atan(I/R)) + Math.PI) / degree;
        R = radius * Math.cos(O);
        I = radius * Math.sin(O);
    }

    public _ComplexNumber clone()
    {
        return new _ComplexNumber(R,I);
    }

    public double getRealValue(){return R;}
    public double getImageryVale(){return I;}

    public String toString()
    {
        if(I < 0)
            return R + " - " + Math.abs(I) + "i";
        return R + " + " + I + "i";
    }
}
