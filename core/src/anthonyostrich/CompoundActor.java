package anthonyostrich;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

/**
 * Created by anthony on 12/27/14.
 */
public class CompoundActor extends Actor {
    protected ArrayList<Actor> actors = new ArrayList<Actor>();

    public CompoundActor(Texture texture, World world, float x, float y, float width) {
        super(texture, world, x, y, 1);

    }

    @Override
    public void draw(Batch batch)
    {
        super.draw(batch);
        for(Actor a : actors)
            a.draw(batch);
    }

    @Override
    public void act(float deltaTime) {
        super.act(deltaTime);
        for(Actor a : actors)
            a.act(deltaTime);
    }
}
