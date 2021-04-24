package mod.reborn.server.event;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.FossilizedTrackwayBlock;
import mod.reborn.server.block.plant.DoublePlantBlock;
import mod.reborn.server.conf.RebornConfig;
import mod.reborn.server.datafixers.PlayerData;
import mod.reborn.server.entity.animal.EntityShark;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.util.GameRuleHandler;
import net.ilexiconn.llibrary.server.capability.EntityDataHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import mod.reborn.server.entity.vehicle.HelicopterEntity;
import mod.reborn.server.world.WorldGenCoal;
import mod.reborn.server.world.loot.Loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServerEventHandler {

    @SubscribeEvent
    public void onEntityConstruct(EntityEvent.EntityConstructing event)
    {
        if (event.getEntity() instanceof EntityPlayer)
        {
            EntityDataHandler.INSTANCE.registerExtendedEntityData((EntityPlayer) event.getEntity(), new PlayerData());
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        GameRuleHandler.register(event.getWorld());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void decorate(DecorateBiomeEvent.Pre event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        Random rand = event.getRand();

        Biome biome = world.getBiome(pos);

        BiomeDecorator decorator = biome.decorator;

        if (RebornConfig.MINERAL_GENERATION.plantFossilGeneration) {
            if (decorator != null && decorator.chunkProviderSettings != null && !(decorator.coalGen instanceof WorldGenCoal)) {
                decorator.coalGen = new WorldGenCoal(Blocks.COAL_ORE.getDefaultState(), decorator.chunkProviderSettings.coalSize);
            }
        }

        if (RebornConfig.PLANT_GENERATION.mossGeneration) {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.CONIFEROUS) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
                if (rand.nextInt(8) == 0) {
                    BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);

                    if (world.getBlockState(topBlock.down()).isOpaqueCube() && !world.getBlockState(topBlock).getMaterial().isLiquid()) {
                        world.setBlockState(topBlock, BlockHandler.MOSS.getDefaultState(), 2 | 16);
                    }
                }
            }
        }

        if (RebornConfig.PLANT_GENERATION.flowerGeneration) {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
                if (rand.nextInt(8) == 0) {
                    BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);
                    if (world.getBlockState(topBlock.down()).isOpaqueCube() && !world.getBlockState(topBlock).getMaterial().isLiquid()) {
                        world.setBlockState(topBlock.up(), BlockHandler.WEST_INDIAN_LILAC.getDefaultState(), 2 | 16);
                        world.setBlockState(topBlock, BlockHandler.WEST_INDIAN_LILAC.getDefaultState().withProperty(DoublePlantBlock.HALF, DoublePlantBlock.BlockHalf.LOWER), 2 | 16);
                    }
                }
            }
        }

        if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
            if (rand.nextInt(8) == 0) {
                BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);
                if (world.getBlockState(topBlock.down()).isOpaqueCube() && !world.getBlockState(topBlock).getMaterial().isLiquid()) {
                    world.setBlockState(topBlock.up(), BlockHandler.HELICONIA.getDefaultState(), 2 | 16);
                    world.setBlockState(topBlock, BlockHandler.HELICONIA.getDefaultState().withProperty(DoublePlantBlock.HALF, DoublePlantBlock.BlockHalf.LOWER), 2 | 16);
                }
            }
        }

        if (RebornConfig.PLANT_GENERATION.gracilariaGeneration) {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)) {
                if (rand.nextInt(8) == 0) {
                    BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);

                    if (topBlock.getY() < 62) {
                        IBlockState state = world.getBlockState(topBlock.down());

                        if (state.isOpaqueCube()) {
                            world.setBlockState(topBlock, BlockHandler.GRACILARIA.getDefaultState(), 2 | 16);
                        }
                    }
                }
            }
        }

        if (RebornConfig.PLANT_GENERATION.peatGeneration) {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
                if (rand.nextInt(2) == 0) {
                    new WorldGenMinable(BlockHandler.PEAT.getDefaultState(), 5, input -> input == Blocks.DIRT.getDefaultState() || input == Blocks.GRASS.getDefaultState()).generate(world, rand, world.getTopSolidOrLiquidBlock(pos));
                }
            }
        }

        if (RebornConfig.MINERAL_GENERATION.trackwayGeneration) {
            int footprintChance = 20;

            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER)) {
                footprintChance = 10;
            }

            if (rand.nextInt(footprintChance) == 0) {
                int y = rand.nextInt(20) + 30;

                FossilizedTrackwayBlock.TrackwayType type = FossilizedTrackwayBlock.TrackwayType.values()[rand.nextInt(FossilizedTrackwayBlock.TrackwayType.values().length)];

                for (int i = 0; i < rand.nextInt(2) + 1; i++) {
                    BlockPos basePos = new BlockPos(pos.getX() + rand.nextInt(10) + 3, y, pos.getZ() + rand.nextInt(10) + 3);

                    float angle = (float) (rand.nextDouble() * 360.0F);

                    IBlockState trackway = BlockHandler.FOSSILIZED_TRACKWAY.getDefaultState().withProperty(FossilizedTrackwayBlock.FACING, EnumFacing.fromAngle(angle)).withProperty(FossilizedTrackwayBlock.VARIANT, type);

                    float xOffset = -MathHelper.sin((float) Math.toRadians(angle));
                    float zOffset = MathHelper.cos((float) Math.toRadians(angle));

                    for (int l = 0; l < rand.nextInt(2) + 3; l++) {
                        BlockPos trackwayPos = basePos.add(xOffset * l, 0, zOffset * l);

                        if (world.getBlockState(trackwayPos).getBlock() == Blocks.STONE) {
                            world.setBlockState(trackwayPos, trackway, 2 | 16);
                        }
                    }
                }
            }
        }
    }




    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation name = event.getName();

        LootTable table = event.getTable();

        Loot.handleTable(table, name);
    }
    @SubscribeEvent
    public void fall(LivingFallEvent e){
        e.setCanceled(e.getEntity().getRidingEntity() instanceof HelicopterEntity);
    }
    @SubscribeEvent
    public void onHarvest(BlockEvent.HarvestDropsEvent event) {
        IBlockState state = event.getState();
        Random rand = event.getWorld().rand;
        if (rand.nextInt(2) == 0) {
            List<Item> bugs = new ArrayList<>();
            if (state.getBlock() == Blocks.HAY_BLOCK) {
                bugs.add(ItemHandler.COCKROACHES);
                bugs.add(ItemHandler.MEALWORM_BEETLES);
            } else if (state.getBlock() == Blocks.GRASS) {
                if (rand.nextInt(6) == 0) {
                    bugs.add(ItemHandler.CRICKETS);
                }
            } else if (state.getBlock() == Blocks.TALLGRASS) {
                if (rand.nextInt(5) == 0) {
                    bugs.add(ItemHandler.CRICKETS);
                }
            } else if (state.getBlock() == Blocks.PUMPKIN || state.getBlock() == Blocks.MELON_BLOCK) {
                bugs.add(ItemHandler.COCKROACHES);
                bugs.add(ItemHandler.MEALWORM_BEETLES);
            } else if (state.getBlock() == Blocks.COCOA) {
                bugs.add(ItemHandler.COCKROACHES);
                bugs.add(ItemHandler.MEALWORM_BEETLES);
            }
            if (bugs.size() > 0) {
                event.getDrops().add(new ItemStack(bugs.get(rand.nextInt(bugs.size()))));
            }
        }
    }

    @SubscribeEvent
    public void preventSharkOverspawning(EntityJoinWorldEvent e)
    {
        if(e.getWorld().isRemote) return;
        if(!(e.getEntity() instanceof EntityShark)) return;
        if(((EntityShark)e.getEntity()).isSpawnedByEgg) return;
        AxisAlignedBB aa_bb = new AxisAlignedBB(e.getEntity().posX-64, 0, e.getEntity().posZ-64, e.getEntity().posX+64, e.getWorld().getSeaLevel(), e.getEntity().posZ+64);
        int count = e.getWorld().getEntitiesWithinAABB(EntityShark.class, aa_bb).size();
        if(count >= 8)
        {
            System.out.println("shark removed");
            e.getEntity().setDead();
        }
    }
}
