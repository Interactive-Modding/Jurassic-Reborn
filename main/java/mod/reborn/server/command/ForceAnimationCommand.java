/**
 * Copyright (C) 2015 by jabelar
 * <p>
 * This file is part of jabelar's Minecraft Forge modding examples; as such, you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p>
 * For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
 */

package mod.reborn.server.command;

import com.google.common.collect.Lists;
import mod.reborn.RebornMod;
import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.api.Animatable;
import net.minecraft.command.*;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author jabelar
 */
public class ForceAnimationCommand implements ICommand {
    private final List<String> aliases;

    public ForceAnimationCommand() {
        this.aliases = new ArrayList<>();
        this.aliases.add("animate");
        this.aliases.add("anim");
    }

    @Override
    public int compareTo(ICommand command) {
        return this.getName().compareTo(command.getName());
    }

    @Override
    public String getName() {
        return "animate";
    }

    @Override
    public String getUsage(ICommandSender parSender) {
        return "animate <AnimID> [<entitySelector>]";
    }

    @Override
    public List<String> getAliases() {
        return this.aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        World world = sender.getEntityWorld();
        if (world.isRemote) {
            RebornMod.getLogger().debug("Not processing on Client side");
        } else {
            RebornMod.getLogger().debug("Processing on Server side");
            if (args.length < 1) {
                throw new WrongUsageException("Missing the animation to set");
            }
            String entitySelector = args.length < 2 ? "@e[c=1]" : args[1];
            List<? extends EntityLivingBase> entities = EntitySelector.matchEntities(new ProxySender(server, sender), entitySelector, EntityLivingBase.class);

            if (entities.size() == 0) {
                throw new EntityNotFoundException("No DinosaurEntity to animate");
            }

            for (EntityLivingBase entity : entities) {
                if (entity instanceof Animatable) {
                    Animatable animatable = (Animatable) entity;
                    try {
                        animatable.setAnimation(EntityAnimation.valueOf(args[0].toUpperCase(Locale.ENGLISH)).get());
                    } catch (IllegalArgumentException iae) {
                        throw new CommandException(args[0] + " is not a valid animation.");
                    }
                    sender.sendMessage(new TextComponentString("Animating entity " + entity.getEntityId() + " with animation type " + args[0]));
                }
            }
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            List<String> animations = Lists.newArrayList();
            String current = args[0].toLowerCase(Locale.ENGLISH);

            for (EntityAnimation animation : EntityAnimation.values()) {
                if (animation.name().toLowerCase(Locale.ENGLISH).startsWith(current)) {
                    animations.add(animation.name());
                }
            }

            return animations;
        }
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] var1, int var2) {
        return false;
    }

    /**
     * A proxy sender that can always execute the "@" (selection) command.
     *
     * @author WorldSEnder
     */
    private static class ProxySender implements ICommandSender {
        private final ICommandSender original;
        private MinecraftServer server;

        public ProxySender(MinecraftServer server, ICommandSender proxy) {
            this.original = Objects.requireNonNull(proxy);
            this.server = server;
        }

        @Override
        public void sendMessage(ITextComponent component) {
            this.original.sendMessage(component);
        }

        @Override
        public boolean canUseCommand(int permLevel, String commandName) {
            return commandName.equals("@") || this.original.canUseCommand(permLevel, commandName);
        }

        @Override
        public Entity getCommandSenderEntity() {
            return this.original.getCommandSenderEntity();
        }

        @Override
        public String getName() {
            return this.original.getName();
        }

        @Override
        public ITextComponent getDisplayName() {
            return this.original.getDisplayName();
        }

        @Override
        public World getEntityWorld() {
            return this.original.getEntityWorld();
        }

        @Override
        public BlockPos getPosition() {
            return this.original.getPosition();
        }

        @Override
        public Vec3d getPositionVector() {
            return this.original.getPositionVector();
        }

        @Override
        public boolean sendCommandFeedback() {
            return this.original.sendCommandFeedback();
        }

        @Override
        public void setCommandStat(Type type, int amount) {
            this.original.setCommandStat(type, amount);
        }

        @Override
        public MinecraftServer getServer() {
            return this.server;
        }
    }
}
