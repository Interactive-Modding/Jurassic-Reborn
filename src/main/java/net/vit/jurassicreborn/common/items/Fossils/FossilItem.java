package net.vit.jurassicreborn.common.items.Fossils;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.SkullDisplayBlockEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList.TyrannosaurusDinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Hybrid;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.util.LangUtil;
import net.vit.jurassicreborn.common.util.api.DinosaurItem;
import net.vit.jurassicreborn.common.util.api.GrindableItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.vit.jurassicreborn.common.blocks.entities.grinder.FossilGrinderBlockEntity.copyDNA;

public class FossilItem extends Item implements GrindableItem {
    public static Map<String, List<Dinosaur>> fossilDinosaurs = new HashMap<>();
    public static Map<String, List<Dinosaur>> freshFossilDinosaurs = new HashMap<>();
    private String type;
    private boolean fresh;
    private final Dinosaur dino;

    public FossilItem(Properties properties, String type, boolean fresh, Dinosaur dino ) {
        super(properties);
        this.type = type.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        this.fresh = fresh;
        this.dino = dino;

//        this.setHasSubtypes(true);
//
//        this.setCreativeTab(TabHandler.FOSSILS);
    }

    private static final java.util.Set<Dinosaur> SKULL_DISPLAY_DINOS = java.util.Set.of(
            DinosaurHandler.ACHILLOBATOR,
            DinosaurHandler.TYRANNOSAURUS,
            DinosaurHandler.VELOCIRAPTOR,
            DinosaurHandler.ECHO,
            DinosaurHandler.BLUE,
            DinosaurHandler.DELTA,
            DinosaurHandler.CHARLIE,
            DinosaurHandler.ALLOSAURUS,
            DinosaurHandler.BARYONYX,
            DinosaurHandler.ANKYLODOCUS,
            DinosaurHandler.ANKYLOSAURUS,
            DinosaurHandler.ARSINOITHERIUM,
            DinosaurHandler.APATOSAURUS,
            DinosaurHandler.BRACHIOSAURUS,
            DinosaurHandler.CAMARASAURUS,
            DinosaurHandler.CARCHARODONTOSAURUS,
            DinosaurHandler.CERATOSAURUS,
            DinosaurHandler.CHASMOSAURUS,
            DinosaurHandler.CARNOTAURUS,
            DinosaurHandler.CORYTHOSAURUS,
            DinosaurHandler.CEARADACTYLUS,
            DinosaurHandler.DREADNOUGHTUS,
            DinosaurHandler.DIPLODOCUS,
            DinosaurHandler.DEINOTHERIUM,
            DinosaurHandler.DUNKLEOSTEUS,
            DinosaurHandler.EDMONTOSAURUS,
            DinosaurHandler.ELASMOTHERIUM,
            DinosaurHandler.HERRERASAURUS,
            DinosaurHandler.GIGANOTOSAURUS,
            DinosaurHandler.INDOMINUS,
            DinosaurHandler.HYAENODON,
            DinosaurHandler.INDORAPTOR,
            DinosaurHandler.DIMETRODON,
            DinosaurHandler.LUDODACTYLUS,
            DinosaurHandler.MAJUNGASAURUS,
            DinosaurHandler.LAMBEOSAURUS,
            DinosaurHandler.MAWSONIA,
            DinosaurHandler.MAMENCHISAURUS,
            DinosaurHandler.MAMMOTH,
            DinosaurHandler.PACHYCEPHALOSAURUS,
            DinosaurHandler.DILOPHOSAURUS,
            DinosaurHandler.METRIACANTHOSAURUS,
            DinosaurHandler.MEGATHERIUM,
            DinosaurHandler.MOSASAURUS,
            DinosaurHandler.PROTOCERATOPS,
            DinosaurHandler.PARASAUROLOPHUS,
            DinosaurHandler.POSTOSUCHUS,
            DinosaurHandler.PTERANODON,
            DinosaurHandler.SUCHOMIMUS,
            DinosaurHandler.STEGOSAURUS,
            DinosaurHandler.SMILODON,
            DinosaurHandler.STYRACOSAURUS,
            DinosaurHandler.SPINOSAURUS,
            DinosaurHandler.SPINORAPTOR,
            DinosaurHandler.SINOCERATOPS,
            DinosaurHandler.RAPHUSREX,
            DinosaurHandler.RUGOPS,
            DinosaurHandler.QUETZAL,
            DinosaurHandler.THERIZINOSAURUS,
            DinosaurHandler.TYLOSAURUS,
            DinosaurHandler.TROPEOGNATHUS,
            DinosaurHandler.TRICERATOPS,
            DinosaurHandler.TITANIS,
            DinosaurHandler.TROODON,
            DinosaurHandler.LIVYATAN,
            DinosaurHandler.DEINOSUCHUS,
            DinosaurHandler.MAIASAURA,
            DinosaurHandler.PARACERATHERIUM,
            DinosaurHandler.PATAGOTITAN
    );

