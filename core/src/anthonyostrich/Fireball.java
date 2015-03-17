package anthonyostrich;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by anthony on 3/16/15.
 */
public class Fireball extends Actor{
    private ParticleEffect effect;
    private long lifespan  = 3000;
    public Fireball(World world, float x, float y, float width) {
        super(null, new CircleShape(), world, x, y, width);
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("fire.p"), Gdx.files.internal(""));
        effect.scaleEffect(width * 10);
        effect.setDuration((int) (lifespan - 1000));
        effect.start();
    }
    @Override
    public void draw(Batch batch) {
        effect.setPosition(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2);
        effect.draw(batch, Gdx.graphics.getDeltaTime());
    }

    @Override
    public long lifeSpan()
    {
        return lifespan;
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
        fixtureDef.shape = shape;
        fixtureDef.restitution = 1;
        fixtureDef.density = 1;
        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
    }

}
