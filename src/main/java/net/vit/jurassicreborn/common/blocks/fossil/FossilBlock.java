package net.vit.jurassicreborn.common.blocks.fossil;

import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface FossilBlock {

    boolean mustBandage();


    default BlockState getEncasedFossil(){
        return null;
    }

    default TimePeriod getTimePeriod(BlockPos pos){
        return TimePeriod.byYValue(pos.getY());
    }

    default Dinosaur getDinosaur(){
        return Dinosaur.EMPTY;
    }
}
