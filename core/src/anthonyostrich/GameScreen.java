package anthonyostrich;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
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
    TiledMap background;
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
        background = new TmxMapLoader().load("map.tmx");
        TiledMapTileLayer actorMap = (TiledMapTileLayer) (background.getLayers().get("Actors"));
        System.out.println(actorMap);
        for (int x = 0; x < actorMap.getWidth(); x++) {
            for (int y = 0; y < actorMap.getHeight(); y++) {
                if (actorMap.getCell(x, y) != null && actorMap.getCell(x, y).getTile() != null) {
                    TiledMapTile tile = actorMap.getCell(x, y).getTile();
                    ActorFactory factory = ActorFactory.lookup((String) (actorMap.getCell(x, y).getTile().getProperties().get("actor")));
                    if (factory != null)
                        factory.get(Assets.getTexture((String) (tile.getProperties().get("texture"))), world, x + .5f, y + .5f, 1);
                }
            }
        }

        TiledMapTileLayer special = (TiledMapTileLayer) background.getLayers().get("Special");
        for(int y = 0; y < special.getWidth(); y ++)
        {
            for(int x = 0; x < special.getWidth(); x ++)
            {

                int right = x;
                while(right < special.getWidth() && special.getCell(right, y) != null && special.getCell(right, y).getTile() != null && special.getCell(right, y).getTile().getProperties().get("wall").equals("1"))
                {
                    right ++;
                }
                if(right != x)
                {
                    Rectangle r = new Rectangle(x,y, right - x, 1);
                    BodyDef bodyDef = new BodyDef();
                    bodyDef.type = BodyDef.BodyType.StaticBody;
                    Vector2 center = new Vector2();
                    center = r.getCenter(center);
                    bodyDef.position.set(center);
                    Body body = world.createBody(bodyDef);
                    FixtureDef fixtureDef = new FixtureDef();
                    PolygonShape shape = new PolygonShape();
                    shape.setAsBox(r.width/2, r.height/2);
                    fixtureDef.shape = shape;
                    body.createFixture(fixtureDef);
                    shape.dispose();
                }
                x = right;

            }
        }

        TiledMapTileLayer backgroundLayer = (TiledMapTileLayer) (background.getLayers().get("Background"));
        System.out.println(backgroundLayer);
        for (int x = 0; x < backgroundLayer.getWidth(); x++) {
            for (int y = 0; y < backgroundLayer.getHeight(); y++) {
                if ("true".equals(backgroundLayer.getCell(x, y).getTile().getProperties().get("flip"))) {
                    if (rand.nextBoolean())
                        backgroundLayer.getCell(x, y).setFlipHorizontally(true);
                    if (rand.nextBoolean())
                        backgroundLayer.getCell(x, y).setFlipVertically(true);
                }
            }
        }

        for(MapLayer m : background.getLayers())
        {
            if(m != backgroundLayer)
                m.setVisible(false);
        }

        mapRenderer = new OrthogonalTiledMapRenderer(background, 1 / 256f);
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
        batch.begin();
        for (Body b : bodies) {
            if (b.getUserData() instanceof Sprite)
                ((Sprite) b.getUserData()).draw(batch);
        }
        batch.end();
 //       DebugRenderer.render(world, camera.combined);
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            camera.translate(0, delta * 2);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            camera.translate(0, -delta * 2);
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