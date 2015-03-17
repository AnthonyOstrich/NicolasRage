package anthonyostrich;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

/**
 * Created by anthony on 3/17/15.
 */
public class ParticleStatus extends Status {
    private ParticleEffect particleEffect;

    public ParticleStatus(Actor owner, long time, String name, ParticleEffect particleEffect) {
        super(owner, time, name);
        this.particleEffect = particleEffect;
        particleEffect.setDuration((int) time);
        particleEffect.start();
    }

    @Override
    public void draw(Batch batch) {
        particleEffect.setPosition(owner.getX() + owner.getWidth() / 2, owner.getY() + owner.getHeight() / 2);
        particleEffect.draw(batch);

    }

    @Override
    public void update(float deltaTime)
    {
        particleEffect.update(deltaTime);
    }
}
