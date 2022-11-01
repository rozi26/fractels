import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class FileMeneger {
    public static Image getArrayAsImage(int[][][] image)
    {
       return getArrayAsBufferedImage(image);
    }
    public static BufferedImage getArrayAsBufferedImage(int[][][] image)
    {
        BufferedImage bufferedImage = new BufferedImage(image[0].length,image.length,3);
        Graphics g = bufferedImage.getGraphics();
        for(int i = 0; i < image.length; i++)
        {
            for(int j = 0; j < image[0].length; j++)
            {
                try
                {
                    g.setColor(new java.awt.Color(image[i][j][0],image[i][j][1],image[i][j][2]));
                    g.fillRect(j,i,1,1);
                }
                catch (Exception e)
                {
                    // System.out.println("crash color is "  + Arrays.toString(image[i][j]));
                }
            }
        }
        return bufferedImage;
    }
    public static void  saveArrayAsImage(int[][][] image, boolean whiteAlpha, String loc) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(image[0].length, image.length,(whiteAlpha)?BufferedImage.TYPE_INT_ARGB:BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();
        for(int i = 0; i < image.length; i++)
        {
            for(int j = 0; j < image[0].length; j++)
            {
                try
                {
                    if(whiteAlpha)
                        g.setColor(new java.awt.Color(image[i][j][0],image[i][j][1],image[i][j][2] ,image[i][j][3]));
                    else
                        g.setColor(new java.awt.Color(image[i][j][0],image[i][j][1],image[i][j][2]));
                    g.fillRect(j,i,1,1);
                }
                catch (Exception e)
                {
                    // System.out.println("crash color is "  + Arrays.toString(image[i][j]));
                }
            }
        }
        ImageIO.write(bufferedImage, "PNG",new File(loc));
    }
    public static int[][][] getImageAsArray(String source, boolean withAlpha) throws IOException {
        File file = new File(source);
        if (file.exists()) {
            BufferedImage img = ImageIO.read(file);
            int[][][] image = new int[img.getHeight()][img.getWidth()][(withAlpha) ? 4 : 3];
            for (int i = 0; i < image.length; i++) {
                for (int g = 0; g < image[i].length; g++) {
                    int pixel = img.getRGB(g, i);
                    Color color = new Color(pixel, withAlpha);
                    image[i][g][0] = color.getRed();
                    image[i][g][1] = color.getGreen();
                    image[i][g][2] = color.getBlue();
                    if (withAlpha)
                        image[i][g][3] = color.getAlpha();
                }
            }
            return image;
        } else {
            System.out.println("the file in {" + source + "} doesn't exist");
            return new int[0][][];
        }
    }
    public static Image getImageAsImage(String source) throws IOException {
        return ImageIO.read(new File(source));
    }


    public static void delete(String path)
    {
        delete(new File(path));
    }
    public static void delete(File file)
    {
        deleteContent(file);
        file.delete();
    }
    public static void deleteContent(File file)
    {
        if(file.isDirectory())
        {
            File[] sub = file.listFiles();
            if(sub != null)
                for(File f:sub)
                    delete(f);
        }
    }
}
