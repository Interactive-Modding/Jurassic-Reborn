package mod.reborn.server.command;

import com.google.common.collect.Lists;
import mod.reborn.server.world.structure.IslaSornaLabGenerator;
import mod.reborn.server.world.structure.RaptorPaddockGenerator;
import mod.reborn.server.world.structure.StructureGenerator;
import mod.reborn.server.world.structure.VisitorCentreGenerator;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpawnStructureCommand extends CommandBase {
    @Override
    public String getName() {
        return "spawnrb";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.spawnrb.usuage"; //TODO write proper
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 1) {
            throw new WrongUsageException("commands.spawnrb.usuage");
        }
        String name = args[0];
        Random random = new Random();

        StructureGenerator generator;
        if(name.equalsIgnoreCase("visitor_center")) {
            generator = new VisitorCentreGenerator(random);
        } else if(name.equalsIgnoreCase("raptor_paddock")) {
            generator = new RaptorPaddockGenerator(random);
        } else if(name.equalsIgnoreCase("isla_sorna_lab")) {
            generator = new IslaSornaLabGenerator(random);
        } else {
            throw new CommandException("commands.spawnrb.notfound", name);
        }

        if(args.length > 1) {
            try {
                generator.setRotation(Rotation.valueOf(args[1].toUpperCase(Locale.ENGLISH)));
            } catch (IllegalArgumentException e) {
                throw new CommandException("commands.spawnrb.notrot", args[1]);
            }
        }
        if(args.length > 2) {
            try {
                generator.setMirror(Mirror.valueOf(args[2].toUpperCase(Locale.ENGLISH)));
            } catch (IllegalArgumentException e) {
                throw new CommandException("commands.spawnrb.notmirror", args[2]);
            }

        }
        if(args.length > 3) {
            random = new Random(parseLong(args[3]));
        }

        generator.generate(sender.getEntityWorld(), random, sender.getPosition());
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length == 1) {
            return getListOfStringsMatchingLastWord(args, Lists.newArrayList("visitor_center", "raptor_paddock", "isla_sorna_lab"));
        } else if(args.length == 2) {
            return getListOfStringsMatchingLastWord(args, Stream.of(Rotation.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()));
        } else if(args.length == 3) {
            return getListOfStringsMatchingLastWord(args, Stream.of(Mirror.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()));
        }
        return Lists.newArrayList();
    }
}
