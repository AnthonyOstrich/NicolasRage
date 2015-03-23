package anthonyostrich;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

/**
 * Created by anthony on 12/27/14.
 */
public class Actor extends Sprite implements Comparable{
    Body body;
    protected Shape shape;
    public ActorSteerer steerer;
    private World world;
    private long timeAlive;
    private Actor owner;
    Array<Status> statusEffects = new Array<Status>();
    private boolean markedForDeletion = false;
    protected long lifeSpan = -1;
    Area area;

    public Actor(TextureRegion texture, Shape objectShape, World world, float x, float y, float width)
    {
        super((texture == null) ? Assets.getTexture("notFound") : texture);
        this.setSize(width, width * this.getHeight() / this.getWidth());
        this.setOriginCenter();
        if(objectShape == null)
        {
            objectShape = new PolygonShape();
            ((PolygonShape)objectShape).setAsBox(this.getWidth()/ 2, this.getHeight() / 2);
        }
        else
            objectShape.setRadius(this.getWidth() / 2);
        shape = objectShape;
        this.addToWold(world, x, y);
        this.world = world;
        steerer = new ActorSteerer(this);
    }

    public void setOwner(Actor owner)
    {
        this.owner = owner;
    }

    public Actor getOwner()
    {
        return this.owner;
    }

    protected static PolygonShape makeBox(float width, float height)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width,height);
        return shape;
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
        if(markedForDeletion)
        {
            this.kill();
            return;
        }

        steerer.update(deltaTime);
        this.setPosition(body.getPosition().x - (this.getWidth() / 2), body.getPosition().y - (this.getHeight() / 2));
        this.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        while(this.getRotation() > 360)
            this.rotate(-360);
        while(this.getRotation() < 0)
            this.rotate(360);

        for(Status s : this.statusEffects) {
            s.update(deltaTime);
        }
        if(getLifeSpan() > -1)
        {
            timeAlive += deltaTime * 1000;
            if(timeAlive > getLifeSpan())
                this.kill();
        }

    }

    protected static Shape shapeFromTexture(Texture texture, float width)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, width * (texture.getHeight() / texture.getWidth()));
        return shape;
    }

    public void SetArea(Area area)
    {
        this.area = area;
    }

    public byte getDrawPriority()
    {
        return 0;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Actor)
            return this.getDrawPriority() - ((Actor) o).getDrawPriority() ;
        else return 0;
    }

    @Override
    public void draw(Batch batch)
    {
        super.draw(batch);
        for(Status s : statusEffects)
            s.draw(batch);
    }

    public void shoot(ActorFactory projectileFactory, float speed)
    {
        Vector2 position = new Vector2(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2);
        position.add(new Vector2(this.getWidth() / 2, 0).rotate(this.getRotation()));
        Actor projectile = projectileFactory.get(null, world, position.x, position.y, 1);
        projectile.setOwner(this);
        projectile.body.setLinearVelocity(new Vector2(speed, 0).rotate(this.getRotation()).add(body.getLinearVelocity()));
    }

    protected long getLifeSpan()
    {
        return lifeSpan;
    }

    protected void setLifeSpan(long life)
    {
        this.lifeSpan = life;
    }

    public void kill() {
        world.destroyBody(body);
        body.setUserData(null);
        body = null;
    }


    public void beginContact(Actor otherActor)
    {
    }

    public void endContact(Actor otherActor)
    {
    }

    public void addStatus(Status status)
    {
        for(Status s : statusEffects)
        {
            s.setTimeLeft(status.timeLeft);
            return;
        }
        statusEffects.add(status);
    }

    public void markForDeletion()
    {
        markedForDeletion = true;
    }

    public BoundingBox getBoundingBox()
    {
        return new BoundingBox(new Vector3(this.getX(), this.getY(), 0), new Vector3(this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0));
    }

    public Area getArea()
    {
        return area;
    }

    public byte getZone()
    {
        if(this.area == null)
            return -1;
        else
            return area.getZone(((int) (this.getX() + this.getWidth()/2)), ((int) (this.getY() + this.getHeight()/2)));
    }

}
