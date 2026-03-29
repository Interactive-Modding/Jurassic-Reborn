package net.vit.jurassicreborn.client.sounds;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;

import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public class CarLoopSound extends AbstractTickableSoundInstance {
    private final VehicleEntity car;
    private final Predicate<VehicleEntity> keepPlaying;
    private final boolean station;

    public CarLoopSound(VehicleEntity car,
                        SoundEvent event,
                        SoundSource category,
                        Predicate<VehicleEntity> keepPlaying,
                        boolean station) {
        super(event, category, SoundInstance.createUnseededRandom());
        this.car = car;
        this.keepPlaying = keepPlaying;
        this.station = station;
        this.looping = true;
        this.delay = 0;
        this.x = (float) car.getX();
        this.y = (float) car.getY();
        this.z = (float) car.getZ();
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

        if (station) {
            this.volume = Mth.clamp(car.getStationSoundVolume(), 0.0F, 1.0F);
            this.pitch = 1.0F;
        } else {
            this.volume = Mth.clamp(car.getEngineSoundVolume(), 0.0F, 1.0F);
            this.pitch = 1.0F;
        }
    }
}