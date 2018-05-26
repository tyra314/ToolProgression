package tyra314.toolprogression.handlers;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.harvest.HarvestLevelName;

@SideOnly(Side.CLIENT)
public class TooltipEventHandler
{
    private static String formatLevel(int level)
    {
        if (HarvestLevelName.levels.containsKey(level))
        {
            return HarvestLevelName.levels.get(level).getFormatted();
        }

        return String.valueOf(level);
    }

    private static void printForClass(ItemTooltipEvent event,
                                      Item item,
                                      String tool_class,
                                      String postfix)
    {
        int level = item.getHarvestLevel(
                event.getItemStack(),
                tool_class,
                null,
                Blocks.STONE.getDefaultState());

        if (level == -1 &&
            item instanceof ItemTool &&
            item.getToolClasses(event.getItemStack()).contains(tool_class))
        {
            ItemTool tool = (ItemTool) item;
            level = tool.getHarvestLevel(
                    event.getItemStack(),
                    tool_class,
                    null,
                    Blocks.STONE.getDefaultState());
        }

        if (level == -1)
        {
            return;
        }

        event.getToolTip().add("§eMining Level:§r " + formatLevel(level) + postfix);
    }

    @SubscribeEvent
    public void onGetToolTipEvent(ItemTooltipEvent event)
    {
        if (!ConfigHandler.tooltip_enabled)
        {
            return;
        }

        Item item = event.getItemStack().getItem();

        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
            if (item instanceof ItemBlock)
            {
                Block block = ((ItemBlock) item).getBlock();

                @SuppressWarnings("deprecation")
                IBlockState state = block.getStateFromMeta(item.getDamage(event.getItemStack()));

                String tool_class = block.getHarvestTool(state);
                int level = block.getHarvestLevel(state);


                event.getToolTip()
                        .add("§eMining Level:§r " + formatLevel(level) + "(" + tool_class + ")");
            }
            else
            {
                for (String tool_class : item.getToolClasses(event.getItemStack()))
                {
                    printForClass(event, item, tool_class, "(" + tool_class + ")");
                }
            }
        }
        else
        {
            printForClass(event, item, "pickaxe", "");
        }
    }
}
