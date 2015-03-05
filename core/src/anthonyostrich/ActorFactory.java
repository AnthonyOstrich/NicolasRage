package anthonyostrich;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by anthony on 2/20/15.
 */
public class ActorFactory {

    public static ActorFactory[] factories = {new ActorFactory(), new BeeFactory(), new BeeManFactory()};

    public static ActorFactory lookup(String name) {
        for(ActorFactory a : factories)
        {
            if(a.toString().equals(name))
                return a;
        }
        System.out.println("Unable to find actor type " + name);
        return null;
    }

    public ActorFactory(){};

    public Actor get(Texture texture, World world, float x, float y, float width){
        return new Actor(texture, world, x, y, width);
    }

    @Override
    public String toString()
    {
        return "generic";
    }

    public static class BeeFactory extends ActorFactory{
        @Override
        public Actor get(Texture texture, World world, float x, float y, float width)
        {
            return new Bee(world, x, y);
        }

        @Override
        public String toString(){
            return "bee";
        }
    }

    public static class BeeManFactory extends ActorFactory {
        @Override
        public Actor get(Texture texture, World world, float x, float y, float width) {
            return new BeeMan(world, x, y, 1);
        }

        @Override
        public String toString(){
            return "beeman";
        }
    }

}


