package anthonyostrich;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by anthony on 12/23/14.
 */
public class GameScreen implements Screen, InputProcessor {

    World world;
    Box2DDebugRenderer renderer;
    OrthographicCamera camera;
    Body body;
    Game game;
    Sprite cage;
    SpriteBatch batch;

    public GameScreen(Game screenSwitcher)
    {
        world = new World(new Vector2(0,0), true);
        renderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(1.0f, ((float)Gdx.graphics.getHeight())/Gdx.graphics.getWidth());
        game = screenSwitcher;
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0,0);
        bodyDef.linearDamping = .9f;
        bodyDef.angularDamping = 5f;

        body = world.createBody(bodyDef);


        CircleShape circle = new CircleShape();
        circle.setRadius(.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        Fixture fixture = body.createFixture(fixtureDef);
        circle.dispose();

        cage = new Sprite(new Texture(Gdx.files.internal("Cage.png")));
        cage.setSize(1,cage.getHeight()/cage.getWidth());
        cage.setOriginCenter();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        cage.setPosition(body.getPosition().x - (cage.getWidth() / 2), body.getPosition().y - (cage.getHeight() / 2));
        cage.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        batch.begin();
        cage.draw(batch);
        batch.end();

        renderer.render(world, camera.combined);


        if(Gdx.input.isKeyPressed(Input.Keys.A))
            body.setTransform(body.getPosition(), body.getAngle() + ((float)(Math.PI) * delta));
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            body.setTransform(body.getPosition(), body.getAngle() + ((float)(Math.PI) * -delta));
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            Vector2 facing = new Vector2(10, 0);
            facing.rotateRad(body.getAngle());
            body.applyForce(facing, new Vector2(0, 0), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            Vector2 facing = new Vector2(-1f, 0);
            facing.rotateRad(body.getAngle());
            body.applyForce(facing, new Vector2(0, 0), true);
        }


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
        renderer.dispose();
        world.dispose();
    }



    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ESCAPE) {
            pause();
            return true;
        }
        if(keycode == Input.Keys.UP) {
            camera.zoom ++;
            camera.update();
            batch.setProjectionMatrix(camera.combined);
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
        Vector3 pointerLocation = new Vector3(screenX, screenY, 0);
        camera.unproject(pointerLocation);
        body.setTransform(pointerLocation.x, pointerLocation.y, body.getAngle());
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
}
