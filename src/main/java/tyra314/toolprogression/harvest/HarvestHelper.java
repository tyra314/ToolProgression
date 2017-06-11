package tyra314.toolprogression.harvest;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tyra314.toolprogression.config.ConfigHandler;

public class HarvestHelper
{
    public enum Result
    {
        TOOL_CLASS,
        LEVEL,
        NONE
    }

    public static Result canPlayerHarvestReason(EntityPlayer player,
                                                IBlockState state,
                                                World world,
                                                BlockPos pos)
    {
        ItemStack item = player.getHeldItemMainhand();

        BlockOverwrite overwrite = ConfigHandler.blockOverwrites.get(state);

        if (overwrite == null)
        {
            boolean result = state.getBlock().canHarvestBlock(world, pos, player);

            if (result)
            {
                return Result.NONE;
            }
        }

        int required_level = state.getBlock().getHarvestLevel(state);

        if (item.isEmpty())
        {
            return required_level < 0 ? Result.NONE : Result.TOOL_CLASS;
        }

        String toolclass = state.getBlock().getHarvestTool(state);

        if (toolclass != null)
        {
            int level = item.getItem().getHarvestLevel(item, toolclass, player, state);

            return level >= required_level ? Result.NONE : Result.LEVEL;
        }

        return required_level < 0 ? Result.NONE : Result.TOOL_CLASS;
    }


    public static boolean canPlayerHarvestBlock(EntityPlayer player,
                                                IBlockState state,
                                                World world,
                                                BlockPos pos)
    {
        return canPlayerHarvestReason(player, state, world, pos) == Result.NONE;
    }
}
