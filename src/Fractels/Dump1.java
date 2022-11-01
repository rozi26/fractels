package Fractels;

public class Dump1 {
    public static class Dragon extends _Fractle
    {
        public Dragon(int presison)
        {
            super(presison,"dragon");
        }
        public boolean inRange(_ComplexNumber a, _ComplexNumber b)
        {
            _ComplexNumber zz = a.clone();
            zz.square();
            _ComplexNumber dev = zz.clone();
            dev.multiply(a);
            dev.add(b);
            zz.add(a);
            zz.devoid(dev);
            a = zz.clone();
            return Math.abs(a.getRealValue()) <= 2;
        }
    }
    public static class PixelEgg extends _Fractle
    {
        public PixelEgg(int presison)
        {
            super(presison,"pixel_egg");
        }
        public boolean inRange(_ComplexNumber a, _ComplexNumber b)
        {
            a.square();
            a.I  += a.R;
            a.add(b);
            return Math.abs(a.R) <= 2;
        }
    }


}
