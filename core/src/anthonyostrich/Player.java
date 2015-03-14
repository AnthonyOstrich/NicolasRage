package anthonyostrich;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
/**
 * Created by anthony on 1/26/15.
 */
public class Player extends Actor {
    Camera camera;
    Controller controller;
    boolean flipped = false;
    public Player(World world, float x, float y, float width, Camera worldCamera) {
        super(Assets.getTexture("cage"), new CircleShape(), world, x, y, width);
        camera = worldCamera;
        this.body.setLinearDamping(2);
        MassData massData = body.getMassData();
        massData.mass = 1;
        this.body.setMassData(massData);
        addController();
    }


    public void addController()
    {
        for(Controller c : Controllers.getControllers())
        {
            if(c != null)
            {
                controller = c;

            }
        }
    }
    
    @Override
    public void act(float delta)
    {
        if(Gdx.input.isTouched())
        {
            try {
                Vector3 pointerLocation = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(pointerLocation);
                Vector2 difference = body.getWorldCenter().sub(pointerLocation.x, pointerLocation.y);
                body.setTransform(body.getPosition(), MathUtils.degreesToRadians * (difference.angle() - 180));
                Vector2 facing = new Vector2(10, 0);
                if (difference.len2() > .001f) {
                    if (difference.len2() < 1)
                        facing.scl(difference.len());
                    facing.rotateRad(body.getAngle());
                    body.applyForce(facing, body.getWorldCenter(), true);
                }
            }
            catch (NullPointerException e)
            {
                System.out.println("No Camera found");
            }
        }
        else if(controller != null && (Math.abs(controller.getAxis(0)) >= .1f || Math.abs(controller.getAxis(1)) >= .1f))
        {
            Vector2 move = new Vector2(controller.getAxis(0),-controller.getAxis(1));
            if(move.len2() > 1)
                move.nor();
            body.setTransform(body.getPosition(), move.angleRad());
            body.applyForce(move.scl(10), body.getWorldCenter(), true);
        }    
        else{
            if (Gdx.input.isKeyPressed(Input.Keys.A))
                body.setTransform(body.getPosition(), body.getAngle() + ((float) (Math.PI) * delta));
            if (Gdx.input.isKeyPressed(Input.Keys.D))
            {
                body.setTransform(body.getPosition(), body.getAngle() + ((float) (Math.PI) * -delta));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                Vector2 facing = new Vector2(10, 0);
                facing.rotateRad(body.getAngle());
                body.applyForce(facing, body.getWorldCenter(), true);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                Vector2 facing = new Vector2(-2, 0);
                facing.rotateRad(body.getAngle());
                body.applyForce(facing, body.getWorldCenter(), true);
            }
        }
            if((this.getRotation() > 90 && this.getRotation() < 270) != flipped) {
            this.flip(false, true);
            flipped = !flipped;
        }
        super.act(delta);
    }
}
