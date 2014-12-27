package anthonyostrich;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;


public class NicolasRage extends Game{
    @Override
    public void create() {
        GameScreen screen = new GameScreen(this);
        Gdx.input.setInputProcessor(screen);
        this.setScreen(screen);
    }
}
