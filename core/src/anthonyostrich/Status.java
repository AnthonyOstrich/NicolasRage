package anthonyostrich;

/**
 * Created by anthony on 3/16/15.
 */
public class Status {
    long timeLeft;
    String name;
    Actor owner;
    boolean markedForRemoval = false;

    public Status(Actor owner, long time, String name)
    {
        this.owner = owner;
        this.timeLeft = time;
        this.name = name;
    }
    public void update(float delta)
    {
        timeLeft -= delta * 1000;
        if(timeLeft <= 0)
            markedForRemoval = true;
    }
    public void clear()
    {

    }
}
