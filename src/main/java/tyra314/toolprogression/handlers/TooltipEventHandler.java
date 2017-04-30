package tyra314.toolprogression.handlers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tyra314.toolprogression.harvest.HarvestLevel;

public class TooltipEventHandler
{
    public static boolean enabled = true;

    @SubscribeEvent
    public void onGetToolTipEvent(ItemTooltipEvent event)
    {
        Item item = event.getItemStack().getItem();

        if (item instanceof ItemPickaxe)
        {
            ItemPickaxe tool = (ItemPickaxe) item;

            int level = tool.getHarvestLevel(event.getItemStack(), "pickaxe", null, null);

            if (HarvestLevel.levels.containsKey(level))
            {
                event.getToolTip().add(String.format("Mining Level: %s", HarvestLevel.levels.get(level).getFormatted()));
            } else
            {
                event.getToolTip().add(String.format("Mining Level: %s", level));
            }
        }
    }
}
