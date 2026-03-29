//package net.vit.jurassicreborn.client.render.entity;
//
//import com.google.common.collect.Lists;
//import com.mojang.blaze3d.systems.RenderSystem;
//import net.minecraft.client.renderer.GameRenderer;
//import net.minecraft.client.Minecraft;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.LightLayer;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.Vec3;
//import net.neoforged.api.distmarker.Dist;
//
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.client.event.ClientTickEvent;
//import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
//import net.vit.jurassicreborn.JurassicReborn;
//import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;
//import net.vit.jurassicreborn.common.entities.vehicle.WheelParticleData;
//import org.apache.commons.lang3.tuple.Pair;
//
//import java.util.Comparator;
//import java.util.List;
//
//@EventBusSubscriber(modid = JurassicReborn.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
//public final class TyretrackRenderer {
//    public static final ResourceLocation TYRE_TRACKS =
//            JurassicReborn.resource("textures/misc/tyre-tracks.png");
//
//    private static final List<List<WheelParticleData>> DEAD_CARS_LISTS = Lists.newArrayList();
//
//    @SubscribeEvent
//    public static void onRenderStage(RenderLevelStageEvent e) {
//        if (e.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
//        Minecraft mc = Minecraft.getInstance();
//        Level level = mc.level;
//        if (!(level instanceof ClientLevel clientLevel)) return;
//
//        Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();
//
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        RenderSystem.setShader(
//                () -> Minecraft.getInstance().gameRenderer.getPositionTexColorShader());
//        RenderSystem.setShaderTexture(0, TYRE_TRACKS);
//        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
//        var renderType = net.minecraft.client.renderer.RenderType.entityTranslucent(TYRE_TRACKS);
//        VertexConsumer buf = bufferSource.getBuffer(renderType);
//
//        List<Pair<Long, Runnable>> runList = Lists.newArrayList();
//        float partialTick = mc.getTimer().getGameTimeDeltaPartialTick(false);
//
//        for (Entity ent : clientLevel.entitiesForRendering()) {
//            if (ent instanceof VehicleEntity car) {
//                for (List<WheelParticleData> list : car.wheelDataList) {
//                    pushListQuads(list, buf, partialTick, runList, clientLevel, camPos);
//                }
//            }
//        }
//        DEAD_CARS_LISTS.forEach(list ->
//                pushListQuads(list, buf, partialTick, runList, clientLevel, camPos));
//
//        runList.sort(Comparator.comparing(Pair::getLeft, Comparator.reverseOrder()));
//        runList.forEach(p -> p.getRight().run());
//
//        bufferSource.endBatch(renderType);
//        RenderSystem.disableBlend();
//    }
//
//    private static void pushListQuads(List<WheelParticleData> data,
//                                      VertexConsumer buf,
//                                      float pt,
//                                      List<Pair<Long, Runnable>> out,
//                                      Level level,
//                                      Vec3 camPos) {
//
//        for (int i = 0; i < data.size() - 1; i++) {
//            WheelParticleData a = data.get(i);
//            WheelParticleData b = data.get(i + 1);
//            if (!a.shouldRender()) continue;
//
//            Vec3 sv = a.getPosition();
//            Vec3 ev = b.getPosition();
//            Vec3 svOpp = a.getOppositePosition();
//            Vec3 evOpp = b.getOppositePosition();
//
//            BlockPos sPos = BlockPos.containing(sv.x, sv.y, sv.z);
//            BlockPos ePos = BlockPos.containing(ev.x, ev.y, ev.z);
//
//            if (!isAccepted(level, sPos) || !isAccepted(level, ePos)) continue;
//
//            final int idx = i;
//            out.add(Pair.of(a.getWorldTime(), () -> {
//                double d = 0.5D / sv.distanceTo(svOpp);
//                Vec3 vA = svOpp.subtract(sv).scale(d);
//
//                double d1 = 0.5D / ev.distanceTo(evOpp);
//                Vec3 vB = evOpp.subtract(ev).scale(d1);
//
//                float lightA = level.getBrightness(LightLayer.BLOCK, sPos);
//                float lightB = level.getBrightness(LightLayer.BLOCK, ePos);
//
//                float alphaA = a.getAlpha(pt);
//                float alphaB = b.getAlpha(pt);
//
//                double off = (idx + 2) * 0.0001D;
//
//                quad(buf, sv, vA, ev, vB, off, lightA, lightB, alphaA, alphaB, camPos);
//                quad(buf, sv, vA, ev, vB, off, lightA, lightB, alphaA, alphaB, camPos, true);
//            }));
//        }
//    }
//
//    private static void quad(VertexConsumer buf,
//                             Vec3 sv, Vec3 vA,
//                             Vec3 ev, Vec3 vB,
//                             double off,
//                             float lightA, float lightB,
//                             float alphaA, float alphaB,
//                             Vec3 camPos) {
//
//        quad(buf, sv, vA, ev, vB, off, lightA, lightB, alphaA, alphaB, camPos, false);
//    }
//
//    private static void quad(VertexConsumer buf,
//                             Vec3 sv, Vec3 vA,
//                             Vec3 ev, Vec3 vB,
//                             double off,
//                             float lightA, float lightB,
//                             float alphaA, float alphaB,
//                             Vec3 camPos,
//                             boolean flip) {
//
//        if (flip) {
//            buf.addVertex((float) (ev.x - camPos.x - vB.x), (float) (ev.y - camPos.y + off), (float) (ev.z - camPos.z - vB.z)).setUv(1, 1).setColor(lightB, lightB, lightB, alphaB);
//            buf.addVertex((float) (ev.x - camPos.x + vB.x), (float) (ev.y - camPos.y + off), (float) (ev.z - camPos.z + vB.z)).setUv(1, 0).setColor(lightB, lightB, lightB, alphaB);
//            buf.addVertex((float) (sv.x - camPos.x + vA.x), (float) (sv.y - camPos.y + off), (float) (sv.z - camPos.z + vA.z)).setUv(0, 0).setColor(lightA, lightA, lightA, alphaA);
//            buf.addVertex((float) (sv.x - camPos.x - vA.x), (float) (sv.y - camPos.y + off), (float) (sv.z - camPos.z - vA.z)).setUv(0, 1).setColor(lightA, lightA, lightA, alphaA);
//        } else {
//            buf.addVertex((float) (sv.x - camPos.x + vA.x), (float) (sv.y - camPos.y + off), (float) (sv.z - camPos.z + vA.z)).setUv(0, 0).setColor(lightA, lightA, lightA, alphaA);
//            buf.addVertex((float) (sv.x - camPos.x - vA.x), (float) (sv.y - camPos.y + off), (float) (sv.z - camPos.z - vA.z)).setUv(0, 1).setColor(lightA, lightA, lightA, alphaA);
//            buf.addVertex((float) (ev.x - camPos.x - vB.x), (float) (ev.y - camPos.y + off), (float) (ev.z - camPos.z - vB.z)).setUv(1, 1).setColor(lightB, lightB, lightB, alphaB);
//            buf.addVertex((float) (ev.x - camPos.x + vB.x), (float) (ev.y - camPos.y + off), (float) (ev.z - camPos.z + vB.z)).setUv(1, 0).setColor(lightB, lightB, lightB, alphaB);
//        }
//    }
//
//    private static boolean isAccepted(Level level, BlockPos pos) {
//        BlockState ground = level.getBlockState(pos);
//        return ground.isFaceSturdy(level, pos, Direction.UP)
//                && level.getFluidState(pos.above()).isEmpty();
//    }
//
//    @SubscribeEvent
//    public static void onClientTick(ClientTickEvent.Post e) {
//        DEAD_CARS_LISTS.removeIf(list -> {
//            List<WheelParticleData> dead = Lists.newArrayList();
//            list.forEach(w -> w.tick(dead));
//            dead.forEach(list::remove);
//            return list.isEmpty();
//        });
//    }
//
//    public static void uploadList(VehicleEntity v) {
//        if (!v.level().isClientSide) return;
//        for (List<WheelParticleData> l : v.wheelDataList) DEAD_CARS_LISTS.add(0, l);
//    }
//}
