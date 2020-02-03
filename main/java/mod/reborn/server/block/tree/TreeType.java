package mod.reborn.server.block.tree;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.item.ItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import mod.reborn.server.plant.Plant;
import mod.reborn.server.plant.PlantHandler;
import mod.reborn.server.world.tree.AraucariaTreeGenerator;
import mod.reborn.server.world.tree.CalamitesTreeGenerator;
import mod.reborn.server.world.tree.GinkgoTreeGenerator;
import mod.reborn.server.world.tree.PhoenixTreeGenerator;
import mod.reborn.server.world.tree.PsaroniusTreeGenerator;

import java.util.function.Supplier;

public enum TreeType {
    GINKGO(PlantHandler.GINKGO, new GinkgoTreeGenerator()),
    CALAMITES(PlantHandler.CALAMITES, new CalamitesTreeGenerator()),
    PSARONIUS(PlantHandler.PSARONIUS, new PsaroniusTreeGenerator()),
    PHOENIX(PlantHandler.PHOENIX, new PhoenixTreeGenerator(), 5, () -> new ItemStack(ItemHandler.PHOENIX_FRUIT)),
    ARAUCARIA(PlantHandler.ARAUCARIA, new AraucariaTreeGenerator());

    private WorldGenAbstractTree generator;
    private Plant plant;
    private int dropChance;
    private Supplier<ItemStack> drop;

    TreeType(Plant plant, WorldGenAbstractTree generator) {
        this(plant, generator, 10);
    }

    TreeType(Plant plant, WorldGenAbstractTree generator, int dropChance) {
        this(plant, generator, dropChance, null);
        this.setDrop(() -> new ItemStack(BlockHandler.ANCIENT_SAPLINGS.get(this)));
    }

    TreeType(Plant plant, WorldGenAbstractTree generator, int dropChance, Supplier<ItemStack> drop) {
        this.plant = plant;
        this.generator = generator;
        this.dropChance = dropChance;
        this.drop = drop;
    }

    public void setDrop(Supplier<ItemStack> drop) {
        this.drop = drop;
    }

    public int getDropChance() {
        return this.dropChance;
    }

    public ItemStack getDrop() {
        return this.drop.get();
    }

    public WorldGenAbstractTree getTreeGenerator() {
        return this.generator;
    }

    public Plant getPlant() {
        return this.plant;
    }
}
