package net.vit.jurassicreborn.client.render.entity;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;
import net.vit.jurassicreborn.common.entities.vehicle.WheelParticleData;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;
import java.util.List;

@Mod.EventBusSubscriber(modid = JurassicReborn.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class TyretrackRenderer {
    public static final List<Material> ALLOWED_MATERIALS = List.of(
            Material.GRASS, Material.DIRT, Material.SAND
    );

    public static final ResourceLocation TYRE_TRACKS =
            new ResourceLocation(JurassicReborn.MODID, "textures/misc/tyre-tracks.png");

    private static final List<List<WheelParticleData>> DEAD_CARS_LISTS = Lists.newArrayList();

    @SubscribeEvent
    public static void onRenderStage(RenderLevelStageEvent e) {
        if (e.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (!(level instanceof ClientLevel clientLevel)) return;

        Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(
                () -> Minecraft.getInstance().gameRenderer.getPositionTexColorShader());
        RenderSystem.setShaderTexture(0, TYRE_TRACKS);
        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        List<Pair<Long, Runnable>> runList = Lists.newArrayList();

        for (Entity ent : clientLevel.entitiesForRendering()) {
            if (ent instanceof VehicleEntity car) {
                for (List<WheelParticleData> list : car.wheelDataList) {
                    pushListQuads(list, buf, e.getPartialTick(), runList, clientLevel, camPos);
                }
            }
        }
        DEAD_CARS_LISTS.forEach(list ->
                pushListQuads(list, buf, e.getPartialTick(), runList, clientLevel, camPos));

        runList.sort(Comparator.comparing(Pair::getLeft, Comparator.reverseOrder()));
        runList.forEach(p -> p.getRight().run());

        Tesselator.getInstance().end();
        RenderSystem.disableBlend();
    }

    private static void pushListQuads(List<WheelParticleData> data,
                                      BufferBuilder buf,
                                      float pt,
                                      List<Pair<Long, Runnable>> out,
                                      Level level,
                                      Vec3 camPos) {

        for (int i = 0; i < data.size() - 1; i++) {
            WheelParticleData a = data.get(i);
            WheelParticleData b = data.get(i + 1);
            if (!a.shouldRender()) continue;

            Vec3 sv = a.getPosition();
            Vec3 ev = b.getPosition();
            Vec3 svOpp = a.getOppositePosition();
            Vec3 evOpp = b.getOppositePosition();

            BlockPos sPos = BlockPos.containing(sv.x, sv.y, sv.z);
            BlockPos ePos = BlockPos.containing(ev.x, ev.y, ev.z);

            if (sv.y != ev.y || !isAccepted(level, sPos) || !isAccepted(level, ePos)) continue;

            final int idx = i;
            out.add(Pair.of(a.getWorldTime(), () -> {
                double d = 0.5D / sv.distanceTo(svOpp);
                Vec3 vA = svOpp.subtract(sv).scale(d);

                double d1 = 0.5D / ev.distanceTo(evOpp);
                Vec3 vB = evOpp.subtract(ev).scale(d1);

                float lightA = level.getBrightness(LightLayer.BLOCK, sPos);
                float lightB = level.getBrightness(LightLayer.BLOCK, ePos);

                float alphaA = a.getAlpha(pt);
                float alphaB = b.getAlpha(pt);

                double off = (idx + 2) * 0.0001D;

                quad(buf, sv, vA, ev, vB, off, lightA, lightB, alphaA, alphaB, camPos);
                quad(buf, sv, vA, ev, vB, off, lightA, lightB, alphaA, alphaB, camPos, true);
            }));
        }
    }

    private static void quad(BufferBuilder buf,
                             Vec3 sv, Vec3 vA,
                             Vec3 ev, Vec3 vB,
                             double off,
                             float lightA, float lightB,
                             float alphaA, float alphaB,
                             Vec3 camPos) {

        quad(buf, sv, vA, ev, vB, off, lightA, lightB, alphaA, alphaB, camPos, false);
    }

    private static void quad(BufferBuilder buf,
                             Vec3 sv, Vec3 vA,
                             Vec3 ev, Vec3 vB,
                             double off,
                             float lightA, float lightB,
                             float alphaA, float alphaB,
                             Vec3 camPos,
                             boolean flip) {

        if (flip) {
            buf.vertex(ev.x - camPos.x - vB.x, ev.y - camPos.y + off, ev.z - camPos.z - vB.z).uv(1, 1).color(lightB, lightB, lightB, alphaB).endVertex();
            buf.vertex(ev.x - camPos.x + vB.x, ev.y - camPos.y + off, ev.z - camPos.z + vB.z).uv(1, 0).color(lightB, lightB, lightB, alphaB).endVertex();
            buf.vertex(sv.x - camPos.x + vA.x, sv.y - camPos.y + off, sv.z - camPos.z + vA.z).uv(0, 0).color(lightA, lightA, lightA, alphaA).endVertex();
            buf.vertex(sv.x - camPos.x - vA.x, sv.y - camPos.y + off, sv.z - camPos.z - vA.z).uv(0, 1).color(lightA, lightA, lightA, alphaA).endVertex();
        } else {
            buf.vertex(sv.x - camPos.x + vA.x, sv.y - camPos.y + off, sv.z - camPos.z + vA.z).uv(0, 0).color(lightA, lightA, lightA, alphaA).endVertex();
            buf.vertex(sv.x - camPos.x - vA.x, sv.y - camPos.y + off, sv.z - camPos.z - vA.z).uv(0, 1).color(lightA, lightA, lightA, alphaA).endVertex();
            buf.vertex(ev.x - camPos.x - vB.x, ev.y - camPos.y + off, ev.z - camPos.z - vB.z).uv(1, 1).color(lightB, lightB, lightB, alphaB).endVertex();
            buf.vertex(ev.x - camPos.x + vB.x, ev.y - camPos.y + off, ev.z - camPos.z + vB.z).uv(1, 0).color(lightB, lightB, lightB, alphaB).endVertex();
        }
    }

    private static boolean isAccepted(Level level, BlockPos pos) {
        BlockState ground = level.getBlockState(pos);
        return ALLOWED_MATERIALS.contains(ground.getMaterial()) &&
                ground.isFaceSturdy(level, pos, Direction.UP) &&
                level.getBlockState(pos.above()).getMaterial() != Material.WATER;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.START) return;
        DEAD_CARS_LISTS.removeIf(list -> {
            List<WheelParticleData> dead = Lists.newArrayList();
            list.forEach(w -> w.tick(dead));
            dead.forEach(list::remove);
            return list.isEmpty();
        });
    }

    public static void uploadList(VehicleEntity v) {
        if (!v.level.isClientSide) return;
        for (List<WheelParticleData> l : v.wheelDataList) DEAD_CARS_LISTS.add(0, l);
    }
}