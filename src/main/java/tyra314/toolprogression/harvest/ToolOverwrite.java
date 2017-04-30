package tyra314.toolprogression.harvest;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ToolOverwrite
{
    public final static Map<ResourceLocation, ToolOverwrite> overwrites = new HashMap();

    public static String getConfig(Item item)
    {
        if (!(item instanceof ItemTool))
        {
            return null;
        }

        String config = new String();

        for (String toolclass : item.getToolClasses(new ItemStack(item)))
        {
            int level = item.getHarvestLevel(new ItemStack(item), toolclass, null, null);

            String config_line = String.format("%s=%d", toolclass, level);

            if (!config.isEmpty())
            {
                config += "," + config_line;
            } else
            {
                config += config_line;
            }
        }

        return config;
    }

    public static void applyTo(Item item)
    {
        if (!(item instanceof ItemTool))
        {
            return;
        }


    }
}
