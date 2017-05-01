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
import tyra314.toolprogression.harvest.OverwriteHelper;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ToolProgressionCommand extends CommandBase
{
    private final List<String> aliases = Lists.newArrayList(ToolProgressionMod.MODID);

    @Override
    public String getName()
    {
        return "tpr";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "tpr reload";
    }

    @Override
    public List<String> getAliases()
    {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
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
            OverwriteHelper.applyOverwrite(block);
        }

        final IForgeRegistry<Item> item_registry = GameRegistry.findRegistry(Item.class);

        for (Item item : item_registry)
        {
            if (item instanceof ItemTool)
            {
                OverwriteHelper.applyOverwrite(item);
            }
        }
        sender.sendMessage(new TextComponentString("ToolProgression configuration reloaded."));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args[1].equals(""))
        {
            return Lists.newArrayList("reload");
        }

        return Collections.emptyList();
    }
}
