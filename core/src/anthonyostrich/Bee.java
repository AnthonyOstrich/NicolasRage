package anthonyostrich;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by anthony on 2/8/15.
 */
public class Bee extends Actor{

    Actor target;
    World worldIn;
    Array<Body> bodies;
    static final float maxRotation = (float) Math.PI;
    Random rand = new Random(System.currentTimeMillis());
    public boolean turning = rand.nextBoolean();
    public float direction = rand.nextInt(2) - 1;
    float timeSinceTurn = 0;
    float timeSinceTarget = 0;
    boolean targeting = false;

    public Bee( World world, float x, float y) {
        super(Assets.getTexture("bee"), new CircleShape(), world, x, y, .5f);
        worldIn = world;
        bodies = new Array<Body>();
        worldIn.getBodies(bodies);
    }

    @Override
    public void act(float delta)
    {
        if(target == null)
        {
            worldIn.getBodies(bodies);
            for (Body b :bodies) {
                if (b.getUserData() instanceof Player) {
                    target = (Actor) b.getUserData();
                }
            }
        }
        else
        {
            /*
            timeSinceTarget += delta;
            if(timeSinceTarget > 5 || (timeSinceTarget > 3 && !targeting))
            {
                timeSinceTarget = 0;
                if(rand.nextInt(4) == 1)
                    targeting = !targeting;
            }
            if(targeting)
            {
            */
                float targetAngle = target.body.getPosition().cpy().sub(this.body.getPosition().cpy()).angle() * MathUtils.degreesToRadians;
                this.body.setTransform(this.body.getPosition(), targetAngle);
            /*
            }
            else
            {
                timeSinceTurn += delta;
                if(timeSinceTurn >= 2)
                {
                    timeSinceTurn = 0;
                    if(rand.nextBoolean())
                        turning = !turning;
                    if(rand.nextBoolean())
                        direction = - direction;
                }

                float toAngle =  this.body.getAngle() + direction * delta;
                while(toAngle < 0)
                    toAngle += Math.PI * 2;
                while(toAngle > Math.PI * 2)
                    toAngle -= Math.PI * 2;
                this.body.setTransform(this.body.getPosition(), toAngle);

            }
            */
            Vector2 facing = new Vector2(1, 0);
            facing.rotateRad(body.getAngle());
            body.applyForce(facing, body.getWorldCenter(), true);

        }
        super.act(delta);
    }
}
