package net.vit.jurassicreborn.client.model;

import org.joml.Vector3f;
import net.minecraft.world.level.block.state.BlockState;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFenceBaseBlock;
import net.vit.jurassicreborn.common.blocks.entities.fence.FenceType;


public final class ElectricFenceModels {

    /** chosen model path (without extension) + Y‑rotation */
    public record Variant(String modelPath, int yRot) {}

    public static Variant resolve(BlockState st, FenceType type) {
        boolean pole = st.getValue(ElectricFenceBaseBlock.POLE);
        int conn = st.getValue(ElectricFenceBaseBlock.CONNECTIONS);
        boolean n = st.getValue(ElectricFenceBaseBlock.NORTH);
        boolean s = st.getValue(ElectricFenceBaseBlock.SOUTH);
        boolean w = st.getValue(ElectricFenceBaseBlock.EAST);  // swapped for legacy
        boolean e = st.getValue(ElectricFenceBaseBlock.WEST);  // swapped for legacy

        // Only get facing degrees here:
        int facingRot = switch (st.getValue(ElectricFenceBaseBlock.FACING)) {
            case NORTH -> 0;
            case EAST  -> 90;
            case SOUTH -> 180;
            case WEST  -> 270;
            default    -> 0;
        };

        String base = type.getPath();

        // Now, always add +facingRot to each variant's rotation
        if (pole) return new Variant(base + "_pole", (0 + facingRot) % 360);
        if (conn == 0) return new Variant(base, (0 + facingRot) % 360);
        if (conn == 4) return new Variant(base + "_lower", (0 + facingRot) % 360);

        if ((conn == 1 && (n || s)) || (conn == 2 && n && s))
            return new Variant(base, (90 + facingRot) % 360);
        if ((conn == 1 && (e || w)) || (conn == 2 && e && w))
            return new Variant(base, (180 + facingRot) % 360);

        if (conn == 2) {
            if (n && e)  return new Variant(base + "_corner", (0 + facingRot) % 360);
            if (e && s)  return new Variant(base + "_corner", (90 + facingRot) % 360);
            if (s && w)  return new Variant(base + "_corner", (180 + facingRot) % 360);
            if (w && n)  return new Variant(base + "_corner", (270 + facingRot) % 360);
        }

        if (conn == 3) {
            if (!e) return new Variant(base + "_corner_lower", (180 + facingRot) % 360);
            if (!w) return new Variant(base + "_corner_lower", (0 + facingRot) % 360);
            if (!n) return new Variant(base + "_corner_lower", (90 + facingRot) % 360);
            if (!s) return new Variant(base + "_corner_lower", (270 + facingRot) % 360);
        }

        return new Variant(base, (0 + facingRot) % 360);
    }



    /** Convenience helper for PoseStack.mulPose. */
    public static Vector3f rotationVec(int deg) {
        return new Vector3f(0f, 1f, 0f);
    }

    private ElectricFenceModels() {}
}
