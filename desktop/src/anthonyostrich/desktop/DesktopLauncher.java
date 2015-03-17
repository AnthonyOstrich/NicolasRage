package anthonyostrich.desktop;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import anthonyostrich.NicolasRage;;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.addIcon("icon.png", Files.FileType.Internal);
        config.fullscreen = false;
        config.title = "Nicolas Rage";
        config.vSyncEnabled = true;
        try{
            Scanner scan = new Scanner(new FileReader("settings.txt"));
            while(scan.hasNext())
            {
                String toProcess = scan.next();
                if(toProcess.contains("="))
                {
                    String[] property = toProcess.split("=", 2);
                    if(property[0].equalsIgnoreCase("fullscreen") && Boolean.parseBoolean(property[1]))
                        config.fullscreen = true;
                    if(property[0].equalsIgnoreCase("width") && Integer.parseInt(property[1]) > 0)
                        config.width = Integer.parseInt(property[1]);
                    if(property[0].equalsIgnoreCase("height") && Integer.parseInt(property[1]) > 0)
                        config.height = Integer.parseInt(property[1]);
                }
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("settings.txt not Found!");
        }
        new LwjglApplication(new NicolasRage(), config);
    }
}