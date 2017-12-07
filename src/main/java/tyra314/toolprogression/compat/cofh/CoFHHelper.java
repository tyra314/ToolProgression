package tyra314.toolprogression.compat.cofh;

import net.minecraft.item.Item;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.config.ConfigHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CoFHHelper
{
    public static final String CLASS_NAME = "cofh.core.item.tool.ItemToolCore";

    private static Class<?> cofh_core = null;

    static
    {
        try
        {
            cofh_core = Class.forName(CLASS_NAME);
        }
        catch (ClassNotFoundException ignored)
        {
            ToolProgressionMod.logger.info("CoFHCore isn't loaded.");
        }
    }

    public static boolean isLoaded()
    {
        return ConfigHandler.cofh_compat && cofh_core != null;
    }

    public static void setHarvestLevel(Item item, int level)
    {
        try
        {
            Method setHarvestLevel = cofh_core.getDeclaredMethod("setHarvestLevel", int.class);
            setHarvestLevel.invoke(item, level);
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
            ToolProgressionMod.logger.warn("Failed to set harvest level of item '" +
                                           item.getRegistryName() +
                                           "'");
        }
    }

    public static void addToolClass(Item item, String tool_class)
    {
        try
        {
            Method addToolClass = cofh_core.getDeclaredMethod("addToolClass", String.class);
            addToolClass.invoke(item, tool_class);
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
            ToolProgressionMod.logger.warn("Failed to set tool class of item '" +
                                           item.getRegistryName() +
                                           "'");
        }
    }

    public static boolean isInstance(Item item)
    {
        try
        {
            Class<?> ToolCore = Class.forName(CLASS_NAME);

            return ToolCore.isInstance(item);

        }
        catch (ClassNotFoundException ignored)
        {
        }

        return false;
    }
}
