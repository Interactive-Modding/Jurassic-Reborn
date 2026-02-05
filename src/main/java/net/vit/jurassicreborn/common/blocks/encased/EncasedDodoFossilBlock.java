

package net.vit.jurassicreborn.common.blocks.encased;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vit.jurassicreborn.common.blocks.entities.EncasedFaunaFossilBlockEntity;
import net.vit.jurassicreborn.common.blocks.fossil.EncasedFossil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EncasedDodoFossilBlock extends Block implements EncasedFossil, EntityBlock {
    public EncasedDodoFossilBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new EncasedFaunaFossilBlockEntity(pos, state);
    }
}
