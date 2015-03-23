package anthonyostrich;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by anthony on 3/16/15.
 */
public class Status {
    long timeLeft;
    String name;
    Actor owner;

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
            this.clear();
    }

    public void draw(Batch batch)
    {

    }

    @Override
    public boolean equals(Object otherStatus)
    {
        if(! (otherStatus instanceof Status))
            return false;
        return ((Status)otherStatus).name.equals(this.name);
    }

    public void clear()
    {
        owner.statusEffects.removeValue(this, false);
    }

    public void setTimeLeft(long time)
    {
        this.timeLeft = time;
    }

}
