package Video;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Merge {
    final static String pythonExe = "C:/Users/iddor/AppData/Local/Programs/Python/Python310/python.exe";
    final static String filePath = "C:\\Users\\iddor\\IdeaProjects\\Fractels\\src\\Video\\ImageMerger.py";
    public static void generateVideo(String source, String dest, int fps) throws IOException {

        ProcessBuilder builder = new ProcessBuilder(pythonExe,filePath,source,dest,Integer.toString(fps));
        builder.redirectErrorStream(true);
        Process process = builder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String lines = null;
        System.out.println("[");
        while ((lines=reader.readLine()) != null)
            System.out.println("lines: " + lines);
        System.out.println("]");
    }
}
