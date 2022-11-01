
import Colorizers.Colorize;

import Colorizers.P1;

import Colorizers.P4;
import Fractels.Mandelbrot;
import Fractels._ComplexNumber;
import Fractels._Fractle;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FractleBuilder {
    final int TRADES = 10;


    final BufferedImage img;
    final int SCREEN_WIDTH;
    final int SCREEN_HEIGHT;

    double WIDTH;
    double HEIGHT;
    double START_X;
    double START_Y;
    double BUFFER_X;
    double BUFFER_Y;

    int SMART_LINE_LIMIT = 2;
    int SLL = (int)(Math.pow(2,SMART_LINE_LIMIT));

    _Fractle fractle;
    Colorize colorize;
    public FractleBuilder(int _SCREEN_WIDTH, int _SCREEN_HEIGHT, BufferedImage _img)
    {
        SCREEN_HEIGHT = _SCREEN_HEIGHT;
        SCREEN_WIDTH = _SCREEN_WIDTH;
        img = _img;
        resetView();
    }
    public FractleBuilder(int _SCREEN_WIDTH,int _SCREEN_HEIGHT, BufferedImage _img,double render_width,double render_height,double start_x,double start_y, _Fractle frac,Colorize color, int smart_line_limit)
    {
        SCREEN_WIDTH = _SCREEN_WIDTH; SCREEN_HEIGHT = _SCREEN_HEIGHT; img = _img;  WIDTH = render_width; HEIGHT = render_height; START_X = start_x; START_Y = start_y; fractle = frac; colorize = color;
        changeSmartLineLimit(smart_line_limit);
        updateBuffers();
    }


    //public methods
    public void LoadAll()
    {
        LoadLineThreads(0,img.getHeight());
    }
    public void Move(int right, int bottom) throws InterruptedException {
        START_X -= right * (WIDTH / SCREEN_WIDTH);
        START_Y -= bottom * (HEIGHT / SCREEN_HEIGHT);

        final boolean RIGHT = right > 0;
        final boolean DOWN =  bottom > 0;

        /*final int x_start = RIGHT?img.getWidth() - 1:0;
        final int x_add = RIGHT?-1:1;*/

        final int ty1 = DOWN?bottom:0;
        final int ty2 = DOWN?0:Math.abs(bottom);
        final int tx1 = RIGHT?right:0;
        final int tx2 = RIGHT?0:Math.abs(right);

        final int width = img.getWidth() - Math.abs(right);
        final int height = img.getHeight() - Math.abs(bottom);
        img.setRGB(tx1,ty1,width,height,img.getRGB(tx2,ty2,width,height,new int[width * height],0,width),0,width);
        /*int y_source = DOWN?img.getHeight() - bottom - 1:Math.abs(bottom);
        for(int y = y_start; DOWN?(y >= bottom):(y < img.getHeight() + bottom); y += y_add )
        {
            final int width = img.getWidth() - Math.abs(right);
            final int[] get = new int[width];
            img.getRGB(tx2,y_source,width,1,get,0,width * 2);
            img.setRGB(tx1,y,width,1,get,0,width * 2);
            y_source += y_add;
        }*/
        /*final int parts = 2;
        final int buffer = img.getHeight() / parts;
        Thread[] threads = new Thread[4];
        for(int i = 0; i < parts; i++)
        {
            int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    movePart(buffer * finalI,buffer*(finalI + 1),right,bottom);
                }
            };
            threads[i] = new Thread(runnable);
        }
        for(int i = 0; i < parts; i++)
            threads[i].start();
        while (!allThreadsDone(threads))
            Thread.sleep(20);*/
        LoadLine(DOWN?0:img.getHeight() + bottom,DOWN?bottom:img.getHeight());

        final int x_render_start = RIGHT?0:img.getWidth() + right - 1;
        final int x_render_end = RIGHT?right:img.getWidth();
        for(int y = DOWN?bottom:0; DOWN?(y < img.getHeight()):(y < img.getHeight() + bottom); y++)
        {
            LoadSmartLine(y,x_render_start,x_render_end);
        }
    }
    public void Zoom(boolean in, int mouse_x, int mouse_y)
    {
        if(in) Zoom(0.5,mouse_x,mouse_y);
        else Zoom(2,mouse_x,mouse_y);
    }
    public void Zoom(double zoom, int mouse_x, int mouse_y)
    {
        START_X += (getPointX(mouse_x) - START_X) * (1 - zoom);
        START_Y += (getPointY(mouse_y) - START_Y) * (1 - zoom);
        WIDTH *= zoom;
        HEIGHT *= zoom;
        updateBuffers();
        LoadAll();
    }


    //building methods
    private void LoadLineThreads(int from, int to)
    {
        if(TRADES == 1) LoadLine(from,to);
        else
        {
            final int part = img.getHeight() / TRADES;
            final int leftOver = img.getHeight() % TRADES;
            Thread[] threads = new Thread[TRADES];
            for(int i = 0; i < TRADES; i++)
            {
                int finalI = i;
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        LoadLine(finalI * part, (finalI + 1) * part + ((finalI == TRADES - 1)?leftOver:0));
                    }
                };
                threads[i] = new Thread(runnable);
                threads[i].start();
            }
            while (!allThreadsDone(threads))
            {
                try {Thread.sleep(1);}
                catch (InterruptedException e) {throw new RuntimeException(e);}
            }
        }
    }

    private void LoadLine(int start_y, int end_y)
    {
        if(SMART_LINE_LIMIT == 1)
        {
            for(int y = start_y; y < end_y; y++)
                LoadRegularLine(y,0,img.getWidth());
            return;
        }
        double point_y = START_Y + BUFFER_Y * start_y;
        double point_x = getPointX(0);
        for(int y = start_y; y < end_y; y++)
        {
            LoadSmartLine(point_y,point_x,0,img.getWidth(),y);
            point_y += BUFFER_Y;
        }
    }

    private void LoadSmartLine(int y, int start, int end)
    {
        LoadSmartLine(getPointY(y),getPointX(start),start,end,y);
    }
    private void LoadSmartLine(double point_y, double point_x, int x_start, int x_end, int y)
    {
        int lastColor = colorizeBrightness(fractle.getBrightness(point_x,point_y));
        img.setRGB(x_start,y,lastColor);

        int pixel_jump = 2;
        if(x_end - x_start < 3)
        {
            for(int x = x_start + 1; x < x_end; x++)
            {
                point_x += BUFFER_X;
                img.setRGB(x,y,colorizeBrightness(fractle.getBrightness(point_x,point_y)));
            }
            return;
        }
        int x = x_start + pixel_jump;
        while (x < x_end)
        {
            point_x += BUFFER_X * pixel_jump;
            final int color = colorizeBrightness(fractle.getBrightness(point_x,point_y));
            img.setRGB(x,y,color);
            if(color == lastColor)
            {
                for(int i = x - 1; i > x - pixel_jump; i--)
                    img.setRGB(i,y,color);
                if(pixel_jump < SLL)
                    pixel_jump *= 2;
            }
            else
            {
                final int start = x - pixel_jump + 1;
                LoadSmartLine(point_y,getPointX(start),start,x,y);
            }
            lastColor = color;
            x += pixel_jump;
        }
        x -= pixel_jump;
        if(x < x_end)
            LoadSmartLine(point_y,getPointX(x),x,x_end,y);
    }
    private void LoadRegularLine(int y, int start, int end)
    {
        final double point_y = getPointY(y);
        double point_x = getPointX(start);
        for(int x = start; x < end; x++)
        {
            img.setRGB(x,y,colorize.Paint(fractle.getBrightness(point_x,point_y)));
            point_x += BUFFER_X;
        }
    }

    /*private void movePart(int start, int end, int right, int bottom)
    {
        final boolean RIGHT = right > 0;
        final boolean DOWN =  bottom > 0;

        final int y_start = DOWN?end - 1:start;
        final int y_add = DOWN?-1:1;

        final int x_start = RIGHT?img.getWidth() - 1:0;
        final int x_add = RIGHT?-1:1;

        int y_source = DOWN?end - bottom - 1:start + Math.abs(bottom);
        for(int y = y_start; DOWN?(y >= start + bottom):(y < end + bottom); y += y_add)
        {
            int x_source = RIGHT?img.getWidth() - right - 1:Math.abs(right);
            for(int x = x_start; RIGHT?(x >= right):(x < img.getWidth() + right); x += x_add)
            {
                img.setRGB(x,y,img.getRGB(x_source,y_source));
                x_source += x_add;
            }
            y_source += y_add;
        }
    }*/
    //setting methods


    //help method
    private int colorizeBrightness(double b)
    {
        return colorize.Paint(b);
    }
    private boolean allThreadsDone(Thread[] threads)
    {
        for(Thread t:threads)
            if(t.isAlive()) return false;
        return true;
    }
    private void updateBuffers()
    {
        BUFFER_X = WIDTH / SCREEN_WIDTH;
        BUFFER_Y = HEIGHT / SCREEN_HEIGHT;
    }
    public double getPointY(int pixel_y)
    {
        return START_Y + pixel_y * BUFFER_Y;
    }
    public double getPointX(int pixel_x)
    {
        return START_X + pixel_x * BUFFER_X;
    }
    public int getPixelX(double point_x)
    {
        return (int)((point_x - START_X) / BUFFER_X);
    }
    public int getPixelY(double point_y)
    {
        return (int)((point_y - START_Y) / BUFFER_Y);
    }


    //graphic methods
    public int[][] getLinesFromPoint(int x, int y)
    {
        final int LINES_LIMIT = 50;
        int[][] dots = new int[2][LINES_LIMIT];
        final double point_x = getPointX(x);
        final double point_y = getPointY(y);
        _ComplexNumber c;
        _ComplexNumber z;
        if(fractle.isJULIA())
        {
            z = new _ComplexNumber(point_x,point_y);
            c = fractle.getStart();
        }
        else
        {
            c = new _ComplexNumber(point_x,point_y);
            z = c.clone();
        }
        for(int i = 0; i < LINES_LIMIT; i++)
        {
            dots[0][i] = getPixelX(z.getRealValue());
            dots[1][i] = getPixelY(z.getImageryVale());
            //if(z.getRealValue() < START_X || z.getRealValue() > START_X + WIDTH || z.getImageryVale() < START_Y || z.getImageryVale() > START_Y + HEIGHT) break;
            fractle.inRange(z,c);
        }
        return dots;
    }

    //setting methods
    public void changeColorize(Colorize c)
    {
        colorize = c;
        LoadAll();
    }
    public void changeFractle(_Fractle _fractle)
    {
        fractle = _fractle;
        LoadAll();
    }
    public void changePreseason(int depth)
    {
        fractle.precision = depth;
        LoadAll();
    }
    public void changeSmartLineLimit(int to)
    {
        SMART_LINE_LIMIT = to;
        SLL = (int)Math.pow(2,to);
    }
    public void changeSize(double width, double height)
    {
        WIDTH = width;
        HEIGHT = height;
        updateBuffers();
    }
    public void resetView()
    {
        HEIGHT = 2;
        WIDTH = HEIGHT * SCREEN_WIDTH / SCREEN_HEIGHT;
        START_X = -2;
        START_Y = -1;
        updateBuffers();
    }

    //default
}
