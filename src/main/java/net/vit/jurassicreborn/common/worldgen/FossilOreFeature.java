package net.vit.jurassicreborn.common.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.stream.IntStream;

public class FossilOreFeature extends Feature<OreConfiguration> {
    public static final Codec<OreConfiguration> CODEC = OreConfiguration.CODEC;

    public FossilOreFeature() {
        super(CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<OreConfiguration> ctx) {
        RandomSource rand = ctx.random();
        BlockPos origin = ctx.origin();
        WorldGenLevel level = ctx.level();
        OreConfiguration original = ctx.config();

        // === expand into ALL bone_count variants ===
        var baseTarget = original.targetStates.get(0);
        var test      = baseTarget.target;
        var fossil    = baseTarget.state;
        var prop = fossil.getBlock()
                .getStateDefinition()
                .getProperty("bone_count");
        if (!(prop instanceof IntegerProperty boneProp))
            throw new IllegalStateException("Missing bone_count on "+fossil);

        int max = boneProp.getPossibleValues().stream().max(Integer::compare).orElse(1);
        List<OreConfiguration.TargetBlockState> variants = IntStream
                .rangeClosed(1, max)
                .mapToObj(i -> OreConfiguration.target(test, fossil.setValue(boneProp, i)))
                .toList();

        // build a new config with those variants
        OreConfiguration config = new OreConfiguration(
                variants,
                original.size,
                original.discardChanceOnAirExposure
        );

        float angle = rand.nextFloat() * (float)Math.PI;
        double dx1 = origin.getX() + Mth.sin(angle) * config.size / 8.0F;
        double dx2 = origin.getX() - Mth.sin(angle) * config.size / 8.0F;
        double dz1 = origin.getZ() + Mth.cos(angle) * config.size / 8.0F;
        double dz2 = origin.getZ() - Mth.cos(angle) * config.size / 8.0F;
        double dy1 = origin.getY() + rand.nextInt(3) - 1;
        double dy2 = origin.getY() + rand.nextInt(3) - 1;

        // --- Y BAND ENFORCEMENT: ---
        int centerY = origin.getY();
        int spreadY = 2; // Only generate fossils from (origin.getY() - 2) to (origin.getY() + 2)
        int minY = Math.max(centerY - spreadY, level.getMinBuildHeight());
        int maxY = Math.min(centerY + spreadY, level.getMaxBuildHeight());
        int minX = origin.getX() - Mth.ceil(config.size / 8.0F) - 1;
        int minZ = origin.getZ() - Mth.ceil(config.size / 8.0F) - 1;
        int width  = 2 * (Mth.ceil(config.size / 8.0F) + 1);

        // Only generate in this Y range!
        return doPlace(level, rand, config,
                dx1, dx2, dz1, dz2, dy1, dy2,
                minX, minY, maxY, minZ, width);
    }

    private boolean doPlace(WorldGenLevel level,
                            RandomSource rand,
                            OreConfiguration config,
                            double dx1, double dx2,
                            double dz1, double dz2,
                            double dy1, double dy2,
                            int minX, int minY, int maxY, int minZ, int width) {
        int placed = 0;
        int height = maxY - minY + 1;
        if (width <= 0 || height <= 0) {
            return false;
        }
        BitSet bitset = new BitSet(width * height * width);
        var pos = new BlockPos.MutableBlockPos();
        int size = config.size;
        double[] coords = new double[size * 4];

        // carve shape (identical to vanilla)
        for (int i = 0; i < size; ++i) {
            float f = (float)i / size;
            coords[i*4 + 0] = Mth.lerp(f, dx1, dx2);
            coords[i*4 + 1] = Mth.lerp(f, dy1, dy2);
            coords[i*4 + 2] = Mth.lerp(f, dz1, dz2);
            double r = (rand.nextDouble() * size) / 16.0D;
            coords[i*4 + 3] = ((Mth.sin((float)Math.PI*f) + 1f)*r + 1d)/2d;
        }
        for (int i = 0; i < size-1; ++i) {
            if (coords[i*4+3] > 0) for (int j = i+1; j < size; ++j) {
                if (coords[j*4+3] > 0) {
                    double dx = coords[i*4] - coords[j*4];
                    double dy = coords[i*4+1] - coords[j*4+1];
                    double dz = coords[i*4+2] - coords[j*4+2];
                    double dr = coords[i*4+3] - coords[j*4+3];
                    if (dr*dr > dx*dx + dy*dy + dz*dz) {
                        if (dr > 0) coords[j*4+3] = -1; else coords[i*4+3] = -1;
                    }
                }
            }
        }

        // placement
        try (var access = new BulkSectionAccess(level)) {
            for (int i = 0; i < size; ++i) {
                double r = coords[i*4+3];
                if (r < 0) continue;
                double cx = coords[i*4], cy = coords[i*4+1], cz = coords[i*4+2];

                int x0 = Math.max(Mth.floor(cx - r), minX);
                int y0 = Math.max(Mth.floor(cy - r), minY);
                int z0 = Math.max(Mth.floor(cz - r), minZ);
                int x1 = Math.min(Mth.floor(cx + r), x0 + width);
                int y1 = Math.min(Mth.floor(cy + r), maxY);
                int z1 = Math.min(Mth.floor(cz + r), z0 + width);

                for (int x = x0; x <= x1; x++) {
                    double dx = ((double)x + .5 - cx) / r;
                    if (dx*dx < 1) {
                        for (int y = y0; y <= y1; y++) {
                            if (y < minY || y > maxY) continue; // Clamp Y!
                            double dy = ((double)y + .5 - cy) / r;
                            if (dx*dx + dy*dy < 1) {
                                for (int z = z0; z <= z1; z++) {
                                    double dz = ((double)z + .5 - cz) / r;
                                    if (dx*dx + dy*dy + dz*dz < 1) {
                                        int idx = (x-minX) + (y-minY)*width + (z-minZ)*width*height;
                                        if (!bitset.get(idx)) {
                                            bitset.set(idx);
                                            pos.set(x,y,z);
                                            if (!level.isOutsideBuildHeight(y)) {
                                                LevelChunkSection sect = access.getSection(pos);
                                                if (sect != null) {
                                                    var curr = sect.getBlockState(
                                                            SectionPos.sectionRelative(x),
                                                            SectionPos.sectionRelative(y),
                                                            SectionPos.sectionRelative(z)
                                                    );
                                                    var list = new ArrayList<>(config.targetStates);
                                                    for (int j = list.size()-1; j>0; j--) {
                                                        int k = rand.nextInt(j+1);
                                                        var tmp = list.get(j);
                                                        list.set(j, list.get(k));
                                                        list.set(k, tmp);
                                                    }
                                                    // try in that order
                                                    for (var target : list) {
                                                        if (OreFeature.canPlaceOre(
                                                                curr,
                                                                access::getBlockState,
                                                                rand,
                                                                config,
                                                                target,
                                                                pos
                                                        )) {
                                                            sect.setBlockState(
                                                                    SectionPos.sectionRelative(x),
                                                                    SectionPos.sectionRelative(y),
                                                                    SectionPos.sectionRelative(z),
                                                                    target.state,
                                                                    false
                                                            );
                                                            placed++;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return placed > 0;
    }
}
