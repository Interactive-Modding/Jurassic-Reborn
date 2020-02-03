package mod.reborn.client.sound;

import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.vehicle.CarEntity;

@SideOnly(Side.CLIENT)
public class CarSound extends EntitySound<CarEntity> {
    public CarSound(CarEntity entity) {
        super(entity, SoundHandler.CAR_MOVE, SoundCategory.BLOCKS);
    }

    @Override
    public boolean canRepeat() {
        return true;
    }

    @Override
    public float getVolume() {
        return (Math.abs(this.entity.wheelRotateAmount) + 0.001F) / (entity.sound == null || entity.sound.isDonePlaying() ? 2f : 4f);
    }

    @Override
    public float getPitch() {
        return Math.min(1.0F, this.getVolume()) * 0.5F + 0.7F;
    }
}
