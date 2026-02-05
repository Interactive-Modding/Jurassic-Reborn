package net.vit.jurassicreborn.common.paleopad;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlappyDinoApp extends App {
    private final List<Integer> scores = new ArrayList<>();
    private int selectedCharacter;
    @Override
    public String getName() {
        return "Flappy Dino";
    }

    @Override
    public void update() {
    }

    @Override
    public void writeToNBT(CompoundTag nbt) {
        nbt.putInt("SelectedCharacter", selectedCharacter);
        ListTag list = new ListTag();
        for (int score : scores) {
            list.add(IntTag.valueOf(score));
        }
        nbt.put("Scores", list);
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        selectedCharacter = nbt.getInt("SelectedCharacter");
        scores.clear();
        ListTag list = nbt.getList("Scores", IntTag.TAG_INT);
        for (int i = 0; i < list.size(); i++) {
            scores.add(list.getInt(i));
        }
        scores.sort(Collections.reverseOrder());
    }

    @Override
    public void init() {
    }

    public List<Integer> getScores() {
        return scores;
    }

    public void addScore(int score) {
        scores.add(score);
        scores.sort(Collections.reverseOrder());
    }

    public int getSelectedCharacter() {
        return selectedCharacter;
    }

    public void setSelectedCharacter(int index) {
        selectedCharacter = index;
    }
}
