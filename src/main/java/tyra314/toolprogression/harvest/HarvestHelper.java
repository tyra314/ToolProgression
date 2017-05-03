package tyra314.toolprogression.harvest;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HarvestHelper
{
    public static boolean canItemHarvestBlock(EntityPlayer player, IBlockState state, World world, BlockPos pos)
    {
        ItemStack item = player.getHeldItemMainhand();

        if (!BlockOverwrite.overwrites.containsKey(BlockHelper.getKeyString(state)))
        {
            return state.getBlock().canHarvestBlock(world, pos, player);
        }

        int required_level = state.getBlock().getHarvestLevel(state);

        if (item.isEmpty())
        {
            return required_level < 0;
        }

        String toolclass = state.getBlock().getHarvestTool(state);

        if (toolclass != null && item.getItem() instanceof ItemTool)
        {
            int level = item.getItem().getHarvestLevel(item, toolclass, player, state);

            return level >= required_level;
        }

        return required_level < 0;
    }
}
