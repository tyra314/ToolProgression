package tyra314.toolprogression.compat.rftools;

import net.minecraft.item.Item;
import tyra314.toolprogression.ToolProgressionMod;

public class RFHelper
{

    public static final String WRENCH_NAME = "mcjty.lib.api.smartwrench.SmartWrench";

    private static Class<?> rftools = null;

    static
    {
        try
        {
            rftools = Class.forName(WRENCH_NAME);
        }
        catch (ClassNotFoundException ignored)
        {
            ToolProgressionMod.logger.info("RFTools isn't loaded.");
        }
    }

    public static boolean isLoaded()
    {
        return rftools != null;
    }


    public static boolean isWrench(Item item)
    {
        try
        {
            Class<?> ToolCore = Class.forName(WRENCH_NAME);

            return ToolCore.isInstance(item);

        }
        catch (ClassNotFoundException ignored)
        {
        }

        return false;
    }
}


