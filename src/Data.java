public class Data {

    public static double round(double num, int digits)
    {
        final int multer = (int)Math.pow(10,digits);
        return (double)((int)(num * multer)) / multer;
    }
    public static String fixSize(double num, int length)
    {
        String n = Double.toString(num);
        if(n.charAt(0) == '-') length++;
        while (n.length() < length)
            n += "0";
        if(n.length() > length)
            return n.substring(0,length);
        return n;
    }
    private static char byte_to_hax(byte b)
    {
            return (char)((b < 10)?(b + 48):(b + 55));
    }
    public static String colorInt_to_code(int c)
    {
        /*int[] arr = Colorize.int_to_RGB(c);
        System.out.println(Arrays.toString(arr));
        StringBuilder code = new StringBuilder();
        for(int b:arr)
            code.append(byte_to_hax((byte)(b / 16))).append(byte_to_hax((byte)(b % 16)));
        return code.toString();*/
        return "";
    }

    public static int StringToInt(String input, boolean withNegative)
    {
        return (int)(StringToDouble(input,withNegative,false));
    }
    public static double StringToDouble(String input, boolean withNegative)
    {
        return StringToDouble(input,withNegative,true);
    }
    private static double StringToDouble(String input, boolean withNegative, boolean withDots)
    {
        StringBuilder text = new StringBuilder();
        for(int i = 0; i < input.length(); i++)
        {
            final char a = input.charAt(i);
            if(Character.isDigit(a) || (withNegative && a == '-') || (withDots && a =='.'))
                text.append(input.charAt(i));
        }
        final String str = (text.charAt(text.length() - 1) == '.')?text.substring(0,text.length() - 1):text.toString();
        return Double.parseDouble(str);
    }
}
