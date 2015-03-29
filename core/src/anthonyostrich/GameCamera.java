package anthonyostrich;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by anthony on 3/14/15.
 */
public class GameCamera extends OrthographicCamera{
    private Actor following;

    public GameCamera(float viewportWidth, float viewportHeight, Actor following)
    {
        super(viewportWidth, viewportHeight);
        this.following = following;
        if(following instanceof Player)
            ((Player) following).setCamera(this);
    }

    public void act(float deltaTime)
    {
        this.translate(following.getX() - position.x, following.getY() - position.y);
    }


}
