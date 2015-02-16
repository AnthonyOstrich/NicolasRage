package anthonyostrich;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
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


    public GameScreen(Game screenSwitcher)
    {
        world = new World(new Vector2(0,0), true);
        for(int i = 0; i < 5; i ++)
            new Bee(world, 4, 2 + i);
        DebugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(1f, 1f * ((float)Gdx.graphics.getHeight())/Gdx.graphics.getWidth());
        player = new Player (world, 1, 1, 1, camera);
        new BeeMan (world, 4, 3, 1);
        background = new TmxMapLoader().load("map.tmx");
        MapObjects border = background.getLayers().get("Border").getObjects();
        for(MapObject m : border)
        {
            if(m instanceof RectangleMapObject)
            {
                Rectangle r =((RectangleMapObject) m).getRectangle();
                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                Vector2 center = new Vector2();
                r.x /= 100;
                r.y /= 100;
                r.width /= 100;
                r.height /= 100;

                r.getCenter(center);
                bodyDef.position.set(center);
                Body body = world.createBody(bodyDef);
                FixtureDef fixtureDef = new FixtureDef();
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(r.getWidth() / 2, r.getHeight() / 2);
                fixtureDef.shape = shape;
                Fixture fixture = body.createFixture(fixtureDef);
                shape.dispose();
            }
        }
        TiledMapTileLayer backgroundLayer = (TiledMapTileLayer)(background.getLayers().get(0));
        System.out.println(backgroundLayer);
        for (int x = 0; x < backgroundLayer.getWidth(); x ++)
        {
            for(int y = 0; y < backgroundLayer.getHeight(); y ++)
            {
                if(rand.nextBoolean())
                    backgroundLayer.getCell(x,y).setFlipHorizontally(true);
                if(rand.nextBoolean())
                    backgroundLayer.getCell(x,y).setFlipHorizontally(false);
            }
        }
        mapRenderer = new OrthogonalTiledMapRenderer(background, 1 / 100f );
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

        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            game.setScreen(new GameScreen(game));
        }

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        mapRenderer.setView(camera);
        mapRenderer.render();
        batch.setProjectionMatrix(camera.combined);
        world.getBodies(bodies);
        batch.begin();
        for (Body b : bodies) {
            if(b.getUserData() instanceof Sprite)
                ((Sprite) b.getUserData()).draw(batch);
        }
        batch.end();
//        DebugRenderer.render(world, camera.combined);
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
            camera.translate(0, delta * 2);
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
            camera.translate(0, -delta * 2);
        for (Body b : bodies) {
            if(b.getUserData() != null && b.getUserData() instanceof Actor)
                ((Actor) b.getUserData()).act(delta);
        }

        Vector3 cameraPosition = camera.position.cpy();
        Vector3 playerPosition = new Vector3(player.getX(), player.getY(), 0);

        camera.translate(playerPosition.sub(cameraPosition).scl(delta));

        camera.update();
        world.step(delta, 6, 2);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 1, ((float)height)/width);
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
        camera.setToOrtho(false, 1, ((float)Gdx.graphics.getHeight())/Gdx.graphics.getWidth());
        batch.setProjectionMatrix(camera.combined);
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
        if(keycode == Input.Keys.ESCAPE || keycode == Input.Keys.MENU) {
            pause();
            return true;
        }
        if(keycode == Input.Keys.PAGE_UP) {
            camera.zoom ++;
            camera.update();
            batch.setProjectionMatrix(camera.combined);
            return true;
        }
        if(keycode == Input.Keys.PAGE_DOWN) {
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
