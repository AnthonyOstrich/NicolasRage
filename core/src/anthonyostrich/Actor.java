package anthonyostrich;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by anthony on 12/27/14.
 */
public class Actor extends Sprite{
    Body body;
    protected Shape shape;
    boolean flipped = false;

    public Actor(Texture texture, Shape objectShape, World world, float x, float y, float width)
    {
        super(texture);
        this.setSize(width, width * this.getHeight() / this.getWidth());
        this.setOriginCenter();
        shape = objectShape;
        shape.setRadius(this.getWidth()/2);
        this.addToWold(world, x, y);
    }

    public Actor(Texture texture, World world, float x, float y, float width)
    {
        super(texture);
        this.setSize(width, width * this.getHeight() / this.getWidth());
        this.setOriginCenter();
        shape = new PolygonShape();
        ((PolygonShape) shape).setAsBox(this.getWidth()/2, this.getHeight()/2);
        this.addToWold(world, x, y);
    }


    protected void addToWold(World world, float x, float y)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.linearDamping = .9f;
        bodyDef.angularDamping = 5f;
        body = world.createBody(bodyDef);
        body.setFixedRotation(false);
        body.setUserData(this);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void act(float deltaTime){
        if(body == null)
            return;
        this.setPosition(body.getPosition().x - (this.getWidth() / 2), body.getPosition().y - (this.getHeight() / 2));
        this.setRotation(body.getAngle() * MathUtils.radiansToDegrees);

        while(this.getRotation() > 360)
            this.rotate(-360);
        while(this.getRotation() < 0)
            this.rotate(360);


    }

    protected static Shape shapeFromTexture(Texture texture, float width)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, width * (texture.getHeight() / texture.getWidth()));
        return shape;
    }



}
