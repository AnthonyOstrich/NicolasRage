package anthonyostrich;

/**
 * Created by anthony on 3/16/15.
 */
public class Property<T> {
        private String name;
        private T value;
    public Property(T value, String name)
    {
        this.name = name;
        this.value = value;
    }

    String getName()
    {
        return name;
    }

    public void set(T value)
    {

    }

    T get()
    {
        return value;
    }

}
