package tyra314.toolprogression.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand
{
    abstract public String getName();

    public boolean matches(@Nonnull String[] args)
    {
        return args[0].equals(getName());
    }

    abstract public boolean handleCommand(@Nonnull MinecraftServer server,
                                   @Nonnull ICommandSender sender,
                                   @Nonnull String[] args);

    public @Nonnull List<String> provideCompletions(String[] args)
    {
        List<String> completions = new ArrayList<>();

        if (args.length >= 1 && getName().startsWith(args[0]))
        {
            completions.add(getName());
        }

        return completions;
    }

    abstract public @Nonnull String getSynopsis();

    protected void sendMessage(ICommandSender sender, String msg)
    {
        sender.sendMessage(new TextComponentString(TextFormatting.YELLOW.toString() +
                                                   TextFormatting.ITALIC.toString() +
                                                   "ToolProgression " +
                                                   TextFormatting.RESET.toString() +
                                                   msg));
    }
}
