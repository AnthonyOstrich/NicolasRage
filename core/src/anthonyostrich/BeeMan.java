package anthonyostrich;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

/**
 * Created by anthony on 12/27/14.
 */
public class BeeMan extends CompoundActor{

    static Texture[] beeTextures = {new Texture(Gdx.files.internal("BeeMan/Torso.png")),
                                    new Texture(Gdx.files.internal("BeeMan/LeftArm.png")),
                                    new Texture(Gdx.files.internal("BeeMan/RightArm.png"))};

    Actor leftArm;


    public BeeMan(World world, float x, float y, float width)
    {
        super(beeTextures[0], world, x, y, width);
        leftArm = new Actor(beeTextures[1], world, x - (getWidth()*2f/3), y + getHeight()/8, 1);
        this.actors.add(leftArm);
        RevoluteJointDef leftArmJointDef = new RevoluteJointDef();
        leftArmJointDef.collideConnected = false;
        leftArmJointDef.initialize(this.body, leftArm.body, new Vector2(x - (getWidth() / 6), y + getHeight() / 8));
        world.createJoint(leftArmJointDef);

        Actor rightArm = new Actor(beeTextures[2], world, x + (getWidth()*2f/3), y + getHeight()/8, 1);
        this.actors.add(rightArm);
        RevoluteJointDef rightArmJointDef = new RevoluteJointDef();
        rightArmJointDef.collideConnected = false;
        System.out.println(this.body);
        rightArmJointDef.initialize(this.body, rightArm.body, new Vector2(x + (getWidth() / 6), y + getHeight() / 8));
        world.createJoint(rightArmJointDef);


    }



}

