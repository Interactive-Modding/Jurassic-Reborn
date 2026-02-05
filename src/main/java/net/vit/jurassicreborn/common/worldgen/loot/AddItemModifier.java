// AddItemModifier.java
package net.vit.jurassicreborn.common.worldgen.loot;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddItemModifier extends LootModifier {

    /* ---------- Fields ---------- */

    private final Item item;
    private final TagKey<Item> tag;
    private final float chance;
    private final int count;

    /* ---------- Ctor ---------- */

    public AddItemModifier(LootItemCondition[] conditions, Item item,
                           TagKey<Item> tag, float chance, int count) {
        super(conditions);
        this.item   = item;
        this.tag    = tag;
        this.chance = chance;
        this.count  = count;
    }

    /* ---------- Logic ---------- */

    @Override
    protected @NotNull List<ItemStack> doApply(List<ItemStack> generated,
                                               LootContext ctx) {

        if (ctx.getRandom().nextFloat() > chance)           // roll the dice
            return generated;

        // Pick the actual item
        Item toAdd = this.item;
        if (toAdd == null && tag != null) {
            List<Item> pool = ForgeRegistries.ITEMS.tags().getTag(tag).stream().toList();
            if (!pool.isEmpty())
                toAdd = pool.get(ctx.getRandom().nextInt(pool.size()));
        }
        if (toAdd != null)
            generated.add(new ItemStack(toAdd, count));

        return generated;
    }

    public static class Serializer extends GlobalLootModifierSerializer<AddItemModifier> {

        @Override
        public AddItemModifier read(ResourceLocation location, JsonObject json, LootItemCondition[] conditions) {
            Item item = null;
            if (json.has("item")) {
                ResourceLocation itemId = new ResourceLocation(GsonHelper.getAsString(json, "item"));
                item = ForgeRegistries.ITEMS.getValue(itemId);
            }

            TagKey<Item> tag = null;
            if (json.has("tag")) {
                ResourceLocation tagId = new ResourceLocation(GsonHelper.getAsString(json, "tag"));
                tag = TagKey.create(Registry.ITEM_REGISTRY, tagId);
            }

            float chance = GsonHelper.getAsFloat(json, "chance", 1.0F);
            int count = GsonHelper.getAsInt(json, "count", 1);

            return new AddItemModifier(conditions, item, tag, chance, count);
        }

        @Override
        public JsonObject write(AddItemModifier instance) {
            JsonObject json = this.makeConditions(instance.conditions);

            if (instance.item != null) {
                ResourceLocation id = ForgeRegistries.ITEMS.getKey(instance.item);
                if (id != null) {
                    json.addProperty("item", id.toString());
                }
            }

            if (instance.tag != null) {
                json.addProperty("tag", instance.tag.location().toString());
            }

            if (instance.chance != 1.0F) {
                json.addProperty("chance", instance.chance);
            }

            if (instance.count != 1) {
                json.addProperty("count", instance.count);
            }

            return json;
        }
    }
}
