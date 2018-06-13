package tyra314.toolprogression.compat.buildcraft;

import net.minecraft.item.Item;
import tyra314.toolprogression.ToolProgressionMod;

public class BCHelper
{
    public static final String HAMMER_NAME = "buildcraft.api.tools.IToolWrench";

    private static Class<?> buildcraft = null;


    static
    {
        try
        {
            buildcraft = Class.forName(HAMMER_NAME);
        }
        catch (ClassNotFoundException ignored)
        {
            ToolProgressionMod.logger.info("Buildcraft isn't loaded.");
        }
    }

    public static boolean isLoaded()
    {
        return buildcraft != null;
    }


    public static boolean isWrench(Item item)
    {
        try
        {
            Class<?> ToolCore = Class.forName(HAMMER_NAME);

            return ToolCore.isInstance(item);

        }
        catch (ClassNotFoundException ignored)
        {
        }

        return false;
    }
}
