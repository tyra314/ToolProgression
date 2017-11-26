package tyra314.toolprogression.harvest;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import tyra314.toolprogression.ToolProgressionMod;

import javax.annotation.Nonnull;

public class BlockHelper
{
    static String getConfigFromState(IBlockState state)
    {
        Block block = state.getBlock();

        float hardness = -2F;

        try
        {
            hardness = block.getBlockHardness(state, null, null);
        }
        catch(Exception e)
        {
            ToolProgressionMod.logger.warn("Couldn't get hardness of block: " + getKeyString(state));
        }

        String toolClass = block.getHarvestTool(state);

        if (toolClass == null )
        {
            return String.format("null=-1@%.1f", hardness);
        }

        int level = block.getHarvestLevel(state);

        return (state.getMaterial().isToolNotRequired() ? "?" : "") + String.format("%s=%d@%.1f", toolClass, level, hardness);
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
