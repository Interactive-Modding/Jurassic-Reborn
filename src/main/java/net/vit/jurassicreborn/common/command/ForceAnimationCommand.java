package net.vit.jurassicreborn.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.EntityUtils.Animatable;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class ForceAnimationCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("animate")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("anim", ResourceLocationArgument.id())
                        .suggests((ctx, builder) -> {
                            for (EntityAnimation anim : EntityAnimation.values()) {
                                builder.suggest(anim.name().toLowerCase(Locale.ROOT));
                            }
                            return builder.buildFuture();
                        })
                        .executes(ctx -> run(ctx, Collections.singleton(getSourceEntity(ctx))))
                        .then(Commands.argument("targets", EntityArgument.entities())
                                .executes(ctx -> run(ctx, EntityArgument.getEntities(ctx, "targets"))))));
    }

    private static Entity getSourceEntity(CommandContext<CommandSourceStack> ctx) {
        Entity entity = ctx.getSource().getEntity();
        if (entity == null) {
            ctx.getSource().sendFailure(Component.literal("No entity was provided"));
        }
        return entity;
    }

    private static int run(CommandContext<CommandSourceStack> ctx, Collection<? extends Entity> targets) {
        if (targets == null || targets.isEmpty()) {
            return 0;
        }

        ResourceLocation id = ResourceLocationArgument.getId(ctx, "anim");
        String animKey = id.getPath();

        EntityAnimation anim;
        try {
            anim = EntityAnimation.valueOf(animKey.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            ctx.getSource().sendFailure(Component.literal("Unknown animation: " + animKey));
            return 0;
        }

        int played = 0;
        for (Entity e : targets) {
            if (e instanceof Animatable a) {
                a.setAnimation(anim.get());
                played++;
            }
        }

        if (played == 0) {
            ctx.getSource().sendFailure(Component.literal("No animatable entities found"));
            return 0;
        }

        ctx.getSource().sendSuccess(Component.literal("Played " + animKey + " on " + played + " entities"), true);
        return played;
    }
}