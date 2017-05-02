package tyra314.toolprogression.command;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.harvest.BlockOverwrite;
import tyra314.toolprogression.harvest.ToolOverwrite;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ToolProgressionCommand extends CommandBase
{
    private final List<String> aliases = Lists.newArrayList(ToolProgressionMod.MODID);

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
        return "tpr reload";
    }

    @Override
    public @Nonnull
    List<String> getAliases()
    {
        return aliases;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
    {
        if (args.length != 1 || !args[0].equals("reload"))
        {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Error parsing command"));

            return;
        }

        ConfigHandler.readBaseConfig();

        final IForgeRegistry<Block> block_registry = GameRegistry.findRegistry(Block.class);

        for (Block block : block_registry)
        {
            BlockOverwrite.applyToAllStates(block);
        }

        final IForgeRegistry<Item> item_registry = GameRegistry.findRegistry(Item.class);

        for (Item item : item_registry)
        {
            if (item instanceof ItemTool)
            {
                ToolOverwrite.applyToItem(item);
            }
        }
        sender.sendMessage(new TextComponentString("§e§lToolProgression§r configuration reloaded."));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public @Nonnull
    List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos)
    {
        if ("reload".startsWith(args[0]) && args.length < 2)
        {
            return Lists.newArrayList("reload");
        }

        return Collections.emptyList();
    }
}
