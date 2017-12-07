package tyra314.toolprogression.harvest;

import net.minecraft.item.Item;
import tyra314.toolprogression.compat.cofh.CoFHHelper;

public class ToolHelper
{
    public static void setHarvestLevel(Item item, String tool_class, int level)
    {
        if (CoFHHelper.isLoaded() && CoFHHelper.isInstance(item))
        {
            CoFHHelper.setHarvestLevel(item, level);
            if(level >= 0)
            {
                CoFHHelper.addToolClass(item, tool_class);
            }
        }
        else
        {
            item.setHarvestLevel(tool_class, level);
        }
    }
}
