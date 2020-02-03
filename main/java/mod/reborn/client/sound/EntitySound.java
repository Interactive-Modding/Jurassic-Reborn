package mod.reborn.client.sound;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import java.util.function.Predicate;

public class EntitySound<T extends Entity> extends MovingSound {

    protected final T entity;
    protected final Predicate<T> predicate;

    public EntitySound(T entity, SoundEvent soundEvent, SoundCategory soundCategory, Predicate<T> predicate) {
        super(soundEvent, soundCategory);
        this.entity = entity;
        this.predicate = predicate;
    }

    public EntitySound(T entity, SoundEvent soundEvent, SoundCategory soundCategory) {
        this(entity, soundEvent, soundCategory, t -> true);
    }

    @Override
    public void update() {
        if (this.entity.isDead || !predicate.test(this.entity)) {
            this.donePlaying = true;
        } else {
            this.xPosF = (float) this.entity.posX;
            this.yPosF = (float) this.entity.posY;
            this.zPosF = (float) this.entity.posZ;
        }
    }

    public void setFinished() {
        this.donePlaying = true;
    }


}
