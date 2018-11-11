package tyra314.toolprogression.command.subcommand;

import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemTool;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import tyra314.toolprogression.command.SubCommand;
import tyra314.toolprogression.compat.tconstruct.TiCHelper;
import tyra314.toolprogression.compat.tconstruct.TiCMiningLevels;
import tyra314.toolprogression.compat.top.TOPHelper;
import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.harvest.BlockHelper;
import tyra314.toolprogression.harvest.MaterialOverwrite;
import tyra314.toolprogression.harvest.ToolHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Reload extends SubCommand
{
    @Override
    public String getName()
    {
        return "reload";
    }

    @Override
    public boolean handleCommand(@Nonnull MinecraftServer server,
                                 @Nonnull ICommandSender sender,
                                 @Nonnull String[] args)
    {
        ConfigHandler.readBaseConfig();

        final IForgeRegistry<Block> block_registry = GameRegistry.findRegistry(Block.class);

        for (Block block : block_registry)
        {
            BlockHelper.applyToAll(block);
        }

        final IForgeRegistry<Item> item_registry = GameRegistry.findRegistry(Item.class);

        for (Item item : item_registry)
        {
            if (item instanceof ItemTool || item instanceof ItemHoe)
            {
                ToolHelper.applyToItem(item);
            }
        }

        for (ItemTool.ToolMaterial mat : ItemTool.ToolMaterial.values())
        {
            MaterialOverwrite.applyToMaterial(mat);
        }

        if (TiCHelper.isLoaded())
        {
            TiCMiningLevels.overwriteMiningLevels();
        }

        if (TOPHelper.isLoaded() && ConfigHandler.top_compat)
        {
            TOPHelper.overwriteHarvestLevels();
        }

        sendMessage(sender, "configuration reloaded.");

        return true;
    }

    @Nonnull
    @Override
    public List<String> provideCompletions(String[] args)
    {
        List<String> completions = new ArrayList<>();

        if(args.length == 1 && getName().startsWith(args[0]))
        {
            completions.add(getName());
        }

        return completions;
    }

    @Nonnull
    @Override
    public String getSynopsis()
    {
        return getName();
    }
}
