package tyra314.toolprogression.harvest;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nonnull;

public class BlockHelper
{
    static String getConfig(IBlockState state)
    {
        if (state.getMaterial().isToolNotRequired())
        {
            return "null=-1";
        }

        Block block = state.getBlock();

        String toolClass = block.getHarvestTool(block.getDefaultState());
        int level = block.getHarvestLevel(block.getDefaultState());

        return String.format("%s=%d", toolClass, level);
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
