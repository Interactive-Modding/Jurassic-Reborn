package mod.reborn.server.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Lists;
import mod.reborn.server.api.GrindableItem;
import mod.reborn.server.api.Hybrid;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;

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
