package tyra314.toolprogression.handlers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
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

        int level = item.getHarvestLevel(event.getItemStack(), "pickaxe", null, null);

        if (level == -1 && item instanceof ItemTool && item.getToolClasses(event.getItemStack()).contains("pickaxe"))
        {
            ItemTool tool = (ItemTool) item;
            level = tool.getHarvestLevel(event.getItemStack(), "pickaxe", null, null);
        }

        if (level == -1)
        {
            return;
        }

        if (HarvestLevel.levels.containsKey(level))
        {
            event.getToolTip().add(String.format("§fMining Level:§r %s", HarvestLevel.levels.get(level).getFormatted()));
        } else
        {
            event.getToolTip().add(String.format("§fMining Level:§r %d", level));
        }
    }
}
