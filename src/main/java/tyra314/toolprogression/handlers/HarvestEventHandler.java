package tyra314.toolprogression.handlers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.harvest.BlockHelper;
import tyra314.toolprogression.harvest.BlockOverwrite;

public class HarvestEventHandler
{
    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        IBlockState state = event.getState();

        BlockOverwrite overwrite = ConfigHandler.blockOverwrites.get(BlockHelper.getKeyString
                (state));

        if (overwrite == null)
        {
            return;
        }

        int required_level = state.getBlock().getHarvestLevel(state);

        ItemStack item = event.getEntityPlayer().getHeldItemMainhand();

        if (item.isEmpty())
        {
            if (required_level >= 0)
            {
                event.setCanceled(true);
            }

            return;
        }

        String toolclass = state.getBlock().getHarvestTool(state);

        if (toolclass != null && item.getItem() instanceof ItemTool)
        {
            int level = item.getItem().getHarvestLevel(item, toolclass, event.getEntityPlayer(), state);

            if (level == -1)
            {
                level = ((ItemTool) item.getItem()).getToolMaterial().getHarvestLevel();
            }

            event.setCanceled(level < required_level);

            return;
        }

        if (required_level >= 0)
        {
            event.setCanceled(true);
        }
    }
}
