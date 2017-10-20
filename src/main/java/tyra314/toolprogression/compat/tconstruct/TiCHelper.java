package tyra314.toolprogression.compat.tconstruct;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TiCHelper
{
    public static boolean isLoaded()
    {
        try
        {
            return Class.forName("slimeknights.tconstruct.TConstruct") != null;
        }
        catch (ClassNotFoundException e)
        {
            return false;
        }
    }

    public static void preInit()
    {
        MinecraftForge.EVENT_BUS.register(new TiCEventHandler());
    }

    public static void init()
    {
        MagicMushroomMod.init();
    }

    @SideOnly(Side.CLIENT)
    public static void client_init()
    {
        TiCBookHelper.client_init();
    }
}
