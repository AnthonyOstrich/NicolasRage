package anthonyostrich;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by anthony on 3/7/15.
 */
public class ActorSteerer implements Steerable<Vector2>{
    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
    private SteeringBehavior<Vector2> steeringBehavior = null;
    Actor controlling = null;
    boolean tagged = false;
    float maxLinearSpeed = 10;
    float maxLinearAcceleration = 10;
    float maxAngularAcceleration = 1;
    float maxAngularSpeed = 1f;
    
    public ActorSteerer(Actor toSteer)
    {
        controlling = toSteer;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> behavior)
    {
        steeringBehavior = behavior;
    }

    public void update(float delta)
    {
        if(steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringOutput);
            steeringOutput.linear = steeringOutput.linear.scl(controlling.body.getMass());
            steeringOutput.angular = steeringOutput.angular * controlling.body.getMass();
            if(Float.isFinite(steeringOutput.linear.x) && Float.isFinite(steeringOutput.linear.y))
                controlling.body.applyForce(steeringOutput.linear, controlling.body.getWorldCenter(), true);
            if(Float.isFinite(steeringOutput.angular))
                controlling.body.applyTorque(steeringOutput.angular, true);
        }
    }
    
    @Override
    public Vector2 getPosition()
    {
        return new Vector2(controlling.getX(), controlling.getY());
    }
    
    @Override
    public float getOrientation()
    {
        float angle = controlling.body.getAngle();
        return angle;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return controlling.body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return controlling.body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return 1;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public Vector2 newVector() {
        return new Vector2(0,0);
    }


    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return vector.angleRad();
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.sub(outVector).add(new Vector2(1,0).rotateRad(angle));
        return outVector;
    }
    
}
