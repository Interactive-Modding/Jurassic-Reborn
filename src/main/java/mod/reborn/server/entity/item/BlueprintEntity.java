package mod.reborn.server.entity.item;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import mod.reborn.RebornMod;
import mod.reborn.server.item.ItemHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Locale;

public class BlueprintEntity extends EntityHanging implements IEntityAdditionalSpawnData {
    private static final Predicate<Entity> IS_Blueprint = entity -> entity instanceof BlueprintEntity;

    public Type type;

    public BlueprintEntity(World world) {
        super(world);
    }

    public BlueprintEntity(World world, BlockPos pos, EnumFacing side) {
        super(world, pos);

        List<Type> possibleTypes = Lists.<Type>newArrayList();

        for (Type type : Type.values()) {
            this.type = type;
            this.updateFacingWithBoundingBox(side);

            if (this.onValidSurface()) {
                possibleTypes.add(type);
            }
        }

        if (!possibleTypes.isEmpty()) {
            this.type = possibleTypes.get(this.rand.nextInt(possibleTypes.size()));
        }

        this.updateFacingWithBoundingBox(side);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setByte("Type", (byte) this.type.ordinal());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.type = Type.values()[compound.getByte("Type")];

        super.readEntityFromNBT(compound);
    }

    @Override
    public int getWidthPixels() {
        return this.type.sizeX;
    }

    @Override
    public int getHeightPixels() {
        return this.type.sizeY;
    }

    @Override
    public void onBroken(Entity entity) {
        if (this.world.getGameRules().getBoolean("doTileDrops")) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;

                if (player.capabilities.isCreativeMode) {
                    return;
                }
            }

            this.entityDropItem(new ItemStack(ItemHandler.BLUEPRINT), 0.0F);
        }
    }

    @Override
    public void playPlaceSound() {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    }

    @Override
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
        BlockPos positionOffset = new BlockPos(x - this.posX, y - this.posY, z - this.posZ);
        BlockPos newPosition = this.hangingPosition.add(positionOffset);
        this.setPosition(newPosition.getX(), newPosition.getY(), newPosition.getZ());
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeByte(this.type.ordinal());
        buffer.writeLong(this.hangingPosition.toLong());
        buffer.writeByte(this.facingDirection.getHorizontalIndex());
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        this.type = Type.values()[buf.readByte()];
        this.hangingPosition = BlockPos.fromLong(buf.readLong());
        this.updateFacingWithBoundingBox(EnumFacing.getHorizontal(buf.readByte()));
    }

    @Override
    public boolean onValidSurface() {
        if (!this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()) {
            return false;
        }

        int width = this.getWidthPixels() / 16;
        int height = this.getHeightPixels() / 16;

        EnumFacing facing = this.facingDirection.rotateYCCW();

        BlockPos pos = this.hangingPosition.offset(this.facingDirection.getOpposite()).offset(facing, -(width / 2) + 1);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                BlockPos partPos = pos.offset(facing, x).down(y);
                IBlockState state = this.world.getBlockState(partPos);

                if (!(state.isSideSolid(this.world, partPos, this.facingDirection) && state.getMaterial().isSolid())) {
                    return false;
                }
            }
        }

        return this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), IS_Blueprint).isEmpty();
    }

    @Override
    protected void updateBoundingBox() {
        if (this.facingDirection != null) {
            double x = this.hangingPosition.getX() + 0.5D;
            double y = this.hangingPosition.getY() + 0.5D;
            double z = this.hangingPosition.getZ() + 0.5D;
            double offsetXZ = this.getWidthPixels() % 32 == 0 ? 0.5D : 0.0D ;
            double offsetY = this.getHeightPixels() % 32 == 0 ? 0.5D : 0.0D;
            x -= this.facingDirection.getFrontOffsetX() * 0.46875D;
            z -= this.facingDirection.getFrontOffsetZ() * 0.46875D;
            y += offsetY;
            EnumFacing facing = this.facingDirection.rotateYCCW();
            x += offsetXZ * facing.getFrontOffsetX();
            z += offsetXZ * facing.getFrontOffsetZ();
            this.posX = x;
            this.posY = y;
            this.posZ = z;
            double sizeX = this.getWidthPixels();
            double sizeY = this.getHeightPixels();
            double sizeZ = this.getWidthPixels();

            if (this.facingDirection.getAxis() == EnumFacing.Axis.Z) {
                sizeZ = 1.0D;
            } else {
                sizeX = 1.0D;
            }

            sizeX /= 32.0D;
            sizeY /= 16.0D;
            sizeZ /= 32.0D;

            this.setEntityBoundingBox(new AxisAlignedBB(x - sizeX, y, z - sizeZ, x + sizeX, y - sizeY, z + sizeZ));
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(ItemHandler.BLUEPRINT, 1, this.type.ordinal());
    }

    public enum Type {
        TYRANNOSAURUS(1920,1200),
        ANKYLODOCUS(1074,671),
        ANKYLOSAURUS(1074,671),
        APATOSAURUS(1074,671),
        ARSINOITHERIUM(1206,676),
        BARYONYX(1206,672),
        BRACHIOSAURUS(1202,676),
        CAMARASAURUS(1074,671),
        CARCHARODONTOSAURUS(1206,676),
        CARNOTAURUS(1920,1080),
        CERATOSAURUS(1074,671),
        CHASMOSAURUS(1074,671),
        COMPSOGNATHUS(1205,676),
        DEINOTHERIUM(1206,676),
        DIMETRODON(1074,671),
        DIPLODOCUS(1074,671),
        DREADNOUGHTUS(1074,671),
        ELASMOTHERIUM(1196,676),
        HERRERASAURUS(1920,1200),
        INDORAPTOR(1074,671),
        LEPTICTIDIUM(1201,676),
        MAMENCHISAURUS(1080,607),
        MEGATHERIUM(1074,671),
        MOSASAURUS(1071,676),
        OVIRAPTOR(1920,1080),
        PARASAUROLOPHUS(1395,783),
        QUETZALCOATLUS(1074,671),
        RAPHUSREX(1074,671),
        RUGOPS(1074,671),
        SINOCERATOPS(1074,671),
        SMILODON(1074,671),
        SPINORAPTOR(1074,671),
        SPINOSAURUS(1202,676),
        STYRACOSAURUS(1074,671),
        TITANIS(1074,671),
        TRICERATOPS(1074,671);




        public final int sizeX;
        public final int sizeY;
        public final ResourceLocation texture;

        Type(int xSize, int ySize) {
            this.sizeX = (96);
            this.sizeY = (64);
            this.texture = new ResourceLocation(RebornMod.MODID, "textures/blueprints/" + this.name().toLowerCase(Locale.ENGLISH) + ".png");
        }
    }
}
