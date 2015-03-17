package anthonyostrich;

import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.*;
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
public class Bee extends Actor {

    private SteeringBehavior<Vector2> wander;

    Actor target;
    World worldIn;
    Array<Body> bodies;
    Random rand = new Random(System.currentTimeMillis());
    public ActorSteerer toFollow;
    
    public Bee( World world, float x, float y) {
        super(Assets.getTexture("bee"), new CircleShape(), world, x, y, .5f);
        if(this instanceof Actor)
            System.out.println("Bee is Actor");
        worldIn = world;
        bodies = new Array<Body>();
        worldIn.getBodies(bodies);
        for(Body b : bodies)
        {
            if (b.getUserData() instanceof Player && b.getUserData() != this )
            {
                toFollow = ((Actor)b.getUserData()).steerer;
            }
        }
        if(toFollow != null)
        {
            wander = new Wander(steerer)
                    .setFaceEnabled(true)
                    .setEnabled(true)
                    .setAlignTolerance(.001f)
                    .setDecelerationRadius(5)
                    .setTimeToTarget(.1f)
                    .setWanderOrientation(10)
                    .setWanderRadius(30)
                    .setWanderOffset(4)
                    .setWanderRate(MathUtils.PI / 10);
            steerer.setSteeringBehavior(wander);
        }
        steerer.setMaxLinearAcceleration(5);
        steerer.setMaxLinearSpeed(10);
        steerer.setMaxAngularAcceleration(1f);
        steerer.setMaxAngularSpeed(3f);
    }

}
