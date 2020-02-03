package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;

public class AncientRecordItem extends ItemRecord {
    public AncientRecordItem(String name, SoundEvent sound) {
        super(name, sound);
        this.setCreativeTab(TabHandler.ITEMS);
    }
}