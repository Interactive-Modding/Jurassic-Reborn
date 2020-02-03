package mod.reborn.server.item;

import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.BiConsumer;

public class Bullet extends Item {

    public Bullet() {
        this.setCreativeTab(TabHandler.ITEMS);
    }

}

