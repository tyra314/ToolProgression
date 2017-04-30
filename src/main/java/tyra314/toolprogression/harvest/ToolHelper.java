package tyra314.toolprogression.harvest;

import net.minecraft.item.ItemStack;

public class ToolHelper
{


    public static int getHarvestLevel(ItemStack item, String toolClass)
    {
        return item.getItem().getHarvestLevel(item, toolClass, null, null);
    }

    public static boolean isPickaxe(ItemStack item)
    {
        return item.getItem().getToolClasses(item).contains("pickaxe");
    }

    public static boolean isAxe(ItemStack item)
    {
        return item.getItem().getToolClasses(item).contains("axe");
    }

    public static boolean isShovel(ItemStack item)
    {
        return item.getItem().getToolClasses(item).contains("shovel");
    }
}
