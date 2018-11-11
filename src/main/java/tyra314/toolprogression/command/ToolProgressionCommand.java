package tyra314.toolprogression.command;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.command.subcommand.Reload;
import tyra314.toolprogression.command.subcommand.Set;
import tyra314.toolprogression.command.subcommand.Unset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ToolProgressionCommand extends CommandBase
{
    private static final SubCommand[] subcommands = {
            new Reload(),
            new Set(),
            new Unset()
    };

    @Override
    public @Nonnull
    String getName()
    {
        return "tpr";
    }

    @Override
    public @Nonnull
    String getUsage(@Nonnull ICommandSender sender)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(getName());
        sb.append(" ");

        boolean first = true;

        for (SubCommand sc : subcommands)
        {
            if (!first)
            {
                sb.append(" | ");
            }

            first = false;
            sb.append(sc.getSynopsis());
        }

        return sb.toString();
    }

    @Override
    public @Nonnull
    List<String> getAliases()
    {
        return Lists.newArrayList(ToolProgressionMod.MODID);
    }

    @Override
    public void execute(@Nonnull MinecraftServer server,
                        @Nonnull ICommandSender sender,
                        @Nonnull String[] args)
    {
        for (SubCommand sc : subcommands)
        {
            if (sc.matches(args))
            {
                if (sc.handleCommand(server, sender, args))
                {
                    return;
                }
            }
        }

        sender.sendMessage(new TextComponentString(TextFormatting.RED + "Error parsing command"));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        if (server.isSinglePlayer())
        {
            return true;
        }

        sender.sendMessage(new TextComponentString(TextFormatting.RED +
                                                   "I'm sorry, but this command can only be used in single player mode."));

        return false;
    }

    @Override
    public @Nonnull
    List<String> getTabCompletions(@Nonnull MinecraftServer server,
                                   @Nonnull ICommandSender sender,
                                   @Nonnull String[] args,
                                   @Nullable BlockPos targetPos)
    {
        List<String> completions = new ArrayList<>();

        for (SubCommand sc : subcommands)
        {
            completions.addAll(sc.provideCompletions(args));
        }

        return completions;
    }
}
