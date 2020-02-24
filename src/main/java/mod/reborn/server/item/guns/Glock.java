package mod.reborn.server.item.guns;

import com.google.common.collect.Lists;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.item.BulletEntity;
import mod.reborn.server.item.Bullet;
import mod.reborn.server.tab.TabHandler;
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

public class Glock extends Gun {

    public Glock() {
        super(TabHandler.ITEMS, 1, SoundHandler.FIRE, SoundHandler.EMPTY, SoundHandler.RELOAD, 8, 40, 5, 2, 0.5F, 0.0F, 5);
    }

}