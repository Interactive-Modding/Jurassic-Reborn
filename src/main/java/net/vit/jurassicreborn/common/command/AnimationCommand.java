package net.vit.jurassicreborn.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class AnimationCommand {

    /* ---------------- registration ---------------------------------- */
    public static void register(CommandDispatcher<CommandSourceStack> d) {

        d.register(Commands.literal("animation")
                .requires(s -> s.hasPermission(2))
                .then(Commands.argument("dino", ResourceLocationArgument.id())
                        .then(Commands.argument("anim", ResourceLocationArgument.id()) // use id for tab-complete
                                .suggests((ctx,b) -> {
                                    for (EntityAnimation a : EntityAnimation.values()) {
                                        b.suggest(a.name().toLowerCase(Locale.ROOT));
                                    }
                                    return b.buildFuture();
                                })
                                .executes(AnimationCommand::run))));
    }

    /* ---------------- execution -------------------------------------- */
    private static int run(CommandContext<CommandSourceStack> ctx) {

        ResourceLocation dinoId  = ResourceLocationArgument.getId(ctx, "dino");
        String           animKey = ResourceLocationArgument.getId(ctx, "anim").getPath();

        EntityAnimation anim;
        try {
            anim = EntityAnimation.valueOf(animKey.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            ctx.getSource().sendFailure(Component.literal("Unknown animation: " + animKey));
            return 0;
        }

        EntityType<?> type = Registry.ENTITY_TYPE.getOptional(dinoId)
                .orElse(null);

        if (type == null) {
            ctx.getSource().sendFailure(Component.literal("Unknown dinosaur id: " + dinoId));
            return 0;
        }

        Level level = ctx.getSource().getLevel();
        int   played = 0;

        AABB worldBox = new AABB(Integer.MIN_VALUE, level.getMinBuildHeight(),
                Integer.MIN_VALUE,
                Integer.MAX_VALUE, level.getMaxBuildHeight(),
                Integer.MAX_VALUE);

        for (DinosaurEntity dino : level.getEntitiesOfClass(
                DinosaurEntity.class, worldBox,
                e -> e.getType() == type)) {

            dino.setAnimation(anim.get());
            played++;
        }

        if (played == 0) {
            ctx.getSource().sendFailure(Component.literal("No living " + dinoId + " found"));
            return 0;
        }

        ctx.getSource().sendSuccess(
                Component.literal("Played " + animKey + " on " + played + " " + dinoId), true);
        return played;
    }
}
