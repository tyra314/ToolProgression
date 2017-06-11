package tyra314.toolprogression.harvest;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import tyra314.toolprogression.proxy.CommonProxy;

public class OverwriteHelper
{
    public static void handleBlock(Block block)
    {
        for (IBlockState state : block.getBlockState().getValidStates())
        {
            String config = BlockHelper.getConfig(state);

            CommonProxy.blocks_config.getString(BlockHelper.getKeyString(state), "block", config, BlockHelper.getKeyString(state));

            BlockOverwrite.applyToState(state);
        }
    }

    public static void handleItem(Item item)
    {
        String config = ToolOverwrite.getConfig(item);

        if (config == null)
        {
            return;
        }

        //noinspection ConstantConditions
        CommonProxy.tools_config.getString(item.getRegistryName().toString(), "tool", config, item.getRegistryName().toString());

        ToolOverwrite.applyToItem(item);
    }

    public static void handleMaterial(ItemTool.ToolMaterial mat)
    {
        String config = MaterialHelper.getConfig(mat);
        String name = MaterialHelper.getName(mat);

        CommonProxy.mats_config.getString(name, "material", config, name);

        MaterialOverwrite.applyToMaterial(mat);
    }
}
