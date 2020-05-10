package mod.reborn.client.render.entity;

import java.util.Comparator;
import java.util.List;

import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;
import mod.reborn.RebornMod;
import mod.reborn.server.entity.vehicle.VehicleEntity;
import mod.reborn.server.entity.vehicle.util.WheelParticleData;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid= RebornMod.MODID, value = Side.CLIENT)
public class TyretrackRenderer {
    
    public static final List<Material> ALLOWED_MATERIALS = Lists.newArrayList(Material.GRASS, Material.GROUND, Material.SAND);//TODO: configurable ?
    public static final ResourceLocation TYRE_TRACKS_LOCATION = new ResourceLocation(RebornMod.MODID, "textures/misc/tyre-tracks.png");
    
    private static final List<List<WheelParticleData>> DEAD_CARS_LISTS = Lists.newArrayList(); 
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.world;
        EntityPlayer player = mc.player;
        Vec3d playerPos = player.getPositionVector();
        
        GlStateManager.enableBlend();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
        
        mc.getTextureManager().bindTexture(TYRE_TRACKS_LOCATION);

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();
        
        double d0 = (player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)event.getPartialTicks());
        double d1 = (player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)event.getPartialTicks());
        double d2 = (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)event.getPartialTicks());
        buffer.setTranslation(-d0, -d1, -d2);
        
        List<Pair<Long, Runnable>> runList = Lists.newArrayList();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        for(Entity entity : Lists.reverse(Lists.newArrayList(world.loadedEntityList))) {
            if(entity instanceof VehicleEntity) {
            VehicleEntity car = (VehicleEntity)entity;
                for(List<WheelParticleData> list : car.wheelDataList) {
                    renderList(list, buffer, event.getPartialTicks(), runList);
                }
            }
        }
        DEAD_CARS_LISTS.forEach(list -> renderList(list, buffer, event.getPartialTicks(), runList));
        
        runList.sort((o1, o2) -> Comparator.<Long>naturalOrder().compare(o2.getLeft(), o1.getLeft()));
        
        GlStateManager.pushMatrix();
        runList.forEach(pair -> pair.getRight().run());
        GlStateManager.popMatrix();
        
        tess.draw();
        buffer.setTranslation(0, 0, 0);
            
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
    }
    
    @SideOnly(Side.CLIENT)
    private static void renderList(List<WheelParticleData> dataList, BufferBuilder buffer, float partialTicks, List<Pair<Long, Runnable>> list) {
        World world = Minecraft.getMinecraft().world;
	for(int i = 0; i < dataList.size() - 1; i++) {
            WheelParticleData start = dataList.get(i);
            if(!start.shouldRender()) {
        	continue;
            }
            WheelParticleData end = dataList.get(i + 1);
                
            Vec3d sv = start.getPosition();
            Vec3d ev = end.getPosition();
            
            Vec3d startOpposite = start.getOppositePosition();
            Vec3d endOpposite = end.getOppositePosition();

            
            BlockPos position = new BlockPos(sv);
            BlockPos endPosition = new BlockPos(ev);
            
            if(sv.y != ev.y || !isStateAccepted(world, position) || !isStateAccepted(world, endPosition)) {
        	continue;
            }
            
            final int n = i;
            list.add(Pair.of(start.getWorldTime(), () -> {
        	  double d = 1D / Math.sqrt(Math.pow(sv.x - startOpposite.x, 2) + Math.pow(sv.z - startOpposite.z, 2)) / 2D;    
                  Vec3d vec = new Vec3d((sv.x - startOpposite.x) * d, 0, (sv.z - startOpposite.z) * d);
                  
                  double d1 = 1D / Math.sqrt(Math.pow(ev.x - endOpposite.x, 2) + Math.pow(ev.z - endOpposite.z, 2)) / 2D;    
                  Vec3d vec1 = new Vec3d((ev.x - endOpposite.x) * d, 0, (ev.z - endOpposite.z) * d);
                  
                  float sl = world.getLightBrightness(position);
                  float el = world.getLightBrightness(endPosition);
                      
                      
                  float sa = start.getAlpha(partialTicks);
                  float ea = end.getAlpha(partialTicks);
                                      
                  double offset = (n + 2) * 0.0001D; //No z-fighting on my watch //TODO: remove n
                  
                  buffer.pos(sv.x + vec.x / 2D, sv.y + offset, sv.z + vec.z / 2D).tex(0, 0).color(sl, sl, sl, sa).endVertex();
                  buffer.pos(sv.x - vec.x / 2D, sv.y + offset, sv.z - vec.z / 2D).tex(0, 1).color(sl, sl, sl, sa).endVertex();
                  buffer.pos(ev.x - vec1.x / 2D, ev.y + offset, ev.z - vec1.z / 2D).tex(1, 1).color(el, el, el, ea).endVertex();
                  buffer.pos(ev.x + vec1.x / 2D, ev.y + offset, ev.z + vec1.z / 2D).tex(1, 0).color(el, el, el, ea).endVertex();
                      
                  //Flip quad to render upside down. Means when looking at tyre track from underneath, it still rendered. Needed because one set of tyres are upside down. //TODO: Fix that
                  buffer.pos(sv.x + vec.x / 2D, sv.y + offset, sv.z + vec.z / 2D).tex(0, 0).color(sl, sl, sl, sa).endVertex();
                  buffer.pos(ev.x + vec1.x / 2D, ev.y + offset, ev.z + vec1.z / 2D).tex(1, 0).color(el, el, el, ea).endVertex();
                  buffer.pos(ev.x - vec1.x / 2D, ev.y + offset, ev.z - vec1.z / 2D).tex(1, 1).color(el, el, el, ea).endVertex();
                  buffer.pos(sv.x - vec.x / 2D, sv.y + offset, sv.z - vec.z / 2D).tex(0, 1).color(sl, sl, sl, sa).endVertex();
            }));
        }
    }
    
    private static boolean isStateAccepted(World world, BlockPos position) {
	BlockPos downPos = position.down();
        IBlockState downState = world.getBlockState(downPos);
        return !world.getBlockState(position).getMaterial().isLiquid() && downState.isSideSolid(world, downPos, EnumFacing.UP) && ALLOWED_MATERIALS.contains(downState.getMaterial());
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onWorldTick(ClientTickEvent event) {
	List<List<WheelParticleData>> emptyLists = Lists.newArrayList();
	DEAD_CARS_LISTS.forEach(list -> {
	    List<WheelParticleData> markedRemoved = Lists.newArrayList();
	    list.forEach(wheel -> wheel.onUpdate(markedRemoved));
	    markedRemoved.forEach(list::remove);
	    markedRemoved.clear();
	    if(list.isEmpty()) {
		emptyLists.add(list);
	    }
	});
	emptyLists.forEach(DEAD_CARS_LISTS::remove);
    }
    
    public static void uploadList(VehicleEntity entity) {
	if(entity.world.isRemote) {
	    for(List<WheelParticleData> list : entity.wheelDataList) {
		DEAD_CARS_LISTS.add(0, list);
	    }
	}
    }
}