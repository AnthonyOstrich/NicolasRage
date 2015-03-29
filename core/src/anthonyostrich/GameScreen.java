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
public class GameScreen implements Screen, InputProcessor, ContactListener {
    World world;
    Box2DDebugRenderer DebugRenderer;
    GameCamera camera;
    Game game;
    Player player;
    SpriteBatch batch;
    Area area;
    TiledMapRenderer mapRenderer;
    Array<Body> bodies = new Array<Body>();
    Random rand = new Random(System.currentTimeMillis());
    Graphics.DisplayMode display;

    public GameScreen(Game screenSwitcher) {
        setUp(screenSwitcher);
    }

    protected void setUp(Game screenSwitcher)
    {
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(this);
        DebugRenderer = new Box2DDebugRenderer();
        player = new Player(world, 15, 15, 1);
        camera = new GameCamera(1f, 1f * ((float) Gdx.graphics.getHeight()) / Gdx.graphics.getWidth(), player);
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
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
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
            {
                ((Actor)b.getUserData()).area = area;
                ((Actor) b.getUserData()).act(delta);
            }
        }
        camera.act(delta);
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
    public void dispose()
    {
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

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        if(bodyA != null && bodyB != null && bodyA.getUserData() != null && bodyB.getUserData() != null)
        {
            ((Actor) contact.getFixtureA().getBody().getUserData()).beginContact((Actor) (contact.getFixtureB().getBody().getUserData()));
            ((Actor) contact.getFixtureB().getBody().getUserData()).beginContact((Actor) (contact.getFixtureA().getBody().getUserData()));
        }
    }

    @Override
    public void endContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        if(bodyA != null && bodyB != null && bodyA.getUserData() != null && bodyB.getUserData() != null)
        {
            ((Actor) contact.getFixtureA().getBody().getUserData()).endContact((Actor) (contact.getFixtureB().getBody().getUserData()));
            ((Actor) contact.getFixtureB().getBody().getUserData()).endContact((Actor) (contact.getFixtureA().getBody().getUserData()));
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}