package net.vit.jurassicreborn.common.blocks.entities.skeletonassembly;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.vit.jurassicreborn.common.blocks.entities.MachineBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.blocks.inventory.ItemHandlerBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

import static net.vit.jurassicreborn.common.network.Network.id;

public class SkeletonAssemblerBlockEntity
        extends MachineBlockEntity
        implements MenuProvider, ItemHandlerBlockEntity, GeoBlockEntity {

    /* ---------------- inventory layout ---------------- */
    public static final int GRID_W = 5, GRID_H = 5;
    /** Number of fossil slots (0-24). */
    private static final int GRID_SLOTS = GRID_W * GRID_H;   // 25
    /** Index of the output slot inside the item handler. */
    public static final int RESULT_SLOT = GRID_SLOTS;        // 25
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("animation.model.idle");
    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private final SkeletonAssemblerItemHandler items =
            new SkeletonAssemblerItemHandler();

    @Override public IItemHandlerModifiable getItemHandler() { return items; }

    /* ---------------- recipe progress data ------------ */
    private int   progress     = 0;
    private int   maxProgress  = 200;   // 10 s @ 20 tps
    private int[] cachedBounds = new int[4];  // minX,minY,maxX,maxY

    /* ---------------- block-entity init --------------- */
    public SkeletonAssemblerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SKELETON_ASSEMBLY_ENTITY.get(), pos, state);
    }

    /* ================================================================== */
    /*  DATA SYNC                                                         */
    /* ================================================================== */

    public final ContainerData data = new ContainerData() {
        @Override public int get(int i) { return i == 0 ? progress : maxProgress; }
        @Override public void set(int i,int v){ if(i==0)progress=v; else maxProgress=v;}
        @Override public int getCount() { return 2; }
    };

    /* store recipe bounds + progress so client can animate bars later */
    @Override public Tag getMachineData() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("Prog", progress);
        nbt.putIntArray("Bounds", cachedBounds);
        return nbt;
    }
    @Override public void readMachineData(Tag tag){
        CompoundTag n = (CompoundTag)tag;
        progress = n.getInt("Prog");
        int[] b  = n.getIntArray("Bounds");
        if(b.length==4) cachedBounds=b;
    }

    /* ================================================================== */
    /*  TICK                                                              */
    /* ================================================================== */

    public static void tick(Level lvl, BlockPos pos, BlockState st,
                            SkeletonAssemblerBlockEntity be) {
        if (lvl.isClientSide) return;

        if (be.hasValidRecipe()) {
            be.progress++;
            if (be.progress >= be.maxProgress) {
                be.craft();
                be.progress = 0;
            }
        } else be.progress = 0;

        be.setChanged();
    }

    /* ================================================================== */
    /*  RECIPE CHECK & CRAFT                                              */
    /* ================================================================== */

    private boolean hasValidRecipe() {
        ItemStack resultItem = items.getStackInSlot(RESULT_SLOT);
        if (!resultItem.isEmpty()) {
            // if the grid no longer matches the recipe, remove the result
            if (!SkeletonRecipeHelper.tryMatch(items).success())
                items.setStackInSlot(RESULT_SLOT, ItemStack.EMPTY);
            return false;                       // wait for player to take result
        }
        return false;
    }

    private void craft() {
        SkeletonRecipeHelper.Result result =
                SkeletonRecipeHelper.tryMatch(items);
        if (!result.success()) return;
        if (!items.getStackInSlot(RESULT_SLOT).isEmpty()) return;
        // clear fossil grid for recipe fossils ONLY
        int[] b = result.bounds();                // minX,minY,maxX,maxY
        for (int y = b[1]; y <= b[3]; y++)
            for (int x = b[0]; x <= b[2]; x++)
                items.setStackInSlot(x + y * GRID_W, ItemStack.EMPTY);

    }

    /* ================================================================== */
    /*  MENU / NAME                                                       */
    /* ================================================================== */

    @Override protected @NotNull Component getDefaultName() {
        return Component.translatable("container.jurassicreborn.skeleton_assembly");
    }

    /**
     * This method should return weather or not the machine block entity should process the inputs given in the {@code ItemStack... inputs} param.
     * The way I've thought about this is that this method should be given an ordered list of item stacks with all inputs in an order
     * the coder devises. <br><br>
     * For example input #1 is a DNA syringe, input #2 is an egg, this method should return {@code true} if the machine is
     * an embryo calcification machine, the dino referenced from the DNA syringe lays an egg, AND input #2 is an egg.
     *
     * @param inputs A given list of inputs from a machine. This should be for an individual input/alt. input set in a multi-processed machine.
     * @return true IF AND ONLY IF the machine is supposed to produce an output for the given list of inputs.
     */
    @Override
    public boolean canProcess(ItemStack... inputs) {
        return false;
    }

    /**
     * This method should return the result of processing the given inputs, in itemstack form. However, handling placing these
     * items should be handled by the tick function. I also suggest handing off decreasing the item counts in the container
     * to the tick function, but that's less important: Do What Works.
     *
     * @param inputs A given list of inputs from a machine. This should be for an individual input/alt. input set in a multi-processed machine.
     * @return Unordered list of output items to be handled by the tick function.
     */
    @Override
    public @NotNull List<ItemStack> processItem(ItemStack... inputs) {
        return List.of();
    }


    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new SkeletonAssemblerMenu(id, inv, items, data, this.getBlockPos());
    }

    @Override
    public boolean hasCustomName() {
        return super.hasCustomName();
    }

    @Override
    public Component getDisplayName() {
        return super.getDisplayName();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0,
                state -> state.setAndContinue(IDLE_ANIMATION)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animationCache;
    }
}
