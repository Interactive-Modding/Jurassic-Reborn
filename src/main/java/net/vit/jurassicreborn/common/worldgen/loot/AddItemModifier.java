// AddItemModifier.java
package net.vit.jurassicreborn.common.worldgen.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;
import java.util.List;
import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {

    /* ---------- Codec ---------- */


    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst ->
                    codecStart(inst)
                            .and(ForgeRegistries.ITEMS.getCodec()
                                    .optionalFieldOf("item")
                                    .forGetter(m -> Optional.ofNullable(m.item)))
                            .and(ResourceLocation.CODEC
                                    .xmap(id -> TagKey.create(Registries.ITEM, id),
                                            TagKey::location)
                                    .optionalFieldOf("tag")
                                    .forGetter(m -> Optional.ofNullable(m.tag)))
                            .and(Codec.FLOAT.optionalFieldOf("chance", 1.0F)
                                    .forGetter(m -> m.chance))
                            .and(Codec.INT.optionalFieldOf("count", 1)
                                    .forGetter(m -> m.count))
                            /* -------- unwrap the Optionals here -------- */
                            .apply(inst, (conditions,
                                          optItem,
                                          optTag,
                                          chance,
                                          count) ->
                                    new AddItemModifier(
                                            conditions,
                                            optItem.orElse(null),
                                            optTag.orElse(null),
                                            chance,
                                            count))));

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
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generated,
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

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
