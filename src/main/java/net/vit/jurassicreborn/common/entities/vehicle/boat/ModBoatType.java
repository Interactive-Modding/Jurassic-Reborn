package net.vit.jurassicreborn.common.entities.vehicle.boat;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * Represents the different Jurassic Reborn boat variants.
 */
public enum ModBoatType implements StringRepresentable {
    ARAUCARIA("araucaria"),
    CALAMITES("calamites"),
    GINKGO("ginkgo"),
    MAGNOLIA("magnolia"),
    PHOENIX("phoenix"),
    PSARONIUS("psaronius");

    private static final ModBoatType[] VALUES = values();

    private final String name;
    private Supplier<Item> boatItem = () -> Items.OAK_BOAT;
    ModBoatType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public void setBoatItem(Supplier<Item> boatItem) {
        this.boatItem = boatItem;
    }

    public Item getBoatItem() {
        return this.boatItem.get();
    }

    public static ModBoatType byId(int id) {
        if (id < 0 || id >= VALUES.length) {
            return VALUES[0];
        }
        return VALUES[id];
    }

    public static ModBoatType byName(String name) {
        for (ModBoatType type : VALUES) {
            if (type.getSerializedName().equals(name)) {
                return type;
            }
        }
        return VALUES[0];
    }

    @Override
    public String toString() {
        return this.name.toLowerCase(Locale.ROOT);
    }
}
