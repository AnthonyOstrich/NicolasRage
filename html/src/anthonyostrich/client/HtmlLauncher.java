package anthonyostrich.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import anthonyostrich.NicolasRage;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig ()
        {

            GwtApplicationConfiguration config =  new GwtApplicationConfiguration(1280, 720);
            config.antialiasing = true;
            return config;
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new NicolasRage();
        }
}