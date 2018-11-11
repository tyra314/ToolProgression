package tyra314.toolprogression.harvest;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import tyra314.toolprogression.proxy.CommonProxy;

public class Overwrites
{
    public static void handleBlock(Block block)
    {
        for (IBlockState state : block.getBlockState().getValidStates())
        {
            String config = BlockHelper.getConfigString(state);

            CommonProxy.blocks_config.getString(BlockHelper.getKeyString(state),
                    "block",
                    config,
                    BlockHelper.getKeyString(state));

            BlockHelper.applyTo(state);
        }
    }

    public static void handleItem(Item item)
    {
        String config = ToolHelper.getConfigString(item);

        if (!config.isEmpty())
        {
            //noinspection ConstantConditions
            CommonProxy.tools_config.getString(item.getRegistryName().toString(),
                    "tool",
                    config,
                    item.getRegistryName().toString());
        }

        ToolHelper.applyToItem(item);
    }

    public static void handleMaterial(ItemTool.ToolMaterial mat)
    {
        String config = String.valueOf(mat.getHarvestLevel());
        String name = mat.name().toLowerCase();

        CommonProxy.mats_config.getString(name, "material", config, name);

        MaterialOverwrite.applyToMaterial(mat);
    }
}
