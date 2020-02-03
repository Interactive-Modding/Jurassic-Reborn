package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.world.World;

public class ApatosaurusEntity extends DinosaurEntity
{
    private int stepCount = 0;

    public ApatosaurusEntity(World world)
    {
        super(world);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.moveForward > 0 && this.stepCount <= 0)
        {
            this.playSound(SoundHandler.STOMP, (float) interpolate(0.1F, 1.0F), this.getSoundPitch());
            stepCount = 65;
        }

        this.stepCount -= this.moveForward * 9.5;
    }
}
