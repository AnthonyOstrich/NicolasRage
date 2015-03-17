package anthonyostrich;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import java.util.Random;
/**
 * Created by anthony on 12/23/14.
 */
public class GameScreen implements Screen, InputProcessor {
    World world;
    Box2DDebugRenderer DebugRenderer;
    OrthographicCamera camera;
    Game game;
    Player player;
    SpriteBatch batch;
    Area area;
    TiledMapRenderer mapRenderer;
    Array<Body> bodies = new Array<Body>();
    Random rand = new Random(System.currentTimeMillis());

    public GameScreen(Game screenSwitcher) {
        world = new World(new Vector2(0, 0), true);
        DebugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(1f, 1f * ((float) Gdx.graphics.getHeight()) / Gdx.graphics.getWidth());
        player = new Player(world, 15, 15, 1, camera);
        camera.zoom = 10;
        camera.update();
        area = new Area("map.tmx");
        area.addToWorld(world, rand);
        mapRenderer = new OrthogonalTiledMapRenderer(area.getMap(), 1 / 256f);
        camera.zoom += 10;
        camera.update();
        game = screenSwitcher;
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        Gdx.input.setCursorImage(new Pixmap(Gdx.files.internal("cursor.png")), 16, 16);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.setContinuousRendering(true);
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            this.dispose();
            game.setScreen(new GameScreen(game));
            return;
        }
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        mapRenderer.setView(camera);
        mapRenderer.render();
        batch.setProjectionMatrix(camera.combined);
        world.getBodies(bodies);
        Array<Actor> toDraw = new Array<Actor>();
        for(Body b : bodies)
        {
            if(b.getUserData() instanceof Actor)
                toDraw.add((Actor)b.getUserData());
        }
        toDraw.sort();
        batch.begin();
        for (Actor a : toDraw) {
            a.draw(batch);
        }
        batch.end();
 //       DebugRenderer.render(world, camera.combined);
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            camera.translate(0, delta * 2);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            camera.translate(0, -delta * 2);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            player.shoot(ActorFactory.lookup("fireball"), 6);
        for (Body b : bodies) {
            if (b.getUserData() != null && b.getUserData() instanceof Actor)
                ((Actor) b.getUserData()).act(delta);
        }
        Vector3 cameraPosition = camera.position.cpy();
        Vector3 playerPosition = new Vector3(player.getX(), player.getY(), 0);
        camera.translate(playerPosition.sub(cameraPosition).scl(delta * 1.5f));
        camera.update();
        world.step(delta, 6, 2);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 1, ((float) height) / width);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {
        PauseScreen pauseScreen = new PauseScreen(game, this);
        game.setScreen(pauseScreen);
        Gdx.input.setInputProcessor(pauseScreen);
    }

    @Override
    public void resume() {
        Vector3 cameraPosition = camera.position;
        camera.setToOrtho(false, 1, ((float) Gdx.graphics.getHeight()) / Gdx.graphics.getWidth());
        batch.setProjectionMatrix(camera.combined);
        camera.translate(cameraPosition.sub(camera.position));
        camera.update();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        world.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.MENU) {
            pause();
            return true;
        }
        if (keycode == Input.Keys.PAGE_UP) {
            camera.zoom++;
            camera.update();
            batch.setProjectionMatrix(camera.combined);
            return true;
        }
        if (keycode == Input.Keys.PAGE_DOWN) {
            camera.zoom--;
            camera.update();
            batch.setProjectionMatrix(camera.combined);
            return true;
        }
        return false;
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
        return false;
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
}