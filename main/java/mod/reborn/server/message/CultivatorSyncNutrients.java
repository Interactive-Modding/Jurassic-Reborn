package mod.reborn.server.message;

import io.netty.buffer.ByteBuf;
import mod.reborn.server.block.entity.CultivatorBlockEntity;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CultivatorSyncNutrients extends AbstractMessage<CultivatorSyncNutrients> {

    private BlockPos position;

    private int waterLevel;
    private int lipids;
    private int proximates;
    private int minerals;
    private int vitamins;
    private int temperature;

    @SuppressWarnings("unused")
    public CultivatorSyncNutrients() {}

    public CultivatorSyncNutrients(CultivatorBlockEntity blockEntity) {
        this.waterLevel = blockEntity.getWaterLevel();
        this.lipids = blockEntity.getLipids();
        this.proximates = blockEntity.getProximates();
        this.minerals = blockEntity.getMinerals();
        this.vitamins = blockEntity.getVitamins();
        this.temperature = blockEntity.getTemperature(0);

        this.position = blockEntity.getPos();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.position.toLong());
        buf.writeInt(this.waterLevel);
        buf.writeInt(this.lipids);
        buf.writeInt(this.proximates);
        buf.writeInt(this.minerals);
        buf.writeInt(this.vitamins);
        buf.writeInt(this.temperature);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.position = BlockPos.fromLong(buf.readLong());
        this.waterLevel = buf.readInt();
        this.lipids = buf.readInt();
        this.proximates = buf.readInt();
        this.minerals = buf.readInt();
        this.vitamins = buf.readInt();
        this.temperature = buf.readInt();

    }

    @Override
    public void onClientReceived(Minecraft client, CultivatorSyncNutrients message, EntityPlayer player, MessageContext messageContext) {
        TileEntity tileEntity = player.world.getTileEntity(message.position);
        if(tileEntity instanceof CultivatorBlockEntity) {
            CultivatorBlockEntity cultivator = (CultivatorBlockEntity)tileEntity;
            cultivator.setField(2, this.waterLevel);
            cultivator.setField(3, this.proximates);
            cultivator.setField(4, this.minerals);
            cultivator.setField(5, this.vitamins);
            cultivator.setField(6, this.lipids);
            cultivator.setField(7, this.temperature);

        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, CultivatorSyncNutrients message, EntityPlayer player, MessageContext messageContext) {}
}
