package net.vit.jurassicreborn.common.items.genetics;

import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.TabHandler;
import net.vit.jurassicreborn.common.util.LangUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class DinosaurEggItem extends DNAContainerItem {
    private final Dinosaur dino;
    public DinosaurEggItem(Properties pProperties, Dinosaur dino) {
        super(pProperties);
        this.dino = dino;
    }

    @Override
    public Component getName(ItemStack pStack) {
        return Component.literal(Component.translatable("item.jurassicreborn.dino_egg").getString().replace("{dino}", LangUtil.getDinoName(this.dino).getString()));
    }

    public Dinosaur getDino() {
        return dino;
    }

    //legacy, use with care
    @Override
    public int getContainerId(ItemStack stack) {
        return Dinosaur.DINOS.indexOf(this.dino);
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subtypes) {
//        List<Dinosaur> dinosaurs = new LinkedList<>(EntityHandler.getDinosaurs().values());
//        Collections.sort(dinosaurs);
//        if(this.isInCreativeTab(tab)) {
//            for (Dinosaur dinosaur : dinosaurs) {
//                if (dinosaur.shouldRegister()) {
//                    if(!dinosaur.isMammal()) {
//                        if(!dinosaur.isMarineCreature()) {
//                            subtypes.add(new ItemStack(this, 1, EntityHandler.getDinosaurId(dinosaur)));
//                        }}}
//            }
//        }
//    }


    @Override
    public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
        if((pCategory == TabHandler.DNA || pCategory == CreativeModeTab.TAB_SEARCH)) {
            if(pItems.stream().anyMatch((stack) -> stack.is(this)))
                return;

            var eggItem = ModItems.dinoEggs.get(dino);
            if (eggItem != null) {
                ItemStack defaultDNAItem = eggItem.get().getDefaultInstance();

                defaultDNAItem.getOrCreateTag().putBoolean("isCreative", true);


                pItems.add(defaultDNAItem);
            }

        }else {

            super.fillItemCategory(pCategory, pItems);
        }
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        //        pos = pos.offset(side);
//
//        if (side == EnumFacing.EAST || side == EnumFacing.WEST)
//        {
//            hitX = 1.0F - hitX;
//        }
//        else if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH)
//        {
//            hitZ = 1.0F - hitZ;
//        }
//
//        if (player.canPlayerEdit(pos, side, stack) && !world.isRemote)
//        {
//            DinosaurEggEntity egg = new DinosaurEggEntity(world, getDinosaur(stack), getDNAQuality(player, stack), getGeneticCode(player, stack).toString());
//            egg.setPosition(pos.getX() + hitX, pos.getY(), pos.getZ() + hitZ);
//            egg.rotationYaw = player.rotationYaw;
//            world.spawnEntity(egg);
//
//            if (!player.capabilities.isCreativeMode)
//            {
//                stack.stackSize--;
//            }
//
//            return EnumActionResult.SUCCESS;
//        }
        return InteractionResult.PASS;
    }
}
