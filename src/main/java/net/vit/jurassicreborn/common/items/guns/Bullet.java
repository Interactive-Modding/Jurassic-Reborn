package net.vit.jurassicreborn.common.items.guns;

import net.minecraft.world.item.Item;
import net.vit.jurassicreborn.common.items.TabHandler;


public class Bullet extends Item {
    public Bullet() {
        super(new Item.Properties()
                .tab(TabHandler.ITEMS)
        );
    }
}
