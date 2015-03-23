package anthonyostrich;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by anthony on 3/16/15.
 */
public class Fireball extends Actor{
    private ParticleEffect effect;
    public Fireball(World world, float x, float y, float width) {
        super(null, new CircleShape(), world, x, y, width);
        this.lifeSpan = 3000;
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("fire.p"), Assets.getAtlas());
        effect.scaleEffect(width);
        effect.setDuration((int) (lifeSpan - 1000));
        effect.start();
    }
    @Override
    public void draw(Batch batch) {
        effect.setPosition(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2);
        effect.draw(batch, Gdx.graphics.getDeltaTime());
    }

    @Override
    public BoundingBox getBoundingBox()
    {
        return effect.getBoundingBox();
    }

    @Override
    public byte getDrawPriority()
    {
        return 1;
    }

    @Override
    protected void addToWold(World world, float x, float y)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);
        body.setLinearDamping(0);
        bodyDef.angularDamping = 5f;
        body.setFixedRotation(false);
        body.setUserData(this);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;new BoundingBox(new Vector3(this.getX(), this.getY(), 0), new Vector3(this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0));
        fixtureDef.restitution = 1;
        fixtureDef.density = 1;
        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
    }

    @Override
    public void beginContact(Actor otherActor)
    {
        if(otherActor != this.getOwner())
        {
            ParticleEffect burningEffect = new ParticleEffect();
            burningEffect.load(Gdx.files.internal("fire.p"), Assets.getAtlas());
            Status fire = new ParticleStatus(otherActor,4000,"fire", burningEffect);
            otherActor.addStatus(fire);
            this.setLifeSpan(1000);
        }
    }

    @Override
    public void setLifeSpan(long life)
    {
        super.setLifeSpan(life);
        if(life <= -1)
            effect.setDuration(Integer.MAX_VALUE);
        else
            effect.setDuration((int)(life - 1000));
    }

}
