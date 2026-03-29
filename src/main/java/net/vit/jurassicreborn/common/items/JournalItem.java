package net.vit.jurassicreborn.common.items;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.screens.JournalGui;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class JournalItem extends Item {
    private final JournalType type;

    public JournalItem(JournalType type) {
        super(new Item.Properties().stacksTo(1));
        this.type = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            openScreen();
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @OnlyIn(Dist.CLIENT)
    private void openScreen() {
        Minecraft.getInstance().setScreen(new JournalGui(type));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {tooltip.add(Component.translatable("journal." + type.getIdentifier().getPath() + ".name"));
    }

    public enum JournalType {
        CHEF_ALEJANDRO(0, ResourceLocation.parse(JurassicReborn.MODID + ":" + "chef_alejandro")),
        DENNIS_NEDRY   (1, ResourceLocation.parse(JurassicReborn.MODID + ":" + "dennis_nedry")),
        DR_GERRY_HARDING(2, ResourceLocation.parse(JurassicReborn.MODID + ":" + "dr_gerry_harding")),
        DR_HENRY_WU    (3, ResourceLocation.parse(JurassicReborn.MODID + ":" + "dr_henry_wu")),
        DR_LAURA_SORKIN(4, ResourceLocation.parse(JurassicReborn.MODID + ":" + "dr_laura_sorkin")),
        ED_REGIS       (5, ResourceLocation.parse(JurassicReborn.MODID + ":" + "ed_regis")),
        JOHN_HAMMOND   (6, ResourceLocation.parse(JurassicReborn.MODID + ":" + "john_hammond")),
        RAY_ARNOLD     (7, ResourceLocation.parse(JurassicReborn.MODID + ":" + "ray_arnold")),
        ROBERT_MULDOON (8, ResourceLocation.parse(JurassicReborn.MODID + ":" + "robert_muldoon"));

        public static final JournalType[] VALUES = values();

        private final int metadata;
        private final ResourceLocation identifier;
        private final ResourceLocation location;
        @OnlyIn(Dist.CLIENT)
        private Content content;

        JournalType(int metadata, ResourceLocation identifier) {
            this.metadata   = metadata;
            this.identifier = identifier;
            this.location   = ResourceLocation.parse(identifier.getNamespace() + ":" + "journal_entries/" + identifier.getPath() + ".json");
        }

        public int getMetadata() {
            return metadata;
        }

        public ResourceLocation getIdentifier() {
            return identifier;
        }

        @OnlyIn(Dist.CLIENT)
        public Content getContent() {
            if (content == null) {
                try (InputStream in = Minecraft.getInstance()
                        .getResourceManager()
                        .getResource(location)
                        .get()
                        .open()) {
                    content = new Gson().fromJson(new InputStreamReader(in), Content.class);
                } catch (IOException e) {
                    String[][] fallback = { { "Failed to load journal entries" } };
                    content = new Content("error", fallback);
                }
            }
            return content;
        }

        public static JournalType get(int meta) {
            if (meta >= 0 && meta < VALUES.length) return VALUES[meta];
            return CHEF_ALEJANDRO;
        }
    }

    public static class Content {
        private final String identifier;
        private final String[][] entries;
        public Content(String identifier, String[][] entries) {
            this.identifier = identifier;
            this.entries    = entries;
        }
        public String getIdentifier() { return identifier; }
        public String[][] getEntries() { return entries; }
    }
}
