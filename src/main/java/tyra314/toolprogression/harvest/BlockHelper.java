package tyra314.toolprogression.harvest;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nonnull;

public class BlockHelper
{
    static String getConfigString(IBlockState state)
    {
        Block block = state.getBlock();

        String toolClass = block.getHarvestTool(block.getDefaultState());

        if (toolClass == null )
        {
            return "null=-1";
        }

        int level = block.getHarvestLevel(block.getDefaultState());

        return (state.getMaterial().isToolNotRequired() ? "?" : "") + String.format("%s=%d", toolClass, level);
    }

    public static String getKeyString(@Nonnull IBlockState state)
    {
        Block block = state.getBlock();

        //noinspection ConstantConditions
        String key = block.getRegistryName().toString();

        int meta = block.getMetaFromState(state);

        return String.format("%s:%d", key, meta);
    }
}
