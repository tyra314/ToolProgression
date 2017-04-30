package tyra314.toolprogression.harvest;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tyra314.toolprogression.proxy.CommonProxy;

public class OverwriteHelper
{

    public static void handleBlock(Block block)
    {
        String config = BlockOverwrite.getConfig(block);

        CommonProxy.blocks_config.getString(block.getRegistryName().toString(), "block", config, block.getLocalizedName());

        if (BlockOverwrite.overwrites.containsKey(block.getRegistryName()))
        {
            BlockOverwrite overwrite = BlockOverwrite.overwrites.get(block.getRegistryName());

            BlockOverwrite.applyTo(block);
        }
    }

    public static void handleItem(Item item)
    {
        String config = ToolOverwrite.getConfig(item);

        if (config == null)
        {
            return;
        }

        CommonProxy.tools_config.getString(item.getRegistryName().toString(), "tool", config, item.getItemStackDisplayName(new ItemStack(item)));

        if (ToolOverwrite.overwrites.containsKey(item.getRegistryName()))
        {
            ToolOverwrite overwrite = ToolOverwrite.overwrites.get(item.getRegistryName());

            ToolOverwrite.applyTo(item);
        }
    }
}
