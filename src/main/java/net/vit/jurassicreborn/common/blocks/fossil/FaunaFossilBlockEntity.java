package net.vit.jurassicreborn.common.blocks.fossil;

import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.saveAdditional(pTag, provider);

        pTag.putString("Dinosaur", dinosaur.getName());

    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.loadAdditional(pTag, provider);

        this.dinosaur = Dinosaur.getDinosaurByName(pTag.getString("Dinosaur"));
    }
}
