package mod.reborn.server.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;

public class SpawnStructureCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        SpawnStructureCommand command = new SpawnStructureCommand();
        //dispatcher.register(Commands.literal("spawnrb").requires(plr -> plr.hasPermissionLevel(2)).executes(context -> System.out.println(context.toString())));
    }
}
