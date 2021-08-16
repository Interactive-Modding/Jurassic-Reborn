package mod.reborn.server.item;

import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.BiConsumer;

public class TrackerDart extends Dart {
    public TrackerDart() {
        super((entity, stack) -> init(entity, stack.getTagCompound().getString("uuid")), 0x111111);
    }

    public static void init(DinosaurEntity entity, String uuid)
    {
        if(!entity.trackersUUID.contains(uuid))
            entity.trackersUUID.add(uuid);

        for(String s : entity.trackersUUID)
            System.out.println("tracker : " + s);
    }
}
