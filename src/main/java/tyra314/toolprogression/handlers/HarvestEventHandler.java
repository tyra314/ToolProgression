package tyra314.toolprogression.handlers;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tyra314.toolprogression.harvest.HarvestHelper;

public class HarvestEventHandler
{
    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        IBlockState state = event.getState();

        if (!HarvestHelper.canPlayerHarvestBlock(event.getEntityPlayer(), state, event
                .getEntityPlayer().getEntityWorld(), event.getPos()))
        {
            event.setCanceled(true);
        }
    }
}
