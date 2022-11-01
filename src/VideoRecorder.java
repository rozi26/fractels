import Colorizers.Colorize;
import Colorizers.Orange;
import Fractels.Mandelbrot;
import Fractels._ComplexNumber;
import Fractels._EditableFractle;
import Fractels._Fractle;
import Video.Merge;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;


public class VideoRecorder {
    final String path;
    public int Presison = 1000;
    public int Width = 2000;
    public int Height = 2000;
    public _Fractle fractle = new Mandelbrot(Presison);
    public Colorize colorize = new Orange();
    public int FPS = 30;
    public int SSL = 1;

    private ImageSaver saver;
    private FractleBuilder builder;

    public VideoRecorder(String _path) throws IOException {
        path = _path;
    }

    private void getBuilder()
    {
        BufferedImage img = new BufferedImage(Width,Height,BufferedImage.TYPE_INT_RGB);
        saver = new ImageSaver(path + "_frames",img,FPS);

        builder = new FractleBuilder(Width,Height,img);
        builder.colorize = colorize;
        builder.fractle = fractle;
        builder.changeSmartLineLimit(SSL);
    }

    private static class ImageSaver
    {
        final String framesPath;
        final File framesFolder;
        final BufferedImage img;
        final int FPS;

        int frames = 0;
        private ImageSaver(String path, BufferedImage IMG, int fps)
        {
            framesPath = path;
            framesFolder = new File(path);
            if(framesFolder.exists()){ FileMeneger.deleteContent(framesFolder);}
            else System.out.println(framesFolder.mkdir()?"":"fail to create folder");
            img = IMG;
            FPS = fps;
        }

        private void addFrame() throws IOException {
            ImageIO.write(img,"png",new File(framesPath + "\\image" + frames + ".png"));
            frames++;
        }

        private void makeVideo(String videoPath) throws IOException {
            if(!videoPath.substring(videoPath.length() - 4).equals("mp4"))
                videoPath += ".mp4";
            Merge.generateVideo(framesPath,videoPath,FPS);
        }

        private void deleteFrames()
        {
            FileMeneger.delete(framesFolder);
        }
    }


    //view methods
    public void ZoomIn(double TO_X, double TO_Y, double zoom_exponent, int frames) throws IOException {
        ZoomIn(TO_X,TO_Y,zoom_exponent,frames,2,((double)Width/Height)*2);
    }
    public void ZoomIn(double TO_X, double TO_Y, double zoom_exponent, int frames, double width, double height) throws IOException {
        getBuilder();
        builder.changeSize(width,height);
        //set the zoom point to be in the middle of the screen
        builder.START_X = TO_X - builder.WIDTH / 2;
        builder.START_Y = TO_Y - builder.HEIGHT / 2;

        Object[] args = new Object[]{zoom_exponent,builder.getPixelX(TO_X),builder.getPixelY(TO_Y)};
        Consumer<Object[]> consumer = o -> builder.Zoom((double)o[0],(int)o[1],(int)o[2]);

        LoadFrames(consumer,args,frames);
    }
    public void MoveEditable(_EditableFractle editableFractle, double start, double end, int frames) throws IOException {
        MoveEditable(editableFractle,start,end,frames,-1.5,-1.5,3,3);
    }
    public void MoveEditable(_EditableFractle editableFractle, double start, double end, int frames, double start_x, double start_y, double width, double height) throws IOException {
        fractle = editableFractle;
        getBuilder();

        builder.START_X = start_x; builder.START_Y = start_y; builder.changeSize(width,height);

        final double buffer = (end - start) / frames;

        Object[] args = new Object[]{buffer};
        Consumer<Object[]> consumer = o -> {builder.fractle.addToVar((double)o[0]); builder.LoadAll();};
        LoadFrames(consumer,args,frames);
    }



    private void LoadFrames(Consumer<Object[]> consumer, Object[] args, int frames) throws IOException {
        saver.addFrame();
        ProgressPrinter printer = new ProgressPrinter(frames);
        for(int i = 0; i < frames - 1; i++)
        {
            consumer.accept(args);
            saver.addFrame();
            printer.Forward();
        }
        saver.makeVideo(path);
    }


    //setting methods
    public void setPresison(int presison)
    {
        Presison = presison;
        fractle.precision = presison;
    }
}
