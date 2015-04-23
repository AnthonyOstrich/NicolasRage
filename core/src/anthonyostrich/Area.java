package anthonyostrich;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Random;

/**
 * Created by anthony on 1/26/15.
 */
public class Area {
    private TiledMap map;
    private byte[][] zoneMap;


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
                    if (factory != null) {
                        System.out.println("Spawning " + factory + " at " + x + "," + y);
                        factory.get(Assets.getTexture((String) (tile.getProperties().get("texture"))), world, x + .5f, y + .5f, 1);
                    };
                }
            }
        }

        TiledMapTileLayer special = (TiledMapTileLayer) map.getLayers().get("Special");
        zoneMap = new byte[special.getWidth()][special.getHeight()];
        for(int y = 0; y < special.getWidth(); y ++)
        {
            for(int x = 0; x < special.getWidth(); x ++)
            {
                int right = x;
                while(special.getCell(right, y) != null && special.getCell(right, y).getTile() != null && "1".equals(special.getCell(right, y).getTile().getProperties().get("wall")))
                {
                    zoneMap[right][y] = -1;
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
                if (backgroundLayer.getCell(x, y) != null && "true".equals(backgroundLayer.getCell(x, y).getTile().getProperties().get("flip"))) {
                    if (rand.nextBoolean())
                        backgroundLayer.getCell(x, y).setFlipHorizontally(true);
                    if (rand.nextBoolean())
                        backgroundLayer.getCell(x, y).setFlipVertically(true);
                }
            }
        }

        byte zoneID = 0;
        for(int x = 0; x < zoneMap.length; x ++)
        {
            for(int y = 0; y < zoneMap[0].length; y ++)
            {
                if(zoneMap[x][y] == 0 && special.getCell(x,y) != null)
                {
                    zoneID ++;
                    System.out.println("Zone " + zoneID + " at " + x +"," + y);
                    fill(x, y, zoneMap, special, zoneID);
                }
            }
        }
        for(int y = 0; y < zoneMap[0].length; y ++)
        {
            for(int x = 0; x < zoneMap.length; x ++)
            {
                if(zoneMap[x][y] != 0)
                    System.out.print(zoneMap[x][y] + " ");
                else
                    System.out.print("  ");
                if(zoneMap[x][y] < 10)
                    System.out.print(" ");
                if(zoneMap[x][y] >= 0 )
                    System.out.print(" ");
            }
            System.out.println();
        }

        System.out.println("\n");

    }

    private void fill(int targetX, int targetY, byte[][] map, TiledMapTileLayer layer, byte value)
    {
        int id = layer.getCell(targetX,targetY).getTile().getId();
        map[targetX][targetY] = value;
        for (int x = (targetX > 0) ? targetX - 1 : 0; x <= targetX + 1 && x < map.length; x ++)
        {
            for (int y = (targetY > 0) ?  targetY - 1 : 0; y <= targetY + 1 && y < map[0].length; y ++)
            {
                if(map[x][y] == 0 && layer.getCell(x,y) != null && layer.getCell(x,y).getTile().getId() == id)
                    fill(x, y, map, layer, value);
            }
        }
    }

    private void flip(byte[][] map)
    {
        for(int y = 0; y < map[0].length / 2 ; y ++)
        {
            for(int x = 0; x < map.length; x ++)
            {
                byte temp = map[x][y];
                map[x][y] = map[x][map[0].length - 1 - y];
                map[x][map[0].length - 1 - y] = temp;
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

    public byte getZone(int x, int y)
    {
        return zoneMap[x][y];
    }
}
