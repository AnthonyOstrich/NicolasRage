package anthonyostrich;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by anthony on 2/8/15.
 */
public class Assets {
    private static TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("texturemap.atlas"));
    public static TextureRegion getTexture(String textureName)
    {
        TextureRegion region = atlas.findRegion(textureName);
        if(region == null) {
            if(textureName != null)
                System.out.println("Texture \"" + textureName + "\" not found.");
            return atlas.findRegion("notFound");
        }
        return region;
    }

    public static TextureAtlas getAtlas()
    {
        return atlas;
    }
}

