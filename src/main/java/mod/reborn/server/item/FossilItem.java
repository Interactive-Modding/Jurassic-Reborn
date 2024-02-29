package mod.reborn.server.item;

import java.util.*;

import com.google.common.collect.Lists;
import mod.reborn.server.api.GrindableItem;
import mod.reborn.server.api.Hybrid;
import mod.reborn.server.dinosaur.*;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import mod.reborn.server.entity.EntityHandler;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.item.ItemBlock;
import mod.reborn.server.entity.dinosaur.TyrannosaurusEntity;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.entity.DisplayBlockEntity;
import mod.reborn.server.block.entity.SkullDisplayEntity;
import com.sun.javafx.geom.Vec2d;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;




import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.util.LangUtils;

import javax.vecmath.Vector2d;

public class FossilItem extends Item implements GrindableItem {
    public static Map<String, List<Dinosaur>> fossilDinosaurs = new HashMap<>();
    public static Map<String, List<Dinosaur>> freshFossilDinosaurs = new HashMap<>();
    private String type;
    private boolean fresh;

    public FossilItem(String type, boolean fresh) {
        this.type = type.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        this.fresh = fresh;

        this.setHasSubtypes(true);

        this.setCreativeTab(TabHandler.FOSSILS);
    }

    public static void init() {
        for (boolean fresh : new boolean[] { true, false }) {
            for (Dinosaur dinosaur : EntityHandler.getDinosaurs().values()) {
                if(!fresh && dinosaur instanceof Hybrid) {
                    continue;
                }
                Map<String, List<Dinosaur>> map = fresh ? freshFossilDinosaurs : fossilDinosaurs;
                String[] boneTypes = dinosaur.getBones();
                for (String boneType : boneTypes) {
                    List<Dinosaur> dinosaursWithType = map.get(boneType);

                    if (dinosaursWithType == null) {
                        dinosaursWithType = new ArrayList<>();
                    }
                    if(!dinosaur.getName().equals("")) {
                        dinosaursWithType.add(dinosaur);
                    }
                    map.put(boneType, dinosaursWithType);
                }
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Dinosaur dinosaur = this.getDinosaur(stack);

        if (dinosaur != null) {
            return LangUtils.translate(this.getUnlocalizedName() + ".name").replace("{dino}", LangUtils.getDinoName(dinosaur));
        }

        return super.getItemStackDisplayName(stack);
    }

    public Dinosaur getDinosaur(ItemStack stack) {
        return EntityHandler.getDinosaurById(stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subtypes) {
        List<Dinosaur> dinosaurs = new ArrayList<>(EntityHandler.getRegisteredDinosaurs());

        Collections.sort(dinosaurs);

        List<Dinosaur> dinosaursForType = this.getMap().get(this.type);
        if(this.isInCreativeTab(tab))
        for (Dinosaur dinosaur : dinosaurs) {
            if (dinosaursForType.contains(dinosaur) && !(!this.fresh && dinosaur instanceof Hybrid)) {
                subtypes.add(new ItemStack(this, 1, EntityHandler.getDinosaurId(dinosaur)));
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> lore, ITooltipFlag flagIn) {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt != null && nbt.hasKey("Genetics") && nbt.hasKey("DNAQuality")) {
            int quality = nbt.getInteger("DNAQuality");

            TextFormatting colour;

            if (quality > 75) {
                colour = TextFormatting.GREEN;
            } else if (quality > 50) {
                colour = TextFormatting.YELLOW;
            } else if (quality > 25) {
                colour = TextFormatting.GOLD;
            } else {
                colour = TextFormatting.RED;
            }


            lore.add(colour + LangUtils.translate(LangUtils.LORE.get("dna_quality")).replace("{quality}", LangUtils.getFormattedQuality(quality)));
            lore.add(TextFormatting.BLUE + LangUtils.translate(LangUtils.LORE.get("genetic_code")).replace("{code}", LangUtils.getFormattedGenetics(nbt.getString("Genetics"))));
        }

        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TyrannosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == AchillobatorDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == VelociraptorDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == VelociraptorSquad.VelociraptorEchoDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == VelociraptorSquad.VelociraptorBlueDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == VelociraptorSquad.VelociraptorDeltaDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == VelociraptorSquad.VelociraptorCharlieDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == AllosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == BaryonyxDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == AnkylodocusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == AnkylosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ArsinoitheriumDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ApatosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == BrachiosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CamarasaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CarcharodontosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CeratosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ChasmosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CarnotaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CorythosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CearadactylusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DreadnoughtusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DiplodocusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DeinotheriumDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DunkleosteusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == EdmontosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ElasmotheriumDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == HerrerasaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == GiganotosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == IndominusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == HyaenodonDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == IndoraptorDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DimetrodonDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == LudodactylusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MajungasaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == LambeosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MawsoniaDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MamenchisaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MammothDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == PachycephalosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DilophosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MetriacanthosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MegatheriumDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MosasaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ProtoceratopsDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ParasaurolophusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == PostosuchusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == PteranodonDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == SuchomimusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == StegosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == SmilodonDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == StyracosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == SpinosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == SpinoraptorDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == SinoceratopsDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == RaphusrexDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == RugopsDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == QuetzalDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TherizinosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TylosaurusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TropeognathusDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TriceratopsDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }
        if(((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TitanisDinosaur.class) {
            lore.add(TextFormatting.GOLD + LangUtils.translate("pose.name") + ": " + LangUtils.getStandType(getHasStand(stack)));
            lore.add(TextFormatting.WHITE + LangUtils.translate("lore.change_variant.name"));
        }


    }

    @Override
    public boolean isGrindable(ItemStack stack) {
        return true;
    }

    public boolean isFresh() {
        return this.fresh;
    }

    public String getBoneType(){
        return type;
    }
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState blockstate = world.getBlockState(pos);
        Block worldBlock = blockstate.getBlock();

        if (worldBlock.isReplaceable(world, pos))
        {
            side = EnumFacing.UP;
            pos = pos.down();
        }
        worldBlock = world.getBlockState(pos).getBlock();

        if (!worldBlock.isReplaceable(world, pos))
        {
            if (!world.getBlockState(pos).getMaterial().isSolid() || !world.isSideSolid(pos, side, true))
            {
                return EnumActionResult.FAIL;
            }

            pos = pos.offset(side);
        }

        ItemStack stack = player.getHeldItem(hand);
        Block block = BlockHandler.SKULL_DISPLAY;
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TyrannosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == VelociraptorDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == VelociraptorSquad.VelociraptorEchoDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MajungasaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == LambeosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == LudodactylusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == VelociraptorSquad.VelociraptorBlueDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == VelociraptorSquad.VelociraptorCharlieDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == VelociraptorSquad.VelociraptorDeltaDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ArsinoitheriumDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TylosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TropeognathusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TriceratopsDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TitanisDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TherizinosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == BaryonyxDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == AllosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == AchillobatorDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == AnkylodocusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == IndoraptorDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == AnkylosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ApatosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == BrachiosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CamarasaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CarcharodontosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CarnotaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CeratosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CearadactylusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CorythosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ChasmosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DreadnoughtusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MammothDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MawsoniaDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MamenchisaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DiplodocusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DeinotheriumDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }

        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DunkleosteusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == EdmontosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ElasmotheriumDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ParasaurolophusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ProtoceratopsDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == PostosuchusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == PteranodonDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == HerrerasaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == GiganotosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == IndominusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == SuchomimusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == StyracosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == StegosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == SmilodonDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == SpinoraptorDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == SpinosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == SinoceratopsDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == RaphusrexDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == RugopsDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == QuetzalDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DimetrodonDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MegatheriumDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DilophosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == PachycephalosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MosasaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MetriacanthosaurusDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack) && world.mayPlace(block, pos, false, side, (Entity) null) && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == HyaenodonDinosaur.class) {

            if (side == EnumFacing.DOWN)
            {
                return EnumActionResult.FAIL;
            }

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState blockstatePlacement = block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, /*meta*/ 0, player, hand);
                if (!world.setBlockState(pos, blockstatePlacement, 11))
                    return EnumActionResult.FAIL;

                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == block)
                {
                    ItemBlock.setTileEntityNBT(world, player, pos, stack);
                    block.onBlockPlacedBy(world, pos, state, player, stack);

                    if (player instanceof EntityPlayerMP)
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                world.playSound(null, pos, SoundType.STONE.getPlaceSound(), SoundCategory.BLOCKS, (SoundType.STONE.getVolume() + 1.0F) / 2.0F, SoundType.STONE.getPitch() * 0.8F);
                SkullDisplayEntity tile = (SkullDisplayEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setModel(stack.getItemDamage(), !this.isFresh(), getHasStand(stack));
                    EnumFacing.Axis axis = side.getAxis();
                    if (axis == EnumFacing.Axis.Y) {
                        tile.setAngle(angleToPlayer(pos, new Vector2d(player.posX, player.posZ)));
                    }else if(axis == EnumFacing.Axis.X) {
                        tile.setAngle((short) side.getHorizontalAngle());
                    }else if(axis == EnumFacing.Axis.Z) {
                        tile.setAngle((short) (180 + side.getHorizontalAngle()));
                    }
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    stack.shrink(1);

                }

            }
        }
        return EnumActionResult.SUCCESS;
    }

    private static short angleToPlayer(BlockPos block, Vector2d player) {
        return (short) (90 - Math.toDegrees(Math.atan2(((double) block.getZ() + 0.5 - player.y), ((double) block.getX() + 0.5 - player.x))));
    }

    public static boolean getHasStand(ItemStack stack) {

        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Type")) {
            return stack.getTagCompound().getBoolean("Type");
        }else {
            return true;
        }
    }

    public static void setHasStand(ItemStack stack, boolean hasStand) {
        NBTTagCompound nbt = stack.getTagCompound();
        if(nbt == null) {
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
        }
        nbt.setBoolean("Type", hasStand);
    }

    public static boolean changeStandType(ItemStack stack) {

        boolean newType = !getHasStand(stack);

        setHasStand(stack, newType);
        return newType;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TyrannosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == AchillobatorDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == IndominusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == VelociraptorDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == VelociraptorSquad.VelociraptorDeltaDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == VelociraptorSquad.VelociraptorBlueDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == VelociraptorSquad.VelociraptorCharlieDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == VelociraptorSquad.VelociraptorEchoDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == BaryonyxDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ArsinoitheriumDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == AllosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == AnkylodocusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == AnkylosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ApatosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == BrachiosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CamarasaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CarcharodontosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CeratosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CarnotaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CorythosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == CearadactylusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ChasmosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DeinotheriumDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DreadnoughtusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DiplodocusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DeinotheriumDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == HerrerasaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == EdmontosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ElasmotheriumDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == GiganotosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == HyaenodonDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DimetrodonDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == IndoraptorDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == LudodactylusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == LambeosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MajungasaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MawsoniaDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MamenchisaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MetriacanthosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == DilophosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == PachycephalosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MosasaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MegatheriumDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == MammothDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ParasaurolophusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == PostosuchusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == ProtoceratopsDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == RugopsDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == RaphusrexDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == SinoceratopsDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == SpinosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == SpinoraptorDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == QuetzalDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == SmilodonDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == StegosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == StyracosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == SuchomimusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == PteranodonDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TropeognathusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TherizinosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TylosaurusDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TitanisDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }
        if (player.isSneaking() && ((FossilItem) stack.getItem()).getBoneType().equals("skull") && ((FossilItem) stack.getItem()).getDinosaur(stack).getClass() == TriceratopsDinosaur.class) {
            boolean oldType = getHasStand(stack);
            boolean type = changeStandType(stack);
            if (type != oldType && world.isRemote) {
                TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.STAND_CHANGE.get("type")).replace("{mode}", LangUtils.getStandType(type)));
                change.getStyle().setColor(TextFormatting.YELLOW);
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
            }
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
    
    @Override
    public ItemStack getGroundItem(ItemStack stack, Random random) {
        NBTTagCompound tag = stack.getTagCompound();

        int outputType = random.nextInt(6);

        if (outputType == 5 || this.fresh) {
            ItemStack output = new ItemStack(ItemHandler.SOFT_TISSUE, 1, stack.getItemDamage());
            output.setTagCompound(tag);
            return output;
        } else if (outputType < 3) {
            return new ItemStack(Items.DYE, 1, 15);
        }

        return new ItemStack(Items.FLINT);
    }


    @Override
    public List<ItemStack> getJEIRecipeTypes() {
        List<ItemStack> list = Lists.newArrayList();
        this.getMap().get(this.type).forEach(dino -> list.add(new ItemStack(this, 1, EntityHandler.getDinosaurId(dino))));
        return list;
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        float single = 100F/6F;
        NBTTagCompound tag = inputItem.getTagCompound();
        ItemStack output = new ItemStack(ItemHandler.SOFT_TISSUE, 1, inputItem.getItemDamage());
        output.setTagCompound(tag);
        if(this.fresh) {
            return Lists.newArrayList(Pair.of(100F, output));
        }
        return Lists.newArrayList(Pair.of(single, output), Pair.of(50f, new ItemStack(Items.DYE, 1, 15)), Pair.of(single*2f, new ItemStack(Items.FLINT)));
    }

    public Map<String, List<Dinosaur>> getMap() {
        return this.fresh ? freshFossilDinosaurs : fossilDinosaurs;
    }
}
