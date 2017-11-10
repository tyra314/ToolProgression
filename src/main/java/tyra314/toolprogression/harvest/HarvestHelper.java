package tyra314.toolprogression.harvest;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import tyra314.toolprogression.compat.gamestages.GSEventHandler;
import tyra314.toolprogression.compat.gamestages.GSHelper;
import tyra314.toolprogression.config.ConfigHandler;

public class HarvestHelper
{
    public static Result canPlayerHarvestReason(EntityPlayer player, IBlockState state)
    {
        if (GSHelper.isLoaded())
        {
            state = GSEventHandler.getStagedBlockState(player, state);
        }

        BlockOverwrite overwrite = ConfigHandler.blockOverwrites.get(state);

        if ((overwrite == null && state.getMaterial().isToolNotRequired()) ||
            (overwrite != null && !overwrite.toolRequired))
        {
            // we can harvest with our plain hands!
            return Result.NONE;
        }

        // TODO, can required_toolclass actually be null?
        String required_toolclass = state.getBlock().getHarvestTool(state);

        if(required_toolclass == null || required_toolclass.equals("null"))
        {
            return Result.NONE;
        }

        // if we make it here, we actually need a tool to harvest the block

        ItemStack tool = player.getHeldItemMainhand();
        if (tool.isEmpty())
        {
            return Result.TOOL_CLASS;
        }

        // if we make it here, we have a "tool" in our hands

        int tool_level = tool.getItem().getHarvestLevel(tool, required_toolclass, player, state);

        if (tool_level < 0)
        {
            return Result.TOOL_CLASS;
        }

        int required_level = state.getBlock().getHarvestLevel(state);

        return tool_level >= required_level ? Result.NONE : Result.LEVEL;
    }

    public static boolean canPlayerHarvestBlock(EntityPlayer player, IBlockState state)
    {
        return canPlayerHarvestReason(player, state) == Result.NONE;
    }

    public enum Result
    {
        TOOL_CLASS,
        LEVEL,
        NONE
    }
}
