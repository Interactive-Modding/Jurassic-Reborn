package net.vit.jurassicreborn.client.sounds;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;

import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public class CarLoopSound extends AbstractTickableSoundInstance {
    private final VehicleEntity car;
    private final Predicate<VehicleEntity> keepPlaying;

    public CarLoopSound(VehicleEntity car,
                        SoundEvent event,
                        SoundSource category,
                        Predicate<VehicleEntity> keepPlaying) {
        super(event, category, SoundInstance.createUnseededRandom());
        this.car = car;
        this.keepPlaying = keepPlaying;
        this.looping = true;
    }

    @Override
    public void tick() {
        if (car.isRemoved() || !keepPlaying.test(car)) {
            this.stop();
            return;
        }
        this.x = (float) car.getX();
        this.y = (float) car.getY();
        this.z = (float) car.getZ();
        // VehicleEntity#getSoundVolume() already scales with speed
        this.volume = Mth.clamp(car.getSoundVolume(), 0.0F, 1.0F);
    }
}
