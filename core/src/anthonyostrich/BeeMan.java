package anthonyostrich;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

/**
 * Created by anthony on 12/27/14.
 */
public class BeeMan extends CompoundActor{

    static TextureRegion[] beeTextures = {Assets.getTexture("torso"),
                                    Assets.getTexture("leftArm"),
                                    Assets.getTexture("rightArm")};

    Actor leftArm;


    public BeeMan(World world, float x, float y, float width)
    {
        super(beeTextures[0], world, x, y, width);
        leftArm = new Actor(beeTextures[1], null, world, x - (getWidth()*2f/3), y + getHeight()/8, 1);
        this.actors.add(leftArm);
        RevoluteJointDef leftArmJointDef = new RevoluteJointDef();
        leftArmJointDef.collideConnected = false;
        leftArmJointDef.initialize(this.body, leftArm.body, new Vector2(x - (getWidth() / 6), y + getHeight() / 8));
        world.createJoint(leftArmJointDef);

        Actor rightArm = new Actor(beeTextures[2], null, world, x + (getWidth()*2f/3), y + getHeight()/8, 1);
        this.actors.add(rightArm);
        RevoluteJointDef rightArmJointDef = new RevoluteJointDef();
        rightArmJointDef.collideConnected = false;
        rightArmJointDef.initialize(this.body, rightArm.body, new Vector2(x + (getWidth() / 6), y + getHeight() / 8));
        world.createJoint(rightArmJointDef);


    }



}

