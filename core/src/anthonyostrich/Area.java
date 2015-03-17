package anthonyostrich;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by anthony on 1/26/15.
 */
public class Area {
    private TiledMap map;



    public Area()
    {

    }

    public Area(String mapFile)
    {
        map = new TmxMapLoader().load(mapFile);
        for(MapLayer m : map.getLayers())
        {
            if(! m.getName().equalsIgnoreCase("background"))
                m.setVisible(false);
        }
    }

    public void addToWorld(World world, Random rand)
    {
        TiledMapTileLayer actorMap = (TiledMapTileLayer) (map.getLayers().get("Actors"));
        for (int x = 0; x < actorMap.getWidth(); x++) {
            for (int y = 0; y < actorMap.getHeight(); y++) {
                if (actorMap.getCell(x, y) != null && actorMap.getCell(x, y).getTile() != null) {
                    TiledMapTile tile = actorMap.getCell(x, y).getTile();
                    ActorFactory factory = ActorFactory.lookup((String) (actorMap.getCell(x, y).getTile().getProperties().get("actor")));
                    if (factory != null)
                        factory.get(Assets.getTexture((String) (tile.getProperties().get("texture"))), world, x + .5f, y + .5f, 1);
                }
            }
        }

        TiledMapTileLayer special = (TiledMapTileLayer) map.getLayers().get("Special");
        for(int y = 0; y < special.getWidth(); y ++)
        {
            for(int x = 0; x < special.getWidth(); x ++)
            {
                int right = x;
                while(special.getCell(right, y) != null && special.getCell(right, y).getTile() != null && "1".equals(special.getCell(right, y).getTile().getProperties().get("wall")))
                {
                    right ++;
                }
                if(right != x)
                {
                    Rectangle r = new Rectangle(x,y, right - x, 1);
                    BodyDef bodyDef = new BodyDef();
                    bodyDef.type = BodyDef.BodyType.StaticBody;
                    Vector2 center = new Vector2();
                    center = r.getCenter(center);
                    bodyDef.position.set(center);
                    Body body = world.createBody(bodyDef);
                    FixtureDef fixtureDef = new FixtureDef();
                    PolygonShape shape = new PolygonShape();
                    shape.setAsBox(r.width/2, r.height/2);
                    fixtureDef.shape = shape;
                    body.createFixture(fixtureDef);
                    shape.dispose();
                }
                x = right;

            }
        }

        TiledMapTileLayer backgroundLayer = (TiledMapTileLayer) (map.getLayers().get("Background"));
        for (int x = 0; x < backgroundLayer.getWidth(); x++) {
            for (int y = 0; y < backgroundLayer.getHeight(); y++) {
                if ("true".equals(backgroundLayer.getCell(x, y).getTile().getProperties().get("flip"))) {
                    if (rand.nextBoolean())
                        backgroundLayer.getCell(x, y).setFlipHorizontally(true);
                    if (rand.nextBoolean())
                        backgroundLayer.getCell(x, y).setFlipVertically(true);
                }
            }
        }
    }

    public void addToWorld(World world) {
        this.addToWorld(world, new Random(System.currentTimeMillis()));
    }

    public TiledMap getMap()
    {
        return map;
    }
}
