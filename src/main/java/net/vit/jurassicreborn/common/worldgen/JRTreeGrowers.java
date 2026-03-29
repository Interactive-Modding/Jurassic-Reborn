package net.vit.jurassicreborn.common.worldgen;

import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

/**
 * Central location for all custom TreeGrowers used by Jurassic Reborn saplings.
 *
 * TreeGrowers are NOT registered objects.
 * They are lightweight holders that reference configured features.
 *
 * Safe to reference directly from SaplingBlock constructors.
 */
public final class JRTreeGrowers {

    public static final TreeGrower ARAUCARIA =
            new TreeGrower(
                    "araucaria",
                    Optional.empty(), // mega tree
                    Optional.of(ModConfiguredFeatures.ARAUCARIA),
                    Optional.empty()  // flowers
            );

    public static final TreeGrower GINKGO =
            new TreeGrower(
                    "ginkgo",
                    Optional.empty(),
                    Optional.of(ModConfiguredFeatures.GINKGO),
                    Optional.empty()
            );

    public static final TreeGrower CALAMITES =
            new TreeGrower(
                    "calamites",
                    Optional.empty(),
                    Optional.of(ModConfiguredFeatures.CALAMITES),
                    Optional.empty()
            );

    public static final TreeGrower PHOENIX =
            new TreeGrower(
                    "phoenix",
                    Optional.empty(),
                    Optional.of(ModConfiguredFeatures.PHOENIX),
                    Optional.empty()
            );

    public static final TreeGrower PSARONIUS =
            new TreeGrower(
                    "psaronius",
                    Optional.empty(),
                    Optional.of(ModConfiguredFeatures.PSARONIUS),
                    Optional.empty()
            );

    public static final TreeGrower MAGNOLIA =
            new TreeGrower(
                    "magnolia",
                    Optional.empty(),
                    Optional.of(ModConfiguredFeatures.MAGNOLIA),
                    Optional.empty()
            );

    /** Utility class — no instantiation */
    private JRTreeGrowers() {}
}
