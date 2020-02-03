package mod.reborn.server.block.entity;

import com.google.common.primitives.Ints;
import io.netty.buffer.ByteBuf;
import mod.reborn.RebornMod;
import mod.reborn.server.container.CultivateContainer;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.food.FoodNutrients;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.item.SyringeItem;
import mod.reborn.server.message.TileEntityFieldsMessage;
import mod.reborn.server.proxy.ServerProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class CultivatorBlockEntity extends MachineBaseBlockEntity implements TemperatureControl, ISyncable {
    private static final int[] INPUTS = new int[] {0, 1, 2, 3};
    private static final int[] OUTPUTS = new int[] {0, 3};
    public static final int MAX_NUTRIENTS = 3000;
    private NonNullList<ItemStack> slots = NonNullList.withSize(5, ItemStack.EMPTY);
    private int waterLevel;
    private int lipids;
    private int proximates;
    private int minerals;
    private int vitamins;
    private int temperature;
    private boolean prevActiveState = false;

    private DinosaurEntity dinosaurEntity; //Used for rendering entities

    @Override
    protected int getProcess(int slot) {
        return 0;
    }

    @Override
    protected boolean canProcess(int process) {
    	ItemStack itemstack = this.slots.get(0);
        if (itemstack.getItem() == ItemHandler.SYRINGE && this.waterLevel == 2) {
            Dinosaur dino = EntityHandler.getDinosaurById(itemstack.getItemDamage());
            if (dino == null) {
                return false;
            }
            Dinosaur meta = dino;
            if (meta.getBirthType() == Dinosaur.BirthType.LIVE_BIRTH) {
                return this.lipids >= meta.getLipids() && this.minerals >= meta.getMinerals() && this.proximates >= meta.getProximates() && this.vitamins >= meta.getVitamins();
            }
        }
        return false;
    }
    
    @Override
	public void packetDataHandler(ByteBuf fields)
	{
		super.packetDataHandler(fields);
		
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			setField(0, fields.readInt());
			//This assumes that every field is of type integer!
			for(int i = 2; i < getFieldCount(); i++){
				setField(i, fields.readInt());
			}
			
		}
		
	}

    @Override
    protected void processItem(int process) {
        ItemStack syringe = this.slots.get(0);
        Dinosaur dinosaur = EntityHandler.getDinosaurById(syringe.getItemDamage());

        if (dinosaur != null) {
        	Dinosaur metadata = dinosaur;
            this.lipids -= metadata.getLipids();
            this.minerals -= metadata.getMinerals();
            this.vitamins -= metadata.getVitamins();
            this.proximates -= metadata.getProximates();
            this.waterLevel--;

            ItemStack hatchedEgg = new ItemStack(ItemHandler.HATCHED_EGG, 1, syringe.getItemDamage());

            NBTTagCompound compound = new NBTTagCompound();
            compound.setBoolean("Gender", this.temperature > 50);

            NBTTagCompound syringeTag = syringe.getTagCompound();
            if (syringeTag != null) {
                compound.setString("Genetics", syringeTag.getString("Genetics"));
                compound.setInteger("DNAQuality", syringeTag.getInteger("DNAQuality"));
            }

            hatchedEgg.setTagCompound(compound);
            this.slots.set(0, hatchedEgg);
        }
    }

    @Override
    public void update() {
        super.update();
        if (!this.world.isRemote) {

        	boolean sync = false;
            if (this.waterLevel < 2 && this.slots.get(2).getItem() == Items.WATER_BUCKET) {
                if (this.slots.get(3).getCount() < 16) {
                    this.slots.get(2).shrink(1);

                    this.waterLevel++;

                    ItemStack stack = this.slots.get(3);
                    if (stack.getItem() == Items.BUCKET) {
                        stack.grow(1);
                    } else {
                        this.slots.set(3, new ItemStack(Items.BUCKET));
                    }

                    sync = true;
                }
            }

            ItemStack stack = this.slots.get(1);
            if (!stack.isEmpty()) {
                if ((this.proximates < MAX_NUTRIENTS) || (this.minerals < MAX_NUTRIENTS) || (this.vitamins < MAX_NUTRIENTS) || (this.lipids < MAX_NUTRIENTS)) {
                    this.consumeNutrients();
                    sync = true;

                }
            }
            
            boolean active = this.isProcessing(0);
            boolean syncActive = false;
            if(active != prevActiveState) {
            	prevActiveState = active;
            	syncActive = true;
            	
            	
            }
            if (sync) {
                this.markDirty();
            }
            if(syncActive || sync)
            	RebornMod.NETWORK_WRAPPER.sendToAllTracking(new TileEntityFieldsMessage(getSyncFields(NonNullList.create()), this), new TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 5));
            
            if(syncActive) {
            	
            	for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double)((float)this.pos.getX() - 5.0F), (double)((float)this.pos.getY() - 5.0F), (double)((float)this.pos.getZ() - 5.0F), (double)((float)(this.pos.getX() + 1) + 5.0F), (double)((float)(this.pos.getY() + 1) + 5.0F), (double)((float)(this.pos.getZ() + 1) + 5.0F))))
                {
                    if (entityplayer.openContainer instanceof CultivateContainer)
                    {
                        if(this.isProcessing(0)) {
                        	entityplayer.openGui(RebornMod.INSTANCE, ServerProxy.GUI_CULTIVATOR_ID, this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ());
                        }
                    }
                    
                }
            }
        }

    }

    private void consumeNutrients() {
        ItemStack foodStack = this.slots.get(1);
        FoodNutrients nutrients = FoodNutrients.get(foodStack.getItem());

        if (nutrients != null) {
            if (foodStack.getItem() instanceof ItemBucketMilk) {
            	this.slots.set(1, new ItemStack(Items.BUCKET));
            } else {
                foodStack.shrink(1);
                if (foodStack.getCount() <= 0) {
                    foodStack.isEmpty();
                }
            }

            Random random = this.world.rand;
            if (this.proximates < MAX_NUTRIENTS) {
                this.proximates = (short) (this.proximates + (800 + random.nextInt(201)) * nutrients.getProximate());
                if (this.proximates > MAX_NUTRIENTS) {
                    this.proximates = (short) MAX_NUTRIENTS;
                }
            }

            if (this.minerals < MAX_NUTRIENTS) {
                this.minerals = (short) (this.minerals + (900 + random.nextInt(101)) * nutrients.getMinerals());
                if (this.minerals > MAX_NUTRIENTS) {
                    this.minerals = (short) MAX_NUTRIENTS;
                }
            }

            if (this.vitamins < MAX_NUTRIENTS) {
                this.vitamins = (short) (this.vitamins + (900 + random.nextInt(101)) * nutrients.getVitamins());
                if (this.vitamins > MAX_NUTRIENTS) {
                    this.vitamins = (short) MAX_NUTRIENTS;
                }
            }

            if (this.lipids < MAX_NUTRIENTS) {
                this.lipids = (short) (this.lipids + (980 + random.nextInt(101)) * nutrients.getLipids());
                if (this.lipids > MAX_NUTRIENTS) {
                    this.lipids = (short) MAX_NUTRIENTS;
                }
            }
        }
    }

    @Override
    protected void onSlotUpdate() {
        super.onSlotUpdate();
        if(this.getStackInSlot(0).isEmpty()) {
            this.dinosaurEntity = null;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.waterLevel = compound.getShort("WaterLevel");
        this.lipids = compound.getInteger("Lipids");
        this.minerals = compound.getInteger("Minerals");
        this.vitamins = compound.getInteger("Vitamins");
        this.proximates = compound.getInteger("Proximates");
        this.temperature = compound.getInteger("Temperature");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        compound.setShort("WaterLevel", (short) this.waterLevel);
        compound.setInteger("Lipids", this.lipids);
        compound.setInteger("Minerals", this.minerals);
        compound.setInteger("Vitamins", this.vitamins);
        compound.setInteger("Proximates", this.proximates);
        compound.setInteger("Temperature", this.temperature);
        ItemStackHelper.saveAllItems(compound, this.slots);
        return compound;
    }

    @Override
    protected int getMainOutput(int process) {
        return 4;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack) {
        return 2000;
    }

    @Override
    protected int getProcessCount() {
        return 1;
    }

    @Override
    protected int[] getInputs() {
        return INPUTS;
    }

    @Override
    protected int[] getInputs(int process) {
        return INPUTS;
    }

    @Override
    protected int[] getOutputs() {
        return OUTPUTS;
    }

    public DinosaurEntity getDinosaurEntity() {
        if(this.getStackInSlot(0).isEmpty()){
            return null;
        }
        return dinosaurEntity == null ? createEntity() : dinosaurEntity;
    }
    
    @Override
	public NonNullList getSyncFields(NonNullList fields)
	{
		super.getSyncFields(fields);
		
		fields.add(this.getField(0));
		
		for(int i = 2; i < getFieldCount(); i++){
			fields.add(this.getField(i));
		}
		
		return fields;
	}

    private DinosaurEntity createEntity() {
        try {
            this.dinosaurEntity = EntityHandler.getDinosaurById(this.getStackInSlot(0).getMetadata()).getDinosaurClass().getDeclaredConstructor(World.class).newInstance(this.world);
            this.dinosaurEntity.setMale(this.temperature > 50);
            this.dinosaurEntity.setFullyGrown();
            this.dinosaurEntity.getAttributes().setScaleModifier(1f);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to create dinosaur entity", e);
        }
        return dinosaurEntity;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new CultivateContainer(playerInventory, this);
    }

	@Override
    public String getGuiID() {
        return RebornMod.MODID + ":cultivator";
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.cultivator";
    }

    public int getWaterLevel() {
        return this.waterLevel;
    }

    public int getMaxNutrients() {
        return MAX_NUTRIENTS;
    }

    public int getProximates() {
        return this.proximates;
    }

    public int getMinerals() {
        return this.minerals;
    }

    public int getVitamins() {
        return this.vitamins;
    }

    public int getLipids() {
        return this.lipids;
    }

    @Override
    public int getField(int id) {
        int processCount = this.getProcessCount();

        if (id < processCount) {
            return this.processTime[id];
        } else if (id < processCount * 2) {
            return this.totalProcessTime[id - processCount];
        } else {
            int type = id - (processCount * 2);
            switch (type) {
                case 0:
                    return this.waterLevel;
                case 1:
                    return this.proximates;
                case 2:
                    return this.minerals;
                case 3:
                    return this.vitamins;
                case 4:
                    return this.lipids;
                case 5:
                    return this.temperature;
            }
        }

        return 0;
    }

    @Override
    public void setField(int id, int value) {
        int processCount = this.getProcessCount();
        if (id < processCount) {
            this.processTime[id] = value;
        } else if (id < processCount * 2) {
            this.totalProcessTime[id - processCount] = value;
        } else {
            int type = id - (processCount * 2);
            switch (type) {
                case 0:
                    this.waterLevel = value;
                    break;
                case 1:
                    this.proximates = value;
                    break;
                case 2:
                    this.minerals = value;
                    break;
                case 3:
                    this.vitamins = value;
                    break;
                case 4:
                    this.lipids = value;
                    break;
                case 5:
                    this.temperature = value;
                    break;
            }
        }
    }

    @Override
    public int getFieldCount() {
        return this.getProcessCount() * 2 + 6;
    }

    public Dinosaur getDinosaur() {
    	ItemStack stack = this.slots.get(0);
        if (!stack.isEmpty()) {
            return EntityHandler.getDinosaurById(stack.getItemDamage());
        }

        return null;
    }

    @Override
    public void setTemperature(int index, int value) {
        this.temperature = value;
    }

    @Override
    public int getTemperature(int index) {
        return this.temperature;
    }

    @Override
    public int getTemperatureCount() {
        return 1;
    }

	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side) {
		if(!this.isProcessing(0)) {
		if(slotID == 0) {
			ItemStack stackInSlot = this.getStackInSlot(slotID);
			
			if (stackInSlot != null && stackInSlot.getItem() == ItemHandler.HATCHED_EGG) {
				return true;
			}else {
				return false;
			}
		}
		return true;
		}
		return false;
		
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
		if(!this.handler.isUp && !this.isProcessing(0)) {
		if (Ints.asList(INPUTS).contains(slotID)) {
			if ((slotID == 0 && itemstack != null && this.getStackInSlot(slotID).getCount() == 0 && itemstack.getItem() instanceof SyringeItem && SyringeItem.getDinosaur(itemstack).getBirthType() == Dinosaur.BirthType.LIVE_BIRTH)
					|| (slotID == 1 && itemstack != null && FoodNutrients.NUTRIENTS.containsKey(itemstack.getItem()))
					|| (slotID == 2 && itemstack != null && CleaningStationBlockEntity.isItemFuel(itemstack))) {

				return true;
			}
		}
		}
		return false;
	}

	@Override
	protected NonNullList<ItemStack> getSlots() {
//        NonNullList<ItemStack> slots = NonNullList.withSize(5, ItemStack.EMPTY);
		return slots;
	}
	
	@Override
	protected void setSlots(NonNullList<ItemStack> slot) {
//		ItemStack stack = this.slots.get(1);
//		stack.grow(slot.size());
		this.slots = slot;
	}
	
	@Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.writeToNBT(new NBTTagCompound()));
    }
	
	private CultivatorCapability handler = new CultivatorCapability(this);

	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {

		if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if(!(facing == EnumFacing.DOWN)){
                return (T) handler;
            }
		return super.getCapability(capability, facing);
	}

	public IItemHandler getCapabilityHandler() {
		return super.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
	}

}
