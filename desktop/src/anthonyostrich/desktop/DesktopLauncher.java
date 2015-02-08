package anthonyostrich.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import anthonyostrich.NicolasRage;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = false;
		config.title = "Nicolas Rage";
		config.vSyncEnabled = true;
		config.addIcon("icon.png", Files.FileType.Internal);
		new LwjglApplication(new NicolasRage(), config);
	}
}
