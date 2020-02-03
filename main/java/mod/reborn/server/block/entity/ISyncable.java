package mod.reborn.server.block.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.NonNullList;

public interface ISyncable
{

    NonNullList getSyncFields(NonNullList fields);
    
    void packetDataHandler(ByteBuf fields);
    
}
