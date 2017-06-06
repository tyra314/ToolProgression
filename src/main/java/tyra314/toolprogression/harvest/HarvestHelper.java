package tyra314.toolprogression.harvest;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tyra314.toolprogression.config.ConfigHandler;

public class HarvestHelper
{
    public static boolean canItemHarvestBlock(EntityPlayer player, IBlockState state, World world, BlockPos pos)
    {
        ItemStack item = player.getHeldItemMainhand();

        BlockOverwrite overwrite = ConfigHandler.blockOverwrites.get(BlockHelper.getKeyString
                (state));

        if (overwrite == null)
        {
            return state.getBlock().canHarvestBlock(world, pos, player);
        }

        int required_level = state.getBlock().getHarvestLevel(state);

        if (item.isEmpty())
        {
            return required_level < 0;
        }

        String toolclass = state.getBlock().getHarvestTool(state);

        if (toolclass != null)
        {
            int level = item.getItem().getHarvestLevel(item, toolclass, player, state);

            if (level == -1 && item.getItem() instanceof ItemTool)
            {
                level = ((ItemTool) item.getItem()).getToolMaterial().getHarvestLevel();
            }

            return level >= required_level;
        }

        return required_level < 0;
    }
}
