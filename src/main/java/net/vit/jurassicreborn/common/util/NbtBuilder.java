package net.vit.jurassicreborn.common.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.UUID;

public class NbtBuilder {
    private CompoundTag root = new CompoundTag();

    public NbtBuilder(){

    }
    public NbtBuilder(CompoundTag tag){
        this.root = tag;

    };

    public NbtBuilder putInt(String key, int value){
        this.root.putInt(key, value);
        return this;
    }

    public NbtBuilder putFloat(String key, float value){
        this.root.putFloat(key, value);
        return this;
    }

    public NbtBuilder putString(String key, String value){
        this.root.putString(key, value);
        return this;
    }
    public NbtBuilder putBoolean(String key, boolean value){
        this.root.putBoolean(key, value);
        return this;
    }

    public NbtBuilder putTag(String key, Tag value){
        this.root.put(key, value);
        return this;
    }

    public NbtBuilder putByte(String key, byte value){
        this.root.putByte(key, value);
        return this;
    }
    public NbtBuilder putUuid(String key, UUID value){
        this.root.putUUID(key, value);
        return this;
    }

    public NbtBuilder putCompound(String key, CompoundTag value){
        this.root.put(key, value);
        return this;
    }

    public NbtBuilder putIntArray(String key, int[] value){
        this.root.putIntArray(key, value);
        return this;
    }
    public NbtBuilder putLongArray(String key, long[] value){
        this.root.putLongArray(key, value);
        return this;
    }

    public CompoundTag build(){
        return this.root;
    }
}
