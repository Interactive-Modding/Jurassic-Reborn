package mod.reborn.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class SpawnStructureCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        SpawnStructureCommand command = new SpawnStructureCommand();
        //dispatcher.register(Commands.literal("spawnrb").requires(plr -> plr.hasPermissionLevel(2)).executes(context -> System.out.println(context.toString())));
    }
}
