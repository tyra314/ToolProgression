package tyra314.toolprogression.handlers;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.harvest.BlockOverwrite;
import tyra314.toolprogression.harvest.HarvestHelper;

public class HarvestEventHandler
{
    /*
     * This event used to be the central point of this mod. But, thanks to the core mod I only need this to prevent the
     * destroying of not harvestable blocks.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        // First, get the blockstate, we are actually trying to break
        IBlockState state = event.getState();

        if (!HarvestHelper.canPlayerHarvestBlock(event.getEntityPlayer(), event.getState()))
        {
            // If this block is not harvestable and we want to prevent block destruction...

            BlockOverwrite overwrite = ConfigHandler.blockOverwrites.get(state);
            if (ConfigHandler.prevent_block_destruction &&
                (overwrite == null || !overwrite.destroyable))
            {
                // ... and there is no exception in the block overwrite, we just cancel the event and thus prevent the
                // block from being broken
                event.setCanceled(true);
            }
        }
    }
}
