package mod.reborn.client.gui.app;

import com.google.common.base.Predicate;
import mod.reborn.RebornMod;
import mod.reborn.client.gui.PaleoPadGui;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.GrowthStage;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.paleopad.MinimapApp;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;
import mod.reborn.server.paleopad.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinimapGuiApp extends GuiApp
{
    private static final ResourceLocation texture = new ResourceLocation(RebornMod.MODID, "textures/gui/paleo_pad/apps/minimap.png");

    private static final ResourceLocation entity = new ResourceLocation(RebornMod.MODID, "textures/gui/paleo_pad/apps/background/entity.png");

    public MinimapGuiApp(App app)
    {
        super(app);
    }

    private final Map<BlockPos, Integer> heights = new HashMap<BlockPos, Integer>();

    @Override
    public void render(int mouseX, int mouseY, PaleoPadGui gui) {
        super.renderButtons(mouseX, mouseY, gui);

        EntityPlayer player = mc.player;
        World world = mc.world;

        gui.drawScaledText("Loc: " + (int) player.posX + " " + (int) player.posY + " " + (int) player.posZ, 2, 3, 1.0F, 0xFFFFFF);

        int playerX = (int) player.posX;
        int playerZ = (int) player.posZ;

        int playerChunkX = playerX >> 4;
        int playerChunkZ = playerZ >> 4;

        int mapX = 0;
        int renderY = 0;
        int renderChunkX = 0;
        int renderChunkY = 0;

        gui.drawBoxOutline(89, 14, 16 * 8 + 1, 16 * 8 + 1, 1, 1.0F, 0x606060);


        for (int chunkX = playerChunkX - 4; chunkX < playerChunkX + 4; chunkX++) {
            for (int chunkZ = playerChunkZ - 4; chunkZ < playerChunkZ + 4; chunkZ++) {
                Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);

                if (!chunk.isEmpty()) {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            int blockX = x + (chunkX * 16);
                            int blockZ = z + (chunkZ * 16);

                            BlockPos pos = getHeight(world, blockX, blockZ);
                            int blockY = pos.getY();

                            IBlockState blockState = world.getBlockState(pos);

                            MapColor color = blockState.getMaterial().getMaterialMapColor();

                            int rgb = color.colorValue;

                            int r = (rgb >> 16) & 0xff;
                            int g = (rgb >> 8) & 0xff;
                            int b = rgb & 0xff;

                            int lightnessOffset = 0;

                            lightnessOffset -= getHeight(world, blockX - 1, blockZ).getY() > blockY ? 10 : 0;
                            lightnessOffset -= getHeight(world, blockX, blockZ - 1).getY() > blockY ? 10 : 0;
                            lightnessOffset -= getHeight(world, blockX + 1, blockZ).getY() > blockY ? 10 : 0;
                            lightnessOffset -= getHeight(world, blockX, blockZ + 1).getY() > blockY ? 10 : 0;

                            r = Math.min(Math.max(r + lightnessOffset, 0), 255);
                            g = Math.min(Math.max(g + lightnessOffset, 0), 255);
                            b = Math.min(Math.max(b + lightnessOffset, 0), 255);

                            rgb = r << 16 | g << 8 | b;

                            gui.drawScaledRect(mapX + (renderChunkX * 16) + 90, renderY + (renderChunkY * 16) + 15, 1, 1, 1.0F, rgb);

                            renderY++;
                        }

                        renderY = 0;
                        mapX++;
                    }
                }

                mapX = 0;
                renderY = 0;

                renderChunkY++;
            }

            renderChunkY = 0;
            renderChunkX++;
        }

        renderChunkX = 0;
        renderChunkY = 0;

        int trackedEntities = 0;

        for (int chunkX = playerChunkX - 4; chunkX < playerChunkX + 4; chunkX++) {
            for (int chunkZ = playerChunkZ - 4; chunkZ < playerChunkZ + 4; chunkZ++) {
                Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);

                if (!chunk.isEmpty()) {
                    for (Object e : getEntitiesInChunk(chunk, null, EntitySelectors.NOT_SPECTATING)) {
                        Entity entity = (Entity) e;

                        if (entity instanceof DinosaurEntity) {
                            DinosaurEntity dino = (DinosaurEntity) entity;
                            Dinosaur dinosaur = dino.getDinosaur();
                            if (dino.hasTracker() && canTrack(dinosaur, dino, player)) {
                                int colour;
                                if(dino.getHealth() <= dino.getMaxHealth()/100*50 && dino.getHealth() >= (dino.getMaxHealth()/100)*10) {
                                    colour = 0xFF9900;
                                }
                                else if (dino.getHealth() <= (dino.getMaxHealth()/100)*10 || dino.isCarcass()) {
                                    colour = 0xFF0000;
                                } else {
                                    colour = dino.isMale() ? dinosaur.getEggPrimaryColorMale() : dinosaur.getEggPrimaryColorFemale();
                                }

                                float red = (colour >> 16 & 255) / 255.0F;
                                float green = (colour >> 8 & 255) / 255.0F;
                                float blue = (colour & 255) / 255.0F;

                                GL11.glColor3f(red, green, blue);

                                mc.getTextureManager().bindTexture(MinimapGuiApp.entity);
                                int dinoX = (int) dino.posX;
                                int dinoZ = (int) dino.posZ;

                                int entityRenderX = (dinoX & 15) + (renderChunkX * 16) + 90 - 4;
                                int entityRenderY = (dinoZ & 15) + (renderChunkY * 16) + 15 - 4;

                                trackedEntities++;

                                gui.drawScaledTexturedModalRect(entityRenderX, entityRenderY, 0, 0, 16, 16, 16, 16, 0.6F);
                                gui.drawCenteredScaledText(dinosaur.getName(), entityRenderX + 5, entityRenderY + 8, 0.3F, colour);
                                gui.drawCenteredScaledText(dinoX + " " + (int) dino.posY + " " + dinoZ, entityRenderX + 5, entityRenderY + 8, 0.3F, 0xFFFFFF);
                            }
                        } else if (player == entity) {
                            mc.getTextureManager().bindTexture(MinimapGuiApp.entity);

                            gui.drawScaledText("You!", playerX, (int) player.posY, playerZ, 0xFFFFFF);
                            gui.drawScaledTexturedModalRect((playerX & 15) + (renderChunkX * 16) + 90 - 4, (playerZ & 15) + (renderChunkY * 16) + 15 - 4, 0, 0, 16, 16, 16, 16, 0.6F);
                        }
                    }
                }

                renderChunkY++;
            }

            renderChunkY = 0;
            renderChunkX++;
        }

        gui.drawScaledText("Tracked Entities: " + trackedEntities, 2, 13, 0.67F, 0xFFFFFF);
    }

    private BlockPos getHeight(World world, int x, int z)
    {
        BlockPos posKey = new BlockPos(x, 0, z);

        if (heights.containsKey(posKey))
        {
            return new BlockPos(x, heights.get(posKey), z);
        }
        else
        {
            int y = world.getHeight(posKey).getY();

            BlockPos pos = new BlockPos(x, y, z);

            while (world.isAirBlock(pos) || world.getBlockState(pos).getBlock() instanceof BlockLiquid)
            {
                y--;
                pos = new BlockPos(x, y, z);
            }

            BlockPos up = pos.add(0, 1, 0);

            if (!world.isAirBlock(up))
            {
                pos = up;
            }

            heights.put(posKey, pos.getY());

            return pos;
        }
    }

    private boolean canTrack(Dinosaur dinosaur, DinosaurEntity dinosaurEntity, EntityPlayer player) {
        return !dinosaur.isImprintable() || dinosaurEntity.getOwner().toString().equals(player.getUniqueID().toString()) && dinosaurEntity.getAgePercentage() >= 75;
    }


    /**
     * Fills the given list of all entities that intersect within the given bounding box that aren't the passed entity.
     */
    public List<Entity> getEntitiesInChunk(Chunk chunk, Entity exclude, Predicate<Entity> predicate)
    {
        List<Entity> entities = new ArrayList<Entity>();

        int i = MathHelper.floor((0 - World.MAX_ENTITY_RADIUS) / 16.0D);
        int j = MathHelper.floor((256 + World.MAX_ENTITY_RADIUS) / 16.0D);
        ClassInheritanceMultiMap<Entity>[] entityLists = chunk.getEntityLists();
        i = MathHelper.clamp(i, 0, entityLists.length - 1);
        j = MathHelper.clamp(j, 0, entityLists.length - 1);

        for (int k = i; k <= j; ++k)
        {
            for (Entity entity : entityLists[k])
            {
                if (entity != exclude && (predicate == null || predicate.apply(entity)))
                {
                    entities.add(entity);
                    Entity[] parts = entity.getParts();

                    if (parts != null)
                    {
                        for (Entity part : parts)
                        {
                            entity = part;

                            if (entity != exclude && (predicate == null || predicate.apply(entity)))
                            {
                                entities.add(entity);
                            }
                        }
                    }
                }
            }
        }

        return entities;
    }

    @Override
    public void actionPerformed(GuiButton button)
    {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, PaleoPadGui gui)
    {
        ScaledResolution dimensions = new ScaledResolution(mc);
        mouseX -= dimensions.getScaledWidth() / 2 - 115;
    }

    @Override
    public void init()
    {
        heights.clear();
    }

    public ResourceLocation getTexture(PaleoPadGui gui)
    {
        return texture;
    }
}