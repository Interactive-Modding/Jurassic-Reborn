package net.vit.jurassicreborn.common.blocks.fossil;

public interface EncasedFossil extends FossilBlock{

    @Override
    default boolean mustBandage() {
        return false;
    }
}
