package tyra314.toolprogression.handlers;


import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.harvest.HarvestLevelName;

@SideOnly(Side.CLIENT)
public class TooltipEventHandler
{
    @SubscribeEvent
    public void onGetToolTipEvent(ItemTooltipEvent event)
    {
        if(!ConfigHandler.tooltip_enabled)
        {
            return;
        }

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

        if (HarvestLevelName.levels.containsKey(level))
        {
            event.getToolTip().add(String.format("§fMining Level:§r %s", HarvestLevelName.levels.get(level).getFormatted()));
        } else
        {
            event.getToolTip().add(String.format("§fMining Level:§r %d", level));
        }
    }
}
