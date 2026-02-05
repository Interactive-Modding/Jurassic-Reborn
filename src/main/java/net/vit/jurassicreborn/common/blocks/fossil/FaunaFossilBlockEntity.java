package net.vit.jurassicreborn.common.blocks.fossil;

import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FaunaFossilBlockEntity extends BlockEntity {


    private Dinosaur dinosaur = Dinosaur.EMPTY;

    public FaunaFossilBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FAUNA_FOSSIL_BLOCK_ENTITY.get(), pPos, pBlockState);


    }

    public void setDinosaur(Dinosaur newDino){
        if(dinosaur == Dinosaur.EMPTY){
            this.dinosaur = newDino;
        }else{
            JurassicReborn.getLogger().debug("Dinosaur tried to be set in a non-empty fossil!");
        }
    }

    public void updateDinosaur(){
        FaunaFossil.setDinosaurFromPos(this.getBlockPos(), this.getLevel(), this.getLevel().getRandom());
    }

    public Dinosaur getDinosaur(){
//        FaunaFossil.setDinosaurFromPos(this.getBlockPos(), this.getLevel(), this.getLevel().getRandom());
        return this.dinosaur;
    }


    @Override
    public void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.putString("Dinosaur", dinosaur.getName());

    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.dinosaur = Dinosaur.getDinosaurByName(pTag.getString("Dinosaur"));
    }
}
