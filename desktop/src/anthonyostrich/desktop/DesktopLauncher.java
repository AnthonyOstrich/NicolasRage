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
        new LwjglApplication(new NicolasRage(), config);
    }
}