package anthonyostrich;

import com.badlogic.gdx.utils.Array;

import java.lang.reflect.Type;

/**
 * Created by anthony on 3/16/15.
 */
public class Properties extends Array<Property> {
    public void set(Object value, String name)
    {
        for(Property p : this)
        {
            if(p.getName().equals(name))
            {
                p.set(value);
                return;
            }
        }
        this.add(new Property(value, name));
    }
    public Object get(String name)
    {
        for(Property p : this)
        {
            if(p.getName().equalsIgnoreCase(name))
                return p.get();
        }
        return null;
    }
}
