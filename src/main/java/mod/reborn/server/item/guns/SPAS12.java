package mod.reborn.server.item.guns;

import com.google.common.collect.Lists;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.item.BulletEntity;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class SPAS12 extends Gun {
    public SPAS12() {
        super(TabHandler.ITEMS, 6, SoundHandler.FIRE, SoundHandler.EMPTY, SoundHandler.RELOAD, 6, 60, 20, 0.9F, 15F, 0F, 5);
    }
}