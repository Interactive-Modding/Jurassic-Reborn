//
//package net.vit.JurassicReborn.common.blocks.fossil.dinosaurs;
//
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.item.context.BlockPlaceContext;
//import net.minecraft.world.level.block.state.BlockState;
//import net.vit.JurassicReborn.common.blocks.ModBlocks;
//import net.vit.JurassicReborn.common.blocks.fossil.FossilBlock;
//import net.vit.JurassicReborn.common.entities.Dinosaurs.Dinosaur;
//import net.vit.JurassicReborn.common.entities.Dinosaurs.DinosaurHandler;
//
//public class CharlieFossilBlock extends Block implements FossilBlock {
//    public CharlieFossilBlock(Properties properties) {
//        super(properties);
//    }
//
//    @Override
//    public boolean mustBandage() {
//        return true;
//    }
//
//    @Override
//    public BlockState getEncasedFossil() {
//        return ModBlocks.ENCASED_CHARLIE_FOSSIL.get().defaultBlockState();
//    }
//
//    @Override
//    public Dinosaur getDinosaur() {
//        return DinosaurHandler.CHARLIE;
//    }
//}