    //wtf is this??
    public static void init() {
        for (boolean fresh : new boolean[] { true, false }) {
            for (Dinosaur dinosaur : Dinosaur.DINOS) {
                if(!fresh && dinosaur instanceof Hybrid) {
                    continue;
                }
                Map<String, List<Dinosaur>> map = fresh ? freshFossilDinosaurs : fossilDinosaurs;
                String[] boneTypes = dinosaur.getBones();
                for (String boneType : boneTypes) {
                    List<Dinosaur> dinosaursWithType = map.get(boneType);

                    if (dinosaursWithType == null) {
                        dinosaursWithType = new ArrayList<>();
                    }
                    if(!dinosaur.getName().isEmpty() || dinosaur == Dinosaur.EMPTY) {
                        dinosaursWithType.add(dinosaur);
                    }
                    map.put(boneType, dinosaursWithType);
                }
            }
        }
    }

//    @Override
//    public String getItemStackDisplayName(ItemStack stack) {
//        Dinosaur dinosaur = this.getDinosaur(stack);
//
//        if (dinosaur != null) {
//            return LangUtils.translate(this.getUnlocalizedName() + ".name").replace("{dino}", LangUtils.getDinoName(dinosaur));
//        }
//
//        return super.getItemStackDisplayName(stack);
//    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack pStack) {
//        Dinosaur dino = this.getDinosaur(pStack);
//        if(dino != null){
//            return LangUtil.replaceInKey(dino::getName, "{dino}", "item.JurassicReborn." + (this.isFresh() ? "fresh_" : "" ) + this.type);
//        }
        return LangUtil.replaceWithDinoName(dino, "item.JurassicReborn." + this.type + (this.isFresh() ? "_fresh" : ""));
    }

    @Nullable
    public Dinosaur getDinosaur(@NotNull ItemStack stack) {
        if(stack.getItem() instanceof FossilItem item) {
            return item.dino;
        }else{
            return null;
        }
    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subtypes) {
//        List<Dinosaur> dinosaurs = new ArrayList<>(EntityHandler.getRegisteredDinosaurs());
//
//        Collections.sort(dinosaurs);
//
//        List<Dinosaur> dinosaursForType = this.getMap().get(this.type);
//        if(this.isInCreativeTab(tab))
//            for (Dinosaur dinosaur : dinosaurs) {
//                if (dinosaursForType.contains(dinosaur) && !(!this.fresh && dinosaur instanceof Hybrid)) {
//                    subtypes.add(new ItemStack(this, 1, EntityHandler.getDinosaurId(dinosaur)));
//                }
//            }
//    }

//    @Override
//    public void addInformation(ItemStack stack, Level worldIn, List<String> lore, ITooltipFlag flagIn) {
//        NBTTagCompound nbt = stack.getTagCompound();
//
//        if (nbt != null && nbt.hasKey("Genetics") && nbt.hasKey("DNAQuality")) {
//            int quality = nbt.getInteger("DNAQuality");
//
//            TextFormatting colour;
//
//            if (quality > 75) {
//                colour = TextFormatting.GREEN;
//            } else if (quality > 50) {
//                colour = TextFormatting.YELLOW;
//            } else if (quality > 25) {
//                colour = TextFormatting.GOLD;
//            } else {
//                colour = TextFormatting.RED;
//            }
//
//
//            lore.add(colour + LangUtils.translate(LangUtils.LORE.get("dna_quality")).replace("%1$s", LangUtils.getFormattedQuality(quality)));
//            lore.add(TextFormatting.BLUE + LangUtils.translate(LangUtils.LORE.get("genetic_code")).replace("%1$s", LangUtils.getFormattedGenetics(nbt.getString("Genetics"))));
//        }
//    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable net.minecraft.world.level.Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        CompoundTag nbt = stack.getTag();

        if (nbt != null && nbt.contains("Genetics") && nbt.contains("DNAQuality")) {
            int quality = nbt.getInt("DNAQuality");

            ChatFormatting colour;//BRIT(derogatory)

            if (quality > 75) {
                colour = ChatFormatting.GREEN;
            } else if (quality > 50) {
                colour = ChatFormatting.YELLOW;
            } else if (quality > 25) {
                colour = ChatFormatting.GOLD;
            } else {
                colour = ChatFormatting.RED;
            }

            pTooltipComponents.add(Component.literal(Component.translatable(LangUtil.LORE.formatted("dna_quality")).getString().replace("%1$s", LangUtil.getFormattedQuality(quality).getString())).withStyle(colour));
            pTooltipComponents.add(LangUtil.replaceInKey(() -> LangUtil.getFormattedGenetics(nbt.getString("Genetics")).getString(), "%1$s", Component.translatable(LangUtil.LORE.formatted("genetic_code")).getString()).withStyle(ChatFormatting.BLUE));
        }
        if (this.type.equals("skull") && SKULL_DISPLAY_DINOS.contains(this.dino)) {
            pTooltipComponents.add(Component.literal(Component.translatable("pose.name").getString() + ": " + LangUtil.getStandType(getHasStand(stack))).withStyle(ChatFormatting.GOLD));
            pTooltipComponents.add(Component.translatable("lore.change_variant.name").withStyle(ChatFormatting.WHITE));
        }
        super.appendHoverText(stack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public boolean isGrindable(ItemStack stack) {
        return true;
    }

    public boolean isFresh() {
        return this.fresh;
    }

    public String getBoneType(){
        return type;
    }

    @Override
    public ItemStack getGroundItem(ItemStack stack, Random random) {
        CompoundTag tag = stack.getTag();

        int outputType = random.nextInt(6);

        if (outputType == 5 || this.fresh) {
            ItemStack output = new ItemStack(ModItems.SOFT_TISSUE.get(this.getDinosaur(stack)).get());
            copyDNA(stack, output);
            return DinosaurItem.setDino(output, this.getDinosaur(stack));
        } else if (outputType < 3) {
            return Items.WHITE_DYE.getDefaultInstance();
//            return new ItemStack(Items.DYE, 1, 15);
        }

        return new ItemStack(Items.FLINT);
    }


//    @Override
//    public List<ItemStack> getJEIRecipeTypes() {
//        List<ItemStack> list = Lists.newArrayList();
//        this.getMap().get(this.type).forEach(dino -> list.add(new ItemStack(this, 1, EntityHandler.getDinosaurId(dino))));
//        return list;
//    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        float single = 100F/6F;
        CompoundTag tag = inputItem.getTag();
        ItemStack output = new ItemStack(ModItems.SOFT_TISSUE.get(this.getDinosaur(inputItem)).get(), 1, tag);
        if(this.fresh) {
            return Lists.newArrayList(Pair.of(100F, output));
        }
        return Lists.newArrayList(Pair.of(single, output), Pair.of(50f, Items.BONE_MEAL.getDefaultInstance()), Pair.of(single*2f, new ItemStack(Items.FLINT)));
    }

    public Map<String, List<Dinosaur>> getMap() {
        return this.fresh ? freshFossilDinosaurs : fossilDinosaurs;
    }
    private static final String TAG_STAND = "Type";

    /**
     * Return whether the given stack should display with a stand. Defaults to
     * {@code true} when the tag is missing.
     */
    public static boolean getHasStand(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null || !tag.contains(TAG_STAND) || tag.getBoolean(TAG_STAND);
    }

    /** Write the stand flag to the stack */
    public static void setHasStand(ItemStack stack, boolean hasStand) {
        stack.getOrCreateTag().putBoolean(TAG_STAND, hasStand);
    }

    /** Toggle the stand flag and return the new value */
    public static boolean changeStandType(ItemStack stack) {
        boolean newVal = !getHasStand(stack);
        setHasStand(stack, newVal);
        return newVal;
    }

    /* ------------------------------------------------------------------ */
    /*  Right-click in air: toggle stand                                  */
    /* ------------------------------------------------------------------ */

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown() && this.type.equals("skull")  && SKULL_DISPLAY_DINOS.contains(this.dino)) {
            boolean oldVal = getHasStand(stack);
            boolean newVal = changeStandType(stack);
            if (level.isClientSide && newVal != oldVal) {
                String msg = LangUtil.translate(LangUtil.STAND_CHANGE.get("type")).replace("{mode}", LangUtil.getStandType(newVal));
                player.displayClientMessage(Component.literal(msg).withStyle(ChatFormatting.YELLOW), true);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        return InteractionResultHolder.pass(stack);
    }

    /**
     * Check if the skull display block can be placed at the target position.
     * Copied from {@link net.minecraft.world.item.BlockItem} logic.
     */
    protected boolean canPlace(BlockPlaceContext ctx, BlockState state) {
        Player player = ctx.getPlayer();
        CollisionContext shape = player == null ? CollisionContext.empty() : CollisionContext.of(player);
        return state.canSurvive(ctx.getLevel(), ctx.getClickedPos()) && ctx.getLevel().isUnobstructed(state, ctx.getClickedPos(), shape);
    }

    /* ------------------------------------------------------------------ */
    /*  Right-click on block: place skull display                         */
    /* ------------------------------------------------------------------ */

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (!this.type.equals("skull") || !SKULL_DISPLAY_DINOS.contains(this.dino)) {
            return InteractionResult.PASS;
        }

        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        BlockPlaceContext placeCtx = new BlockPlaceContext(context);
        Block block = ModBlocks.SKULL_DISPLAY.get();
        BlockState state = block.getStateForPlacement(placeCtx);
        if (state == null) {
            return InteractionResult.FAIL;
        }

        if (!canPlace(placeCtx, state)) {
            return InteractionResult.FAIL;
        }

        level.setBlock(pos, state, 3);
        block.setPlacedBy(level, pos, state, context.getPlayer(), stack);

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof SkullDisplayBlockEntity tile) {
            tile.setModel(DinosaurHandler.getId(this.dino), !this.isFresh(), getHasStand(stack));
            Direction face = context.getClickedFace();
            if (face.getAxis() == Direction.Axis.Y && context.getPlayer() != null) {
                tile.setAngle(angleToPlayer(pos, context.getPlayer().getX(), context.getPlayer().getZ()));
            } else if (face.getAxis() == Direction.Axis.X) {
                tile.setAngle((short) face.toYRot());
            } else if (face.getAxis() == Direction.Axis.Z) {
                tile.setAngle((short) (180 + face.toYRot()));
            }
            tile.setChanged();
        }

        level.updateNeighborsAt(pos, block);
        if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * Calculate rotation angle so the skull faces the player when placed on the ground.
     */
    private static short angleToPlayer(BlockPos block, double playerX, double playerZ) {
        return (short) (90 - Math.toDegrees(Math.atan2((block.getZ() + 0.5) - playerZ, (block.getX() + 0.5) - playerX)));
    }

}
