package mod.reborn.server.message;

import io.netty.buffer.ByteBuf;
import mod.reborn.client.sound.EntitySound;
import mod.reborn.server.entity.vehicle.CarEntity;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CarEntityPlayRecord extends AbstractMessage<CarEntityPlayRecord> {

    private int entityId;
    private ItemStack record;

    public CarEntityPlayRecord(){}

    public CarEntityPlayRecord(CarEntity entity, ItemStack record){
        this.record = record;
        this.entityId = entity.getEntityId();
    }


    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        ByteBufUtils.writeItemStack(buf, this.record);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        record = ByteBufUtils.readItemStack(buf);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, CarEntityPlayRecord message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.entityId);
        if(entity instanceof CarEntity) {
            CarEntity carEntity = (CarEntity)entity;
            if(carEntity.sound != null) {
                carEntity.sound.setFinished();
            }
            carEntity.sound = new EntitySound<>(carEntity, ((ItemRecord) message.record.getItem()).getSound(), SoundCategory.RECORDS, car -> car.getItem().getItem() instanceof ItemRecord && ((ItemRecord)car.getItem().getItem()).getSound() == ((ItemRecord) message.record.getItem()).getSound());
            client.getSoundHandler().playSound(carEntity.sound);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, CarEntityPlayRecord message, EntityPlayer player, MessageContext messageContext) {}
}