package anthonyostrich;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by anthony on 2/8/15.
 */
public class Assets {
    private static Asset[] assets = {new Asset("cage"), new Asset("box"), new Asset("beeMan/leftArm"),
                                     new Asset("beeMan/rightArm"), new Asset("beeMan/torso"),
                                     new Asset("beeMan/leftLeg"), new Asset("beeMan/RightLeg"),
                                     new Asset("bee")};
    private static Asset notFound = new Asset("notFound");

    public static Texture getTexture(String textureName)
    {
        for(Asset a : assets)
        {
            if(a.name.equalsIgnoreCase(textureName))
                return a.getTexture();
        }
        System.out.println("Texture \"" + textureName + ".png\" not found");
        return notFound.getTexture();
    }

    private static class Asset{
        public String name;
        public Texture texture;
        public FileHandle fileHandle;
        public Asset(String fileName)
        {
            name = fileName;
            fileHandle = Gdx.files.internal(name + ".png");
        }
        public Texture getTexture()
        {
            if(texture == null)
            {
                System.out.println("Loading texture " + fileHandle);
                texture = new Texture(fileHandle);
            }
            return texture;
        }


    }
}

