package net.vit.jurassicreborn.client.screens;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.HashMap;

public class ModScreens {



    private static HashMap<BlockEntityType<?>, ModScreenSupplier<?, ?>> screenHashMap = new HashMap<>();


    public static <T extends BlockEntity, E extends  AbstractContainerMenu, S extends AbstractContainerScreen<E>> void register(BlockEntityType<T> expectedType, ModScreenSupplier<E, S> supplier){
        screenHashMap.put(expectedType, supplier);
    }

    public static <T extends BlockEntity, E extends AbstractContainerMenu, S extends AbstractContainerScreen<E>> ModScreenSupplier<E, S> get(BlockEntityType<T> type){
        return (ModScreenSupplier<E, S>) screenHashMap.get(type);
    }

    public static boolean has(BlockEntityType<?> type){
        return screenHashMap.containsKey(type);
    }



    public interface ModScreenSupplier<M extends AbstractContainerMenu, T extends AbstractContainerScreen<M>> {

        T create(M menu, Inventory inv, Component title, BlockEntity container);

    }
}
