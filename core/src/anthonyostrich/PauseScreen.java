package anthonyostrich;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by anthony on 12/27/14.
 */
public class PauseScreen implements Screen, InputProcessor {

    SpriteBatch batch;
    BitmapFont font;
    GameScreen screen;
    Game game;

    public PauseScreen(Game screenSwitcher, GameScreen paused)
    {
        batch = new SpriteBatch();
        font = new BitmapFont();
        screen = paused;
        game = screenSwitcher;
    }

    @Override
    public boolean keyDown(int keycode) {
        Gdx.graphics.setContinuousRendering(true);
        game.setScreen(screen);
        Gdx.input.setInputProcessor(screen);
        this.dispose();
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        keyDown(Input.Keys.ESCAPE);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.graphics.setContinuousRendering(false);
        screen.render(0);
        batch.begin();
        font.draw(batch, "Paused", 0, 15);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        screen.resize(width, height);
        render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
