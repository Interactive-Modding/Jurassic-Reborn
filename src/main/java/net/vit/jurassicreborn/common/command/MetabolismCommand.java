package net.vit.jurassicreborn.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.vit.jurassicreborn.common.util.GameRuleHandler;

public class MetabolismCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("metabolism")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.literal("on")
                        .executes(ctx -> set(ctx.getSource(), true)))
                .then(Commands.literal("off")
                        .executes(ctx -> set(ctx.getSource(), false)))
                .then(Commands.literal("toggle")
                        .executes(ctx -> toggle(ctx.getSource()))));
    }

    private static int set(CommandSourceStack source, boolean value) {
        ServerLevel level = source.getLevel();
        GameRules.BooleanValue rule = level.getGameRules().getRule(GameRuleHandler.DINO_METABOLISM);
        rule.set(value, level.getServer());
        source.sendSuccess(new TextComponent(value ? "Dinosaur metabolism enabled" : "Dinosaur metabolism disabled"), true);
        return 1;
    }

    private static int toggle(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        GameRules.BooleanValue rule = level.getGameRules().getRule(GameRuleHandler.DINO_METABOLISM);
        boolean value = !rule.get();
        rule.set(value, level.getServer());
        source.sendSuccess(new TextComponent(value ? "Dinosaur metabolism enabled" : "Dinosaur metabolism disabled"), true);
        return 1;
    }
}