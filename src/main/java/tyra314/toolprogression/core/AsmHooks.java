package tyra314.toolprogression.core;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import tyra314.toolprogression.harvest.HarvestHelper;

import javax.annotation.Nonnull;

public class AsmHooks
{
    @SuppressWarnings("unused")
    public static boolean canHarvestBlock(@Nonnull EntityPlayer player,
                                          @Nonnull IBlockAccess world,
                                          @Nonnull BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        state = state.getActualState(world, pos);
        return HarvestHelper.canPlayerHarvestBlock(player, state);
    }
}
