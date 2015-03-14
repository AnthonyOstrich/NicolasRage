package anthonyostrich;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by anthony on 3/14/15.
 */
public class GameCamera extends OrthographicCamera{
    private Actor following;

    public GameCamera(float viewportWidth, float viewportHeight, Actor following)
    {
        super(viewportWidth, viewportHeight);
    }


}
