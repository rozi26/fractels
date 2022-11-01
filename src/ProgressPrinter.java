public class ProgressPrinter {

    final long startTime;
    final int goal;
    int current = 0;
    public ProgressPrinter(final int _goal)
    {
        startTime = System.currentTimeMillis();
        goal = _goal;
    }

    public void Forward()
    {
        current++;
        print();
    }
    private void print()
    {
        final long time = System.currentTimeMillis();
        String text = "run " + niceWrite(current) + "\\" + niceWrite(goal) + " (" + presentOf(current,goal) + "%) times,\t";
        text += "in " + getTimeDifferentS(startTime,time) + ",\t time left: " + predictTime(current, goal - current,time - startTime);
        text += ".\t(average load time: " + getTimeDifferent(0,(time - startTime) / current) + ")";
        System.out.print(((char)13) + text);
    }

    private static String niceWrite(int _num)
    {
        final String num = Integer.toString(_num);
        StringBuilder text = new StringBuilder();
        int count = 0;
        for(int i = num.length() - 1; i > 0; i--)
        {
            text.append(num.charAt(i));
            count++;
            if(count == 3)
            {
                text.append(",");
                count = 0;
            }
        }
        text.append(num.charAt(0));
        return text.reverse().toString();
    }
    private static double presentOf(int num, int from)
    {
        return ((int)(((double)num / from * 100) * 100)) / 100.0;
    }
    private static double round(double num, int digits)
    {
        final int multer = (int)Math.pow(10,digits);
        return (double)((int)(num * multer)) / multer;
    }
    public static String getTimeDifferent(long start, long end) {
        String time = getTimeDifferentS(start,end);
        if(time.charAt(0) == '0') time = "";
        else time += ", ";
        time += ((end - start) %  1000) + " milliseconds";
        return time;
    }
    public static String getTimeDifferentS(long start, long end)
    {
        String time = "";
        long different = end - start;
        if (different >= 86400000)
        {
            time += (different / 86400000) + " days, ";
            different %= 86400000;
        }
        if(different >= 3600000)
        {
            time += (different / 3600000) + " hours, ";
            different %= 3600000;
        }
        if(different >= 60000)
        {
            time += (different / 60000) + " minutes, ";
            different %= 60000;
        }
        time += (different / 1000) + " seconds";
        return time;
    }
    public static String predictTime(int pass, int left, long sofar)
    {
        return getTimeDifferentS(0,(int)(((double) sofar / pass) * left));
    }
}
